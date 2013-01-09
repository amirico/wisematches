package wisematches.playground.tourney.regular.impl;

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
	private long internalId;

	@Column(name = "roundNumber", updatable = false)
	private int round;

	@Column(name = "totalGamesCount", updatable = true)
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

	public HibernateTourneyRound() {
	}

	public HibernateTourneyRound(HibernateTourneyDivision division, int round) {
		this.round = round;
		this.division = division;
	}

	long getInternalId() {
		return internalId;
	}

	@Override
	public State getState() {
		return State.getState(startedDate, finishedDate);
	}

	@Override
	public int getRound() {
		return round;
	}

	@Override
	public HibernateTourneyDivision getDivision() {
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
	public boolean isFinal() {
		return totalGamesCount == 1; // 1 game - last round
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

	void gamesStarted(int gamesCount) {
		if (startedDate != null) {
			throw new IllegalStateException("Round already started");
		}
		this.totalGamesCount += gamesCount;
		startedDate = new Date();
	}

	void gamesFinished(int gamesCount) {
		if (finishedDate != null) {
			throw new IllegalStateException("Round already finished");
		}
		finishedGamesCount += gamesCount;
		if (totalGamesCount == finishedGamesCount) {
			finishedDate = new Date();
		}
	}

	@Override
	public String toString() {
		return "HibernateTourneyRound{" +
				"internalId=" + internalId +
				", round=" + round +
				", division=" + division +
				", gamesCount=" + totalGamesCount +
				", finishedGamesCount=" + finishedGamesCount +
				", startedDate=" + startedDate +
				", finishedDate=" + finishedDate +
				'}';
	}
}
