package wisematches.playground.tourney.regular.impl;

import wisematches.personality.Language;
import wisematches.playground.tourney.regular.RegularTourney;
import wisematches.playground.tourney.regular.TourneyDivision;
import wisematches.playground.tourney.regular.TourneySection;

import javax.persistence.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "tourney_regular_division")
public class HibernateTourneyDivision extends HibernateTourneyEntity implements TourneyDivision {
	@Column(name = "id")
	@javax.persistence.Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "round")
	@Enumerated(EnumType.ORDINAL)
	private int activeRound;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "language", updatable = false)
	private Language language;

	@Column(name = "section", updatable = false)
	private TourneySection section;

	@JoinColumn(name = "tourney")
	@OneToOne(fetch = FetchType.LAZY)
	private HibernateTourney tourney;

	@Deprecated
	private HibernateTourneyDivision() {
	}

	public HibernateTourneyDivision(HibernateTourney tourney, Language language, TourneySection section) {
		this.tourney = tourney;
		this.language = language;
		this.section = section;
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

	void nextRoundStarted() {
		activeRound = activeRound + 1;
	}


	@Override
	public String toString() {
		return "HibernateTourneyDivision{" +
				"activeRound=" + activeRound +
				", language=" + language +
				", section=" + section +
				", tourney=" + tourney +
				'}';
	}
}
