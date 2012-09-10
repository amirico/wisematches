package wisematches.playground.tourney.regular;

import wisematches.personality.Language;
import wisematches.playground.tourney.TourneyEntity;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface RegularTourneyGame extends RegularTourneyElement<RegularTourneyGame, RegularTourneyGame.Id, RegularTourneyGame.Context> {
	public final class Id implements TourneyEntity.Id<RegularTourneyGame, Id> {
	}

	public final class Context implements TourneyEntity.Context<RegularTourneyGame, Context> {
		private final int tournament;
		private final Language language;
		private final RegularTourneySection section;

		public Context(int tournament, Language language, RegularTourneySection section) {
			this.tournament = tournament;
			this.language = language;
			this.section = section;
		}

		public int getTournament() {
			return tournament;
		}

		public Language getLanguage() {
			return language;
		}

		public RegularTourneySection getSection() {
			return section;
		}
	}
}
