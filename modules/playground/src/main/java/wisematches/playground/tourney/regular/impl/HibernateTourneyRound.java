package wisematches.playground.tourney.regular.impl;

import wisematches.playground.tourney.regular.TourneyDivision;
import wisematches.playground.tourney.regular.TourneyRound;

import javax.persistence.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "tourney_regular_round")
public class HibernateTourneyRound extends HibernateTourneyEntity implements TourneyRound {
	@Column(name = "id")
	@javax.persistence.Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "number")
	private int number;

	@Column(name = "totalGamesCount", updatable = false)
	private int totalGamesCount;

	@Column(name = "finishedGamesCount", updatable = true)
	private int finishedGamesCount;

	@OneToOne
	@JoinColumn(name = "division")
	private HibernateTourneyDivision division;

	private HibernateTourneyRound() {
	}

	public HibernateTourneyRound(int number, HibernateTourneyDivision division, int totalGamesCount) {
		this.number = number;
		this.totalGamesCount = totalGamesCount;
		this.division = division;
	}


	long getDbId() {
		return id;
	}

	@Override
	public int getNumber() {
		return number;
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
		return new TourneyRound.Id(division.getId(), number);
	}

	void gameFinished() {
		finishedGamesCount = finishedGamesCount + 1;
	}


	@Override
	public String toString() {
		return "HibernateTourneyRound{" +
				"number=" + number +
				", totalGamesCount=" + totalGamesCount +
				", finishedGamesCount=" + finishedGamesCount +
				", division=" + division +
				'}';
	}
}
