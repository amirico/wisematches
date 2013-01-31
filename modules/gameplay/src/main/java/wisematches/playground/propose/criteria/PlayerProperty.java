package wisematches.playground.propose.criteria;

import wisematches.core.Personality;
import wisematches.playground.tracking.Statistics;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum PlayerProperty {
	RATING() {
		@Override
		public Comparable getProperty(Personality player, Statistics statistics) {
			return statistics.getRating();
		}
	},
	RATING_AVERAGE() {
		@Override
		public Comparable getProperty(Personality player, Statistics statistics) {
			return statistics.getAverageRating();
		}
	},
	RATING_MIN() {
		@Override
		public Comparable getProperty(Personality player, Statistics statistics) {
			return statistics.getLowestRating();
		}
	},
	RATING_MAX() {
		@Override
		public Comparable getProperty(Personality player, Statistics statistics) {
			return statistics.getHighestRating();
		}
	},
	COMPLETED_GAMES() {
		@Override
		public Comparable getProperty(Personality player, Statistics statistics) {
			return statistics.getFinishedGames() - statistics.getUnratedGames();
		}
	},
	TIMEOUTS() {
		@Override
		public Comparable getProperty(Personality player, Statistics statistics) {
			return (int) (statistics.getTimeouts() * 100f / statistics.getFinishedGames());
		}
	};

	abstract Comparable getProperty(Personality player, Statistics statistics);
}
