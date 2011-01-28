package wisematches.server.utils.sessions.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.server.player.Player;
import wisematches.server.player.PlayerManager;
import wisematches.server.utils.sessions.*;
import wisematches.server.utils.sessions.chouse.PlayerCustomHouse;
import wisematches.server.utils.sessions.chouse.PlayerCustomHouseListener;

import java.beans.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerSessionsManagerImpl implements PlayerSessionsManager {
	private PlayerManager playerManager;

	private ProxyFactory proxyDescriptor;
	private Collection<PlayerCustomHouse> playerCustomHouses = new ArrayList<PlayerCustomHouse>();

	private final Map<Player, Set<String>> playerMap = new HashMap<Player, Set<String>>();
	private final Map<String, PlayerSessionBean> sessionBeanMap = new HashMap<String, PlayerSessionBean>();

	private final PlayerCustomHouseListener customHouseListener = new ThePlayerCustomHouseListener();

	private final Collection<PlayerSessionsListener> sessionListeners = new CopyOnWriteArraySet<PlayerSessionsListener>();
	private final Collection<PlayerOnlineStateListener> stateListeners = new CopyOnWriteArraySet<PlayerOnlineStateListener>();

	private final Lock lock = new ReentrantLock();

	private static final Log log = LogFactory.getLog(PlayerSessionsManagerImpl.class);

	public PlayerSessionsManagerImpl() {
	}

	@Override
	public void addPlayerSessionsListener(PlayerSessionsListener l) {
		sessionListeners.add(l);
	}

	@Override
	public void removePlayerSessionsListener(PlayerSessionsListener l) {
		sessionListeners.remove(l);
	}

	@Override
	public void addPlayerOnlineStateListener(PlayerOnlineStateListener l) {
		stateListeners.add(l);
	}

	@Override
	public void removePlayerOnlineStateListener(PlayerOnlineStateListener l) {
		stateListeners.remove(l);
	}


	@SuppressWarnings("unchecked")
	public <T extends PlayerSessionBean> T getPlayerSessionBean(String sessionKey) {
		lock.lock();
		try {
			return (T) sessionBeanMap.get(sessionKey);
		} finally {
			lock.unlock();
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends PlayerSessionBean> Collection<T> getPlayerSessionBeans(Player player) {
		lock.lock();
		try {
			final Collection<String> stringCollection = playerMap.get(player);
			if (stringCollection != null) {
				Collection<T> res = new ArrayList<T>(stringCollection.size());
				for (String s : stringCollection) {
					res.add((T) sessionBeanMap.get(s));
				}
				return res;
			} else {
				return Collections.emptyList();
			}
		} finally {
			lock.unlock();
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends PlayerSessionBean> Collection<T> getPlayerSessionBeans() {
		lock.lock();
		try {
			return (Collection<T>) sessionBeanMap.values();
		} finally {
			lock.unlock();
		}
	}


	public Collection<Player> getOnlinePlayers() {
		return Collections.unmodifiableCollection(playerMap.keySet());
	}

	public boolean isPlayerOnline(Player player) {
		return playerMap.containsKey(player);
	}


	private PlayerSessionBean createPlayerSessionBean(String sessionKey, Player player) {
		return proxyDescriptor.createNewInstance(sessionKey, player);
	}

	private void checkInterface(Class<?> bean) {
		if (!bean.isInterface()) {
			throw new IllegalArgumentException(bean + " is not a interface");
		}

		try {
			final Class<?>[] interfaces = bean.getInterfaces();
			for (Class<?> anInterface : interfaces) {
				checkInterface(anInterface);
			}

			final BeanInfo beanInfo = Introspector.getBeanInfo(bean);
			final PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

			final MethodDescriptor[] descriptors = beanInfo.getMethodDescriptors();
			final Collection<Method> methodDescriptors = new HashSet<Method>(descriptors.length);
			for (MethodDescriptor descriptor : descriptors) {
				methodDescriptors.add(descriptor.getMethod());
			}

			for (PropertyDescriptor pd : propertyDescriptors) {
				methodDescriptors.remove(pd.getWriteMethod());
				methodDescriptors.remove(pd.getReadMethod());
			}

			if (methodDescriptors.size() != 0) {
				Class implementationClass;
				final ImplementationBean annotation = bean.getAnnotation(ImplementationBean.class);
				if (annotation == null) {
					final ImplementationBeanType at = bean.getAnnotation(ImplementationBeanType.class);
					if (at == null) {
						throw new IllegalArgumentException(bean + " does not have 'ImplementationBean' or " +
								"'ImplementationBeanType' but contains not JavaBean methods: " +
								methodDescriptors.toString());
					} else {
						try {
							implementationClass = bean.getClassLoader().loadClass(at.value());
						} catch (ClassNotFoundException e) {
							throw new IllegalArgumentException("Implementation class " + at.value() + " not found");
						}
					}
				} else {
					implementationClass = annotation.value();
				}

				if (!bean.isAssignableFrom(implementationClass)) {
					throw new IllegalArgumentException("Implementation class is not implement " + bean);
				}
				checkImplementationClass(implementationClass);
			}
		} catch (IntrospectionException ex) {
			throw new IllegalArgumentException(bean + " can't be introspected", ex);
		}
	}


	private void checkImplementationClass(Class aClass) {
		final int modifiers = aClass.getModifiers();
		if (Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers)) {
			throw new IllegalArgumentException("Implementation class " + aClass + " is abstract or interface");
		}

		try {
			aClass.getConstructor();
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException("Implementation class " + aClass + " doesn't have default public constructor");
		}
	}


	public void setPlayerCustomHouses(Collection<PlayerCustomHouse> playerCustomHouses) {
		for (PlayerCustomHouse house : this.playerCustomHouses) {
			house.removePlayerCustomHouseListener(customHouseListener);
		}
		this.playerCustomHouses.clear();

		if (playerCustomHouses != null && playerCustomHouses.size() != 0) {
			for (PlayerCustomHouse playerCustomHouse : playerCustomHouses) {
				this.playerCustomHouses.add(playerCustomHouse);
				playerCustomHouse.addPlayerCustomHouseListener(customHouseListener);
			}
		}
	}

	public void setPlayerSessionBeanInterfaces(Collection<Class<? extends PlayerSessionBean>> beans) {
		for (Class<? extends PlayerSessionBean> bean : beans) {
			checkInterface(bean);
		}
		proxyDescriptor = new ProxyFactory(beans);
	}

	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}


	protected void firePlayerSessionsCreated(Player player, String sessionKey) {
		final PlayerSessionsEvent sessionsEvent = new PlayerSessionsEvent(player, sessionKey);
		for (PlayerSessionsListener listener : sessionListeners) {
			listener.playerSessionsCreated(sessionsEvent);
		}
	}

	protected void firePlayerSessionsRemoved(Player player, String sessionKey) {
		final PlayerSessionsEvent sessionsEvent = new PlayerSessionsEvent(player, sessionKey);
		for (PlayerSessionsListener listener : sessionListeners) {
			listener.playerSessionsRemoved(sessionsEvent);
		}
	}

	protected void firePlayerOnline(Player player) {
		for (PlayerOnlineStateListener listener : stateListeners) {
			listener.playerIsOnline(player);
		}
	}

	protected void firePlayerOffline(Player player) {
		for (PlayerOnlineStateListener listener : stateListeners) {
			listener.playerIsOffline(player);
		}
	}


	private class ThePlayerCustomHouseListener implements PlayerCustomHouseListener {
		public void playerMoveIn(Player player, String sessionKey) {
			if (log.isDebugEnabled()) {
				log.debug("Player moved in: " + player + " to session " + sessionKey);
			}

			// We add these flags because should fire events outside of lock
			boolean fireStateEvent = false;
			boolean fireSessionEvent = false;
			lock.lock();
			try {
				Set<String> stringCollection = playerMap.get(player);
				if (stringCollection == null) {
					stringCollection = new HashSet<String>();
					playerMap.put(player, stringCollection);

					fireStateEvent = true;
				}
				if (stringCollection.add(sessionKey)) {
					final PlayerSessionBean playerSessionBean = createPlayerSessionBean(sessionKey, player);
					sessionBeanMap.put(sessionKey, playerSessionBean);

					fireSessionEvent = true;
				} else {
					log.warn("Player move in event receive twise for the same session: " + player + "->" + sessionKey);
				}
			} finally {
				lock.unlock();
			}

			// TODO: commented!
//            player.setLastSigninDate(new Date());
//            playerManager.updatePlayer(player);

			if (fireStateEvent) {
				firePlayerOnline(player);
			}

			if (fireSessionEvent) {
				firePlayerSessionsCreated(player, sessionKey);
			}
		}

		public void playerMoveOut(Player player, String sessionKey) {
			if (log.isDebugEnabled()) {
				log.debug("Player moved out: " + player + " from session " + sessionKey);
			}

			// We add these flags because should fire events outside of lock
			boolean fireSessionEvent = false;
			boolean fireStateEvent = false;
			lock.lock();
			try {
				if (sessionBeanMap.remove(sessionKey) != null) {
					final Collection<String> sessionKeys = playerMap.get(player);
					if (sessionKeys != null) {
						if (sessionKeys.remove(sessionKey)) {
							fireSessionEvent = true;
						} else {
							if (log.isDebugEnabled()) {
								log.debug("Player was not removed from " + sessionKey);
							}
						}

						if (sessionKeys.size() == 0) {
							if (log.isDebugEnabled()) {
								log.debug("Player removed from playersMap because there is no one associated sessions");
							}
							playerMap.remove(player);
							fireStateEvent = true;

						} else {
							if (log.isDebugEnabled()) {
								log.debug("Player is not removed because still have associated sessions: " + sessionKeys);
							}
						}
					}
				} else {
					if (log.isDebugEnabled()) {
						log.debug("Session for this player is not exist.");
					}
				}
			} finally {
				lock.unlock();
			}

			if (fireSessionEvent) {
				firePlayerSessionsRemoved(player, sessionKey);
			}

			if (fireStateEvent) {
				firePlayerOffline(player);
			}
		}
	}
}
