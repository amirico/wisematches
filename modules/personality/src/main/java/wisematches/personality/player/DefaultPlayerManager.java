package wisematches.personality.player;

import wisematches.personality.Personality;
import wisematches.personality.account.Account;
import wisematches.personality.account.AccountListener;
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
	private final AccountListener accountListener = new TheAccountListener();

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
		if (personality instanceof Player) {
			return (Player) personality;
		}
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
		}

		@Override
		public void accountRemove(Account account) {
			playerMap.remove(account);
		}

		@Override
		public void accountUpdated(Account oldAccount, Account newAccount) {
			if (playerMap.containsKey(oldAccount)) {
				playerMap.put(oldAccount, new MemberPlayer(newAccount));
			}
		}
	}
}
