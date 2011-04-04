package wisematches.server.personality.player;

import wisematches.server.personality.Personality;
import wisematches.server.personality.account.Account;
import wisematches.server.personality.account.AccountManager;
import wisematches.server.personality.player.computer.ComputerPlayer;
import wisematches.server.personality.player.member.MemberPlayer;
import wisematches.server.standing.rating.PlayerRatingManager;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class StandingPlayerManager implements PlayerManager {
	private AccountManager accountManager;
	private PlayerRatingManager ratingManager;

	private final Map<Personality, Player> playerMap = new WeakHashMap<Personality, Player>();

	public StandingPlayerManager() {
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

	@Override
	public Player findByEmail(String email) {
		return getMemberPlayer(accountManager.findByEmail(email));
	}

	@Override
	public Player findByUsername(String username) {
		return getMemberPlayer(accountManager.findByUsername(username));
	}

	private Player getMemberPlayer(Account account) {
		if (account == null) {
			return null;
		}
		Player p = playerMap.get(account);
		if (p == null) {
			p = new MemberPlayer(account, this);
			playerMap.put(account, p);
		}
		return p;
	}

	short getRating(StandingPlayer player) {
		return ratingManager.getRating(player);
	}

	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	public void setRatingManager(PlayerRatingManager ratingManager) {
		this.ratingManager = ratingManager;
	}
}
