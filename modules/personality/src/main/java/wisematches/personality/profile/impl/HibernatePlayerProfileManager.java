package wisematches.personality.profile.impl;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
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
public class HibernatePlayerProfileManager extends HibernateDaoSupport implements PlayerProfileManager {
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
		return getHibernateTemplate().get(HibernatePlayerProfile.class, personality.getId());
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void updateProfile(PlayerProfile playerProfile) {
		HibernatePlayerProfile hProfile = getHibernateTemplate().get(HibernatePlayerProfile.class, playerProfile.getPlayerId());
		hProfile.updatePlayerProfile(playerProfile);
		getHibernateTemplate().update(hProfile);

		for (PlayerProfileListener listener : listeners) {
			listener.playerProfileChanged(hProfile);
		}
	}

	private void removeProfile(PlayerProfile profile) {
		getHibernateTemplate().delete(profile);
	}

	private void createProfile(PlayerProfile profile) {
		getHibernateTemplate().save(profile);
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
