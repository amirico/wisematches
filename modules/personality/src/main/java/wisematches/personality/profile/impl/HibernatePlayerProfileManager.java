package wisematches.personality.profile.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Personality;
import wisematches.personality.account.Account;
import wisematches.personality.account.AccountListener;
import wisematches.personality.account.AccountManager;
import wisematches.personality.profile.PlayerProfile;
import wisematches.personality.profile.PlayerProfileListener;
import wisematches.personality.profile.PlayerProfileManager;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernatePlayerProfileManager implements PlayerProfileManager {
	private SessionFactory sessionFactory;
	private AccountManager accountManager;

	private final TheAccountListener accountListener = new TheAccountListener();
	private final Collection<PlayerProfileListener> listeners = new CopyOnWriteArraySet<PlayerProfileListener>();

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
	public PlayerProfile getPlayerProfile(Personality personality) {
		final Session session = sessionFactory.getCurrentSession();
		return (PlayerProfile) session.get(HibernatePlayerProfile.class, personality.getId());
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void updateProfile(PlayerProfile playerProfile) {
		final Session session = sessionFactory.getCurrentSession();
		final HibernatePlayerProfile hProfile = (HibernatePlayerProfile) session.get(HibernatePlayerProfile.class, playerProfile.getPlayerId());
		hProfile.updatePlayerProfile(playerProfile);
		session.update(hProfile);

		for (PlayerProfileListener listener : listeners) {
			listener.playerProfileChanged(hProfile);
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

	public void setAccountManager(AccountManager accountManager) {
		if (this.accountManager != null) {
			this.accountManager.removeAccountListener(accountListener);
		}

		this.accountManager = accountManager;

		if (this.accountManager != null) {
			this.accountManager.addAccountListener(accountListener);
		}
	}

	private class TheAccountListener implements AccountListener {
		private TheAccountListener() {
		}

		@Override
		public void accountCreated(Account account) {
			createProfile(new HibernatePlayerProfile(account));
		}

		@Override
		public void accountRemove(Account account) {
			final PlayerProfile profile = getPlayerProfile(account);
			if (profile != null) {
				removeProfile(profile);
			}
		}

		@Override
		public void accountUpdated(Account oldAccount, Account newAccount) {
		}
	}
}
