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
@Table(name = "regular_tourney_division")
public class HibernateTourneyDivision extends HibernateRegularTourneyEntity implements TourneyDivision {
	@Column(name = "id")
	@javax.persistence.Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "round")
	private int activeRound;

	@Column(name = "language", updatable = false)
	private Language language;

	@Column(name = "section", updatable = false)
	private TourneySection section;

	@OneToOne
	@JoinColumn(name = "tourney")
	private HibernateRegularTourney regularTourney;

	@Deprecated
	private HibernateTourneyDivision() {
	}

	public HibernateTourneyDivision(HibernateRegularTourney regularTourney, Language language, TourneySection section) {
		this.regularTourney = regularTourney;
		this.language = language;
		this.section = section;
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
	public RegularTourney getRegularTourney() {
		return regularTourney;
	}

	@Override
	public Id getId() {
		return new Id(regularTourney.getId(), language, section);
	}

	void nextRoundStarted() {
		activeRound = activeRound + 1;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("HibernateTourneyDivision");
		sb.append("{id=").append(id);
		sb.append(", activeRound=").append(activeRound);
		sb.append(", language=").append(language);
		sb.append(", section=").append(section);
		sb.append(", regularTourney=").append(regularTourney);
		sb.append('}');
		return sb.toString();
	}
}
