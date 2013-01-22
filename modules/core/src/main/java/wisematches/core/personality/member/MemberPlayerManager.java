package wisematches.core.personality.member;

import wisematches.core.personality.PlayerManager;
import wisematches.core.personality.member.account.Account;
import wisematches.core.personality.member.account.AccountManager;
import wisematches.core.personality.member.membership.MembershipManager;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MemberPlayerManager implements PlayerManager {
	private AccountManager accountManager;
	private MembershipManager membershipManager;

	private final Map<Long, MemberPlayer> playerMap = new WeakHashMap<>();

	public MemberPlayerManager() {
	}

	@Override
	public MemberPlayer getPlayer(long pid) {
		if (pid < 1000) {
			return null;
		}

		final Long id = pid;
		MemberPlayer player = playerMap.get(id);
		if (player == null) {
			final Account account = accountManager.getAccount(pid);
			if (account != null) {
				final Membership membership = membershipManager.getMembership(player);
				player = new MemberPlayer(account, membership);
				playerMap.put(id, player);
			}
		}
		return player;
	}

	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	public void setMembershipManager(MembershipManager membershipManager) {
		this.membershipManager = membershipManager;
	}
}
