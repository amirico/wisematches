package wisematches.server.core.sessions.impl;

import wisematches.server.core.sessions.ImplementationBean;
import wisematches.server.core.sessions.ImplementationBeanType;
import wisematches.server.core.sessions.PlayerSessionBean;
import wisematches.server.player.Player;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
class ProxyFactory {
	private final Map<Class<?>, Class<?>> implementatations = new HashMap<Class<?>, Class<?>>();
	private final Collection<Class<? extends PlayerSessionBean>> interfaces = new ArrayList<Class<? extends PlayerSessionBean>>();

	ProxyFactory(Collection<Class<? extends PlayerSessionBean>> beans) {
		for (Class<? extends PlayerSessionBean> bean : beans) {
			collectionImplementations(bean);

			interfaces.add(bean);
		}
	}

	PlayerSessionBean createNewInstance(String sessionKey, Player player) {
		final PlayerSessionsInvocationHandler ih = new PlayerSessionsInvocationHandler(sessionKey, player, implementatations);
		final Class[] objects = interfaces.toArray(new Class[interfaces.size()]);
		return (PlayerSessionBean) Proxy.newProxyInstance(getClass().getClassLoader(), objects, ih);
	}

	private void collectionImplementations(Class<?> bean) {
		final Class<?>[] interfaces2 = bean.getInterfaces();
		for (Class<?> aClass : interfaces2) {
			collectionImplementations(aClass);
		}

		final ImplementationBean ib = bean.getAnnotation(ImplementationBean.class);
		if (ib != null) {
			implementatations.put(bean, ib.value());
		} else {
			final ImplementationBeanType annotation = bean.getAnnotation(ImplementationBeanType.class);
			if (annotation != null) {
				try {
					implementatations.put(bean, bean.getClassLoader().loadClass(annotation.value()));
				} catch (ClassNotFoundException e) {
					throw new AssertionError("Implementation class not found " + annotation.value() + "!!!!. This must be checked before...");
				}
			}
		}
	}

	private static final class PlayerSessionsInvocationHandler implements InvocationHandler {
		private final Map<String, Object> values = new HashMap<String, Object>();
		private final Map<Class<?>, Object> beansMap = new HashMap<Class<?>, Object>();

		public PlayerSessionsInvocationHandler(String sessionKey, Player player, Map<Class<?>, Class<?>> implementatations) {
			values.put("sessionKey", sessionKey);
			values.put("player", player);

			for (Map.Entry<Class<?>, Class<?>> entry : implementatations.entrySet()) {
				final Class<?> ifs = entry.getKey();
				final Class<?> impl = entry.getValue();
				try {
					beansMap.put(ifs, impl.newInstance());
				} catch (InstantiationException ex) {
					throw new IllegalStateException("Implementation bean '" + impl +
							"' can't be instanciated", ex);
				} catch (IllegalAccessException ex) {
					throw new IllegalStateException("Implementation bean '" + impl +
							"' can't be instanciated", ex);
				}
			}
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			final Class<?> declaringClass = method.getDeclaringClass();
			final Object o = beansMap.get(declaringClass);
			if (o != null) {
				return method.invoke(o, args);
			}

			final String name = method.getName();
			final String propertyName = getPropertyName(name);
			if (name.startsWith("get") || method.getName().startsWith("is")) {
				if (args != null && args.length != 0) {
					throw new IllegalStateException("Calling get method but method has more than one parameter: " + method);
				}
				return values.get(propertyName);
			} else if (method.getName().startsWith("set")) {
				if (args == null || args.length != 1) {
					throw new IllegalStateException("Calling get method but method has more than one parameter: " + method);
				}
				values.put(propertyName, args[0]);
			}
			return null;
		}

		private String getPropertyName(String name) {
			String res;
			if (name.startsWith("get") || name.startsWith("set")) {
				res = name.substring(3);
			} else if (name.startsWith("is")) {
				res = name.substring(2);
			} else {
				throw new IllegalArgumentException("Specified name is not JavaBean property method name: " + name);
			}
			if (res.length() == 0) {
				throw new IllegalArgumentException("Specified name is not JavaBean property method name: " + name);
			}
			return Character.toLowerCase(res.charAt(0)) + res.substring(1);
		}
	}
}
