package wisematches.core.personality.player;

import wisematches.core.personality.PlayerManager;
import wisematches.core.personality.player.account.Account;
import wisematches.core.personality.player.account.AccountListener;
import wisematches.core.personality.player.account.AccountManager;
import wisematches.core.personality.player.membership.MembershipCard;
import wisematches.core.personality.player.membership.MembershipListener;
import wisematches.core.personality.player.membership.MembershipManager;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class MemberPlayerManager extends AbstractPlayerManager implements PlayerManager {
	private AccountManager accountManager;
	private MembershipManager membershipManager;

	private final Map<Long, MemberPlayer> playerMap = new WeakHashMap<>();
	private final TheMemberPlayerListener memberPlayerListener = new TheMemberPlayerListener();

	public MemberPlayerManager() {
	}

	@Override
	public MemberPlayer getPlayer(Long pid) {
		MemberPlayer player = playerMap.get(pid);
		if (player == null) {
			final Account account = accountManager.getAccount(pid);
			if (account != null) {
				playerMap.put(pid, createMemberPlayer(account));
			}
		}
		return player;
	}

	public void setAccountManager(AccountManager accountManager) {
		if (this.accountManager != null) {
			this.accountManager.removeAccountListener(memberPlayerListener);
		}

		this.accountManager = accountManager;

		if (this.accountManager != null) {
			this.accountManager.addAccountListener(memberPlayerListener);
		}
	}

	public void setMembershipManager(MembershipManager membershipManager) {
		if (this.membershipManager != null) {
			this.membershipManager.removeMembershipListener(memberPlayerListener);
		}

		this.membershipManager = membershipManager;

		if (this.membershipManager != null) {
			this.membershipManager.addMembershipListener(memberPlayerListener);
		}
	}

	private MemberPlayer createMemberPlayer(Account account) {
		final MembershipCard card = membershipManager.getPlayerMembership(account);
		return new MemberPlayer(account, card);
	}

	private final class TheMemberPlayerListener implements AccountListener, MembershipListener {
		private TheMemberPlayerListener() {
		}

		@Override
		public void accountCreated(Account account) {
			firePlayerRegistered(createMemberPlayer(account));
		}

		@Override
		public void accountRemove(Account account) {
			final MemberPlayer remove = playerMap.remove(account.getId());
			if (remove != null) {
				firePlayerRegistered(remove);
			} else {
				firePlayerRegistered(createMemberPlayer(account));
			}
		}

		@Override
		public void accountUpdated(Account oldAccount, Account newAccount) {
			final MemberPlayer memberPlayer = playerMap.get(oldAccount.getId());
			if (memberPlayer != null) {
				memberPlayer.setAccount(newAccount);
			}
		}

		@Override
		public void membershipCardUpdated(Account account, MembershipCard oldCard, MembershipCard newCard) {
			final MemberPlayer memberPlayer = playerMap.get(account.getId());
			if (memberPlayer != null) {
				memberPlayer.setMembershipCard(newCard);
			}
		}
	}
}
