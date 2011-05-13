package wisematches.personality.player;

import wisematches.personality.Personality;
import wisematches.personality.account.Account;
import wisematches.personality.account.AccountManager;
import wisematches.personality.player.computer.ComputerPlayer;
import wisematches.personality.player.member.MemberPlayer;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultPlayerManager implements PlayerManager {
	private AccountManager accountManager;

	private final Map<Personality, Player> playerMap = new WeakHashMap<Personality, Player>();

	public DefaultPlayerManager() {
	}

	@Override
	public Player getPlayer(long playerId) {
		final ComputerPlayer computerPlayer = ComputerPlayer.getComputerPlayer(playerId);
		if (computerPlayer != null) {
			return computerPlayer;
		}
		return getMemberPlayer(accountManager.getAccount(playerId));
	}

	@Override
	public Player getPlayer(Personality personality) {
		return getPlayer(personality.getId());
	}

	private Player getMemberPlayer(Account account) {
		if (account == null) {
			return null;
		}
		Player p = playerMap.get(account);
		if (p == null) {
			p = new MemberPlayer(account);
			playerMap.put(account, p);
		}
		return p;
	}

	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}
}
