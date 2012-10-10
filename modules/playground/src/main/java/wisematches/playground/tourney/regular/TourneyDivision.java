package wisematches.playground.tourney.regular;

import wisematches.personality.Language;
import wisematches.playground.tourney.TourneyEntity;

import java.util.EnumSet;

/**
 * {@code TournamentDivision} is main block of tournament. Each tournament division has
 * language and section.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TourneyDivision extends RegularTourneyEntity<TourneyDivision, TourneyDivision.Id, TourneyDivision.Context> {
	/**
	 * Returns active round for this division starting with one.
	 *
	 * @return the active round or {@code zero} if division is finished.
	 */
	int getActiveRound();

	/**
	 * Returns division language
	 *
	 * @return the division language.
	 */
	Language getLanguage();

	/**
	 * Returns division section.
	 *
	 * @return the division section.
	 */
	TourneySection getSection();

	Tourney getTourney();


	public final class Id extends TourneyEntity.Id<TourneyDivision, Id> {
		private final Language language;
		private final TourneySection section;
		private final Tourney.Id tourneyId;

		public Id(int tourney, Language language, TourneySection section) {
			this(new Tourney.Id(tourney), language, section);
		}

		public Id(Tourney.Id tourneyId, Language language, TourneySection section) {
			this.tourneyId = tourneyId;
			this.language = language;
			this.section = section;
		}

		public Language getLanguage() {
			return language;
		}

		public TourneySection getSection() {
			return section;
		}

		public Tourney.Id getTourneyId() {
			return tourneyId;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Id id = (Id) o;

			if (language != id.language) return false;
			if (section != id.section) return false;
			if (!tourneyId.equals(id.tourneyId)) return false;

			return true;
		}

		@Override
		public int hashCode() {
			int result = tourneyId.hashCode();
			result = 31 * result + language.hashCode();
			result = 31 * result + section.hashCode();
			return result;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append("Id");
			sb.append("{tourneyId=").append(tourneyId);
			sb.append(", language=").append(language);
			sb.append(", section=").append(section);
			sb.append('}');
			return sb.toString();
		}
	}

	public final class Context extends TourneyEntity.Context<TourneyDivision, Context> {
		private final Tourney.Id tourney;
		private final Language language;
		private final TourneySection section;

		public Context(Tourney.Id tourney) {
			this(tourney, null, null, null);
		}

		public Context(Tourney.Id tourney, Language language) {
			this(tourney, language, null, null);
		}

		public Context(Tourney.Id tourney, TourneySection section) {
			this(tourney, null, section, null);
		}

		public Context(Tourney.Id tourney, EnumSet<State> states) {
			this(tourney, null, null, states);
		}

		public Context(Tourney.Id tourney, Language language, EnumSet<State> states) {
			this(tourney, language, null, states);
		}

		public Context(Tourney.Id tourney, TourneySection section, EnumSet<State> states) {
			this(tourney, null, section, states);
		}

		public Context(Tourney.Id tourney, Language language, TourneySection section, EnumSet<State> states) {
			super(states);
			this.tourney = tourney;
			this.language = language;
			this.section = section;
		}

		public Tourney.Id getTourneyId() {
			return tourney;
		}

		public Language getLanguage() {
			return language;
		}

		public TourneySection getSection() {
			return section;
		}
	}
}