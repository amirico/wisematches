package wisematches.playground.tourney.regular.impl;

import wisematches.personality.Language;
import wisematches.playground.tourney.regular.RegularTourney;
import wisematches.playground.tourney.regular.TourneyDivision;
import wisematches.playground.tourney.regular.TourneySection;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "tourney_regular_division")
public class HibernateTourneyDivision implements TourneyDivision {
	@Column(name = "id")
	@javax.persistence.Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "activeRound")
	private int activeRound;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "language", updatable = false)
	private Language language;

	@Column(name = "section", updatable = false)
	private TourneySection section;

	@JoinColumn(name = "tourneyId")
	@OneToOne(fetch = FetchType.LAZY)
	private HibernateTourney tourney;

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
	private HibernateTourneyDivision() {
	}

	public HibernateTourneyDivision(HibernateTourney tourney, Language language, TourneySection section) {
		this.tourney = tourney;
		this.language = language;
		this.section = section;
		this.activeRound = 1;
		lastChange = startedDate = new Date();
	}


	long getDbId() {
		return id;
	}

	@Override
	public int getActiveRound() {
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
	public RegularTourney getTourney() {
		return tourney;
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

	void nextRoundStarted() {
		if (finishedDate != null) {
			throw new IllegalStateException("Division already finished");
		}
		activeRound += 1;
		lastChange = new Date();
	}

	void divisionFinished() {
		if (finishedDate != null) {
			throw new IllegalStateException("Division already finished");
		}
		activeRound = 0;
		lastChange = finishedDate = new Date();
	}

	@Override
	public String toString() {
		return "HibernateTourneyDivision{" +
				"id=" + id +
				", round=" + activeRound +
				", tourney=" + tourney +
				", section=" + section +
				", language=" + language +
				", startedDate=" + startedDate +
				", finishedDate=" + finishedDate +
				", lastChange=" + lastChange +
				'}';
	}
}
