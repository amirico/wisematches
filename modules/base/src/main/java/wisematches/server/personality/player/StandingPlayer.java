package wisematches.server.personality.player;

import wisematches.server.personality.account.Language;
import wisematches.server.personality.account.Membership;
import wisematches.server.standing.profile.PlayerProfile;
import wisematches.server.standing.rating.RatingChange;
import wisematches.server.standing.statistic.PlayerStatistic;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class StandingPlayer extends Player {
	private final StandingPlayerManager standingPlayerManager;

	protected StandingPlayer(long id, StandingPlayerManager standingPlayerManager) {
		super(id);
		this.standingPlayerManager = standingPlayerManager;
	}

	@Override
	public short getRating() {
		return standingPlayerManager.getRating(this);
	}

	@Override
	public long getPosition() {
		return standingPlayerManager.getPosition(this);
	}

	@Override
	public PlayerProfile getPlayerProfile() {
		return standingPlayerManager.getProfile(this);
	}

	@Override
	public PlayerStatistic getPlayerStatistic() {
		return standingPlayerManager.getPlayerStatistic(this);
	}

	@Override
	public Collection<RatingChange> getRatingChanges() {
		return standingPlayerManager.getRatingChanges(this);
	}
}
