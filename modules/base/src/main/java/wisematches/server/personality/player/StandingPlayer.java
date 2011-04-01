package wisematches.server.personality.player;

import wisematches.server.personality.account.Language;
import wisematches.server.personality.account.Membership;
import wisematches.server.standing.rating.RatingHistory;

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
	public abstract String getNickname();


	@Override
	public abstract Language getLanguage();

	@Override
	public abstract Membership getMembership();


	@Override
	public short getRating() {
		return standingPlayerManager.getRating(this);
	}

	@Override
	public RatingHistory getRatingHistory() {
		return standingPlayerManager.getRatingHistory(this);
	}
}
