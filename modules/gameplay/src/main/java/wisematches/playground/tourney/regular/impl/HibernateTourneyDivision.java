package wisematches.playground.tourney.regular.impl;

import wisematches.core.Language;
import wisematches.playground.tourney.TourneyPlace;
import wisematches.playground.tourney.TourneyWinner;
import wisematches.playground.tourney.regular.TourneyDivision;
import wisematches.playground.tourney.regular.TourneyRound;
import wisematches.playground.tourney.regular.TourneySection;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "tourney_regular_division")
public class HibernateTourneyDivision implements TourneyDivision {
	@Column(name = "id")
	@javax.persistence.Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long internalId;

	@OneToOne
	@JoinColumn(name = "activeRound")
	private HibernateTourneyRound activeRound;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "language", updatable = false)
	private Language language;

	@Column(name = "section", updatable = false)
	private TourneySection section;

	@JoinColumn(name = "tourneyId")
	@OneToOne(fetch = FetchType.LAZY)
	private HibernateTourney tourney;

	@Column(name = "roundsCount", updatable = false)
	private int roundsCount;

	@Column(name = "started")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startedDate;

	@Column(name = "finished")
	@Temporal(TemporalType.TIMESTAMP)
	private Date finishedDate;

	@ElementCollection(targetClass = HibernateTourneyWinner.class)
	@CollectionTable(name = "tourney_regular_winner", joinColumns = @JoinColumn(name = "division"))
	private Collection<TourneyWinner> tourneyWinners;

	public HibernateTourneyDivision() {
	}

	public HibernateTourneyDivision(HibernateTourney tourney, Language language, TourneySection section) {
		this.tourney = tourney;
		this.language = language;
		this.section = section;
		this.activeRound = null;
	}


	long getInternalId() {
		return internalId;
	}

	@Override
	public State getState() {
		return State.getState(startedDate, finishedDate);
	}

	@Override
	public TourneyRound getActiveRound() {
		return activeRound;
	}

	@Override
	public Language getLanguage() {
		return language;
	}

	@Override
	public TourneySection getSection() {
		return section;
	}

	@Override
	public HibernateTourney getTourney() {
		return tourney;
	}

	@Override
	public int getRoundsCount() {
		return roundsCount;
	}

	@Override
	public Id getId() {
		return new Id(tourney.getId(), language, section);
	}

	@Override
	public Date getStartedDate() {
		return startedDate;
	}

	@Override
	public Date getFinishedDate() {
		return finishedDate;
	}

	@Override
	public TourneyPlace getTourneyPlace(long pid) {
		for (TourneyWinner tourneyWinner : tourneyWinners) {
			if (tourneyWinner.getPlayer() == pid) {
				return tourneyWinner.getPlace();
			}
		}
		return null;
	}

	@Override
	public Collection<TourneyWinner> getTourneyWinners() {
		return tourneyWinners;
	}

	void startRound(HibernateTourneyRound round) {
		if (finishedDate != null) {
			throw new IllegalStateException("Division already finished");
		}
		if (activeRound == null) {
			startedDate = new Date();
		}
		roundsCount++;
		activeRound = round;
	}

	boolean finishRound(HibernateTourneyRound round) {
		if (finishedDate != null) {
			throw new IllegalStateException("Division already finished");
		}
		return round.isFinal();
	}

	void finishDivision(List<HibernateTourneyWinner> winners) {
		if (finishedDate != null) {
			throw new IllegalStateException("Division is not finished");
		}

		if (tourneyWinners != null && tourneyWinners.size() != 0) {
			throw new IllegalStateException("Winners already set. Change is not possible.");
		}

		activeRound = null;
		finishedDate = new Date();

		tourneyWinners = new ArrayList<>();
		tourneyWinners.addAll(winners);
	}

	@Override
	public String toString() {
		return "HibernateTourneyDivision{" +
				"internalId=" + internalId +
				", round=" + (activeRound != null ? activeRound.getRound() : "null") +
				", tourney=" + tourney +
				", section=" + section +
				", language=" + language +
				", startedDate=" + startedDate +
				", finishedDate=" + finishedDate +
				'}';
	}
}
