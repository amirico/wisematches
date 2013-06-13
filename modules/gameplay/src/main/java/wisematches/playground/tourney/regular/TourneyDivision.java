package wisematches.playground.tourney.regular;

import wisematches.core.Language;
import wisematches.playground.tourney.TourneyEntity;
import wisematches.playground.tourney.TourneyPlace;
import wisematches.playground.tourney.TourneyWinner;

import java.util.Collection;
import java.util.EnumSet;

/**
 * {@code TournamentDivision} is main block of tourney. Each tourney division has
 * language and section.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TourneyDivision extends RegularTourneyEntity<TourneyDivision, TourneyDivision.Id, TourneyDivision.Context> {
	/**
	 * Returns tourney for this division.
	 *
	 * @return the tourney for this division.
	 */
	Tourney getTourney();

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

	/**
	 * Returns active round for this division starting with one.
	 *
	 * @return the active round or {@code null} if there is no active rounds or division is finished.
	 */
	TourneyRound getActiveRound();

	/**
	 * Returns number of all current rounds in the division. Please note that this number can grow in time depends
	 * how many rounds was finished.
	 *
	 * @return number of all current rounds in the division
	 */
	int getRoundsCount();

	/**
	 * Returns place of the player in the division. Returns null if player doesn't play in the tourney or
	 * division is not finished.
	 *
	 * @param pid the player id to be checked
	 * @return place of the player in the division or {@code null}
	 */
	TourneyPlace getTourneyPlace(long pid);

	/**
	 * Returns collection of all winners for this division.
	 * <p/>
	 * Please note that you mustn't use this method to check is division finished or not. It's better
	 * to check is division finished or not before call this method.
	 * <p/>
	 * Please note it's possible that for one place more than one winner. You can filter
	 * winners by place using {@link wisematches.playground.tourney.TourneyPlace#filter(java.util.Collection)} method.
	 *
	 * @return the collection of winners.
	 * @see wisematches.playground.tourney.TourneyPlace
	 */
	Collection<TourneyWinner> getTourneyWinners();

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

			return language == id.language && section == id.section && tourneyId.equals(id.tourneyId);
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

		public Context(EnumSet<State> states) {
			this(null, null, null, states);
		}

		public Context(Language language, EnumSet<State> states) {
			this(null, language, null, states);
		}

		public Context(TourneySection section, EnumSet<State> states) {
			this(null, null, section, states);
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