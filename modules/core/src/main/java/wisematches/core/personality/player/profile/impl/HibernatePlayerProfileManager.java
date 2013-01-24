package wisematches.core.personality.player.profile.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.personality.player.account.Account;
import wisematches.core.personality.player.account.AccountListener;
import wisematches.core.personality.player.account.AccountManager;
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
	private AccountManager accountManager;

	private final TheAccountListener accountListener = new TheAccountListener();
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
	public PlayerProfile getPlayerProfile(Account account) {
		final Session session = sessionFactory.getCurrentSession();
		return (PlayerProfile) session.get(HibernatePlayerProfile.class, account.getId());
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void updateProfile(Account account, PlayerProfile playerProfile) {
		final Session session = sessionFactory.getCurrentSession();
		final HibernatePlayerProfile hProfile = (HibernatePlayerProfile) session.get(HibernatePlayerProfile.class, account.getId());
		hProfile.updatePlayerProfile(playerProfile);
		session.update(hProfile);

		for (PlayerProfileListener listener : listeners) {
			listener.playerProfileChanged(account, hProfile);
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
