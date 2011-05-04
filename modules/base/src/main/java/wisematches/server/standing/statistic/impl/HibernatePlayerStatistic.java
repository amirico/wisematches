package wisematches.server.standing.statistic.impl;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import wisematches.server.personality.Personality;
import wisematches.server.standing.statistic.PlayerStatistic;
import wisematches.server.standing.statistic.statistician.RatingsStatisticEditor;
import wisematches.server.standing.statistic.statistician.GamesStatisticEditor;
import wisematches.server.standing.statistic.statistician.MovesStatisticEditor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@Entity
@Table(name = "rating_statistic")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
class HibernatePlayerStatistic implements PlayerStatistic, Serializable {
	@Id
	@Column(name = "playerId", insertable = true, updatable = false)
	private long playerId;

	@Column(name = "updateTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;

	@Embedded
	private GamesStatisticEditor gamesStatistic = new GamesStatisticEditor();

	@Embedded
	private MovesStatisticEditor movesStatistic = new MovesStatisticEditor();

	@Embedded
	private RatingsStatisticEditor ratingsStatistic = new RatingsStatisticEditor();

	/**
	 * Hibernate constructor
	 *
	 * @deprecated hibernate constructor
	 */
	@Deprecated
	HibernatePlayerStatistic() {
	}

	public HibernatePlayerStatistic(Personality personality) {
		this.playerId = personality.getId();
	}

	public long getPlayerId() {
		return playerId;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public GamesStatisticEditor getGamesStatistic() {
		return gamesStatistic;
	}

	public MovesStatisticEditor getMovesStatistic() {
		return movesStatistic;
	}

	public RatingsStatisticEditor getRatingsStatistic() {
		return ratingsStatistic;
	}
}