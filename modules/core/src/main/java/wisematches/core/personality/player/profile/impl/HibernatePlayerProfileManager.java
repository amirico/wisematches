package wisematches.core.personality.player.profile.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.Personality;
import wisematches.core.PersonalityListener;
import wisematches.core.PersonalityManager;
import wisematches.core.Player;
import wisematches.core.personality.player.profile.PlayerProfile;
import wisematches.core.personality.player.profile.PlayerProfileListener;
import wisematches.core.personality.player.profile.PlayerProfileManager;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernatePlayerProfileManager implements PlayerProfileManager {
	private SessionFactory sessionFactory;
	private PersonalityManager personalityManager;

	private final PersonalityListener personalityListener = new ThePersonalityListener();
	private final Collection<PlayerProfileListener> listeners = new CopyOnWriteArraySet<>();

	public HibernatePlayerProfileManager() {
	}

	@Override
	public void addPlayerProfileListener(PlayerProfileListener l) {
		if (l != null) {
			listeners.add(l);
		}
	}

	@Override
	public void removePlayerProfileListener(PlayerProfileListener l) {
		listeners.remove(l);
	}

	@Override
	public PlayerProfile getPlayerProfile(Player player) {
		final Session session = sessionFactory.getCurrentSession();
		return (PlayerProfile) session.get(HibernatePlayerProfile.class, player.getId());
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void updateProfile(Player player, PlayerProfile playerProfile) {
		final Session session = sessionFactory.getCurrentSession();
		final HibernatePlayerProfile hProfile = (HibernatePlayerProfile) session.get(HibernatePlayerProfile.class, player.getId());
		hProfile.updatePlayerProfile(playerProfile);
		session.update(hProfile);

		for (PlayerProfileListener listener : listeners) {
			listener.playerProfileChanged(player, hProfile);
		}
	}

	private void removeProfile(PlayerProfile profile) {
		sessionFactory.getCurrentSession().delete(profile);
	}

	private void createProfile(PlayerProfile profile) {
		sessionFactory.getCurrentSession().save(profile);
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setPersonalityManager(PersonalityManager personalityManager) {
		if (this.personalityManager != null) {
			this.personalityManager.removePersonalityListener(personalityListener);
		}

		this.personalityManager = personalityManager;

		if (this.personalityManager != null) {
			this.personalityManager.addPersonalityListener(personalityListener);
		}
	}

	private class ThePersonalityListener implements PersonalityListener {
		private ThePersonalityListener() {
		}

		@Override
		public void playerRegistered(Personality player) {
			if (player instanceof Player) {
				createProfile(new HibernatePlayerProfile((Player) player));
			}
		}

		@Override
		public void playerUnregistered(Personality player) {
			if (player instanceof Player) {
				final PlayerProfile profile = getPlayerProfile((Player) player);
				if (profile != null) {
					removeProfile(profile);
				}
			}
		}
	}
}
