package wisematches.playground.tourney.regular.impl;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "tournament_config")
class HibernateTourneyConfiguration {
	@Id
	@Column(name = "tournament")
	private int tournament;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lastDivisionCheck")
	private Date lastDivisionCheck;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lastRoundCheck")
	private Date lastRoundCheck;

	@Deprecated
	HibernateTourneyConfiguration() {
	}

	public HibernateTourneyConfiguration(int tournament) {
		this.tournament = tournament;
	}

	public int getTournament() {
		return tournament;
	}

	public Date getLastDivisionCheck() {
		return lastDivisionCheck;
	}

	public void setLastDivisionCheck(Date lastDivisionCheck) {
		this.lastDivisionCheck = lastDivisionCheck;
	}

	public Date getLastRoundCheck() {
		return lastRoundCheck;
	}

	public void setLastRoundCheck(Date lastRoundCheck) {
		this.lastRoundCheck = lastRoundCheck;
	}
}
