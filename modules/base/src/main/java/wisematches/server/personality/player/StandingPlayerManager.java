package wisematches.server.personality.player;

import wisematches.server.personality.Personality;
import wisematches.server.personality.account.Account;
import wisematches.server.personality.account.AccountManager;
import wisematches.server.personality.player.computer.ComputerPlayer;
import wisematches.server.personality.player.member.MemberPlayer;
import wisematches.server.standing.profile.PlayerProfile;
import wisematches.server.standing.profile.PlayerProfileManager;
import wisematches.server.standing.rating.PlayerRatingManager;
import wisematches.server.standing.rating.RatingBatch;
import wisematches.server.standing.rating.RatingBatching;
import wisematches.server.standing.rating.RatingChange;
import wisematches.server.standing.statistic.PlayerStatistic;
import wisematches.server.standing.statistic.PlayerStatisticManager;

import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class StandingPlayerManager implements PlayerManager {
	private AccountManager accountManager;
	private PlayerRatingManager ratingManager;
	private PlayerProfileManager profileManager;
	private PlayerStatisticManager statisticsManager;

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

	long getPosition(StandingPlayer standingPlayer) {
		return ratingManager.getPosition(standingPlayer);
	}

	PlayerProfile getProfile(StandingPlayer standingPlayer) {
		return profileManager.getPlayerProfile(standingPlayer);
	}

	PlayerStatistic getPlayerStatistic(StandingPlayer standingPlayer) {
		return statisticsManager.getPlayerStatistic(standingPlayer);
	}

	Collection<RatingBatch> getRatingChanges(StandingPlayer standingPlayer, RatingBatching batching) {
		return ratingManager.getRatingChanges(standingPlayer, batching);
	}

	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	public void setRatingManager(PlayerRatingManager ratingManager) {
		this.ratingManager = ratingManager;
	}

	public void setStatisticsManager(PlayerStatisticManager statisticsManager) {
		this.statisticsManager = statisticsManager;
	}

	public void setProfileManager(PlayerProfileManager profileManager) {
		this.profileManager = profileManager;
	}
}
