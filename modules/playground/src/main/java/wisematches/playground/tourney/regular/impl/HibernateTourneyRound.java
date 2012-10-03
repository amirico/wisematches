package wisematches.playground.tourney.regular.impl;

import wisematches.playground.tourney.regular.TourneyDivision;
import wisematches.playground.tourney.regular.TourneyRound;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "tourney_regular_round")
public class HibernateTourneyRound implements TourneyRound {
	@Column(name = "id")
	@javax.persistence.Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "roundNumber")
	private int round;

	@Column(name = "totalGamesCount", updatable = false)
	private int totalGamesCount;

	@Column(name = "finishedGamesCount", updatable = true)
	private int finishedGamesCount;

	@OneToOne
	@JoinColumn(name = "divisionId")
	private HibernateTourneyDivision division;

	@Column(name = "started")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startedDate;

	@Column(name = "finished")
	@Temporal(TemporalType.TIMESTAMP)
	private Date finishedDate;

	@Column(name = "lastChange")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastChange;

	@Deprecated
	private HibernateTourneyRound() {
	}

	public HibernateTourneyRound(int round, HibernateTourneyDivision division, int totalGamesCount) {
		this.round = round;
		this.division = division;
		this.totalGamesCount = totalGamesCount;
		lastChange = startedDate = new Date();
	}


	long getDbId() {
		return id;
	}

	@Override
	public int getRound() {
		return round;
	}

	@Override
	public TourneyDivision getDivision() {
		return division;
	}

	@Override
	public int getTotalGamesCount() {
		return totalGamesCount;
	}

	@Override
	public int getFinishedGamesCount() {
		return finishedGamesCount;
	}

	@Override
	public Id getId() {
		return new TourneyRound.Id(division.getId(), round);
	}

	@Override
	public Date getStartedDate() {
		return startedDate;
	}

	@Override
	public Date getFinishedDate() {
		return finishedDate;
	}

	void gameFinished() {
		if (finishedDate != null) {
			throw new IllegalStateException("Round already finished");
		}
		finishedGamesCount += 1;
		lastChange = new Date();
		if (totalGamesCount == finishedGamesCount) {
			finishedDate = lastChange;
		}
	}

	@Override
	public String toString() {
		return "HibernateTourneyRound{" +
				"id=" + id +
				", round=" + round +
				", division=" + division +
				", totalGamesCount=" + totalGamesCount +
				", finishedGamesCount=" + finishedGamesCount +
				", startedDate=" + startedDate +
				", finishedDate=" + finishedDate +
				", lastChange=" + lastChange +
				'}';
	}
}
