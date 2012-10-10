package wisematches.playground.tourney.regular.impl;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "tourney_regular_config")
public class HibernateTourneyChanges {
	@Id
	@Column(name = "id")
	private long tourney;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lastDivisionCheck")
	private Date lastDivisionCheck;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lastRoundCheck")
	private Date lastRoundCheck;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lastGroupCheck")
	private Date lastGroupCheck;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lastTourneyCheck")
	private Date lastTourneyCheck;

	@Deprecated
	private HibernateTourneyChanges() {
	}

	HibernateTourneyChanges(HibernateTourney tourney) {
		this.tourney = tourney.getDbId();
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

	public Date getLastGroupCheck() {
		return lastGroupCheck;
	}

	public void setLastGroupCheck(Date lastGroupCheck) {
		this.lastGroupCheck = lastGroupCheck;
	}

	public Date getLastTourneyCheck() {
		return lastTourneyCheck;
	}

	public void setLastTourneyCheck(Date lastTourneyCheck) {
		this.lastTourneyCheck = lastTourneyCheck;
	}
}
