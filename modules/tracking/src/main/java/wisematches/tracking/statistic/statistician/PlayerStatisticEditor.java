package wisematches.tracking.statistic.statistician;

import wisematches.personality.Personality;
import wisematches.tracking.statistic.GamesStatistic;
import wisematches.tracking.statistic.MovesStatistic;
import wisematches.tracking.statistic.PlayerStatistic;
import wisematches.tracking.statistic.RatingsStatistic;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@MappedSuperclass
public abstract class PlayerStatisticEditor implements PlayerStatistic, Serializable {
	@Id
	@Column(name = "playerId", insertable = true, updatable = false)
	private long playerId;

	@Column(name = "updateTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;

	@Embedded
	private GamesStatisticEditor gamesStatistic;

	@Embedded
	private MovesStatisticEditor movesStatistic;

	@Embedded
	private RatingsStatisticEditor ratingsStatistic;

	private static final long serialVersionUID = 7069115060834598864L;

	@Deprecated
	protected PlayerStatisticEditor() {
	}

	public PlayerStatisticEditor(Personality personality, GamesStatisticEditor gamesStatistic, MovesStatisticEditor movesStatistic, RatingsStatisticEditor ratingsStatistic) {
		this.playerId = personality.getId();
		this.gamesStatistic = gamesStatistic;
		this.movesStatistic = movesStatistic;
		this.ratingsStatistic = ratingsStatistic;
	}

	@Override
	public long getPlayerId() {
		return playerId;
	}

	@Override
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public GamesStatistic getGamesStatistic() {
		return gamesStatistic;
	}

	@Override
	public MovesStatistic getMovesStatistic() {
		return movesStatistic;
	}

	@Override
	public RatingsStatistic getRatingsStatistic() {
		return ratingsStatistic;
	}

	public RatingsStatisticEditor getRatingsStatisticEditor() {
		return ratingsStatistic;
	}


	public GamesStatisticEditor getGamesStatisticEditor() {
		return gamesStatistic;
	}


	public MovesStatisticEditor getMovesStatisticEditor() {
		return movesStatistic;
	}
}
