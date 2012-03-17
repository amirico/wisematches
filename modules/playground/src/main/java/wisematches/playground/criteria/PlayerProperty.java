package wisematches.playground.criteria;

import wisematches.personality.player.Player;
import wisematches.playground.tracking.Statistics;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
interface PlayerProperty {
	PlayerProperty ID = new PlayerProperty() {
		@Override
		public Comparable getProperty(Player player, Statistics statistics) {
			return player.getId();
		}
	};

	PlayerProperty RATING = new PlayerProperty() {
		@Override
		public Comparable getProperty(Player player, Statistics statistics) {
			return statistics.getRating();
		}
	};
	PlayerProperty RATING_AVERAGE = new PlayerProperty() {
		@Override
		public Comparable getProperty(Player player, Statistics statistics) {
			return statistics.getAverageRating();
		}
	};
	PlayerProperty RATING_MIN = new PlayerProperty() {
		@Override
		public Comparable getProperty(Player player, Statistics statistics) {
			return statistics.getLowestRating();
		}
	};
	PlayerProperty RATING_MAX = new PlayerProperty() {
		@Override
		public Comparable getProperty(Player player, Statistics statistics) {
			return statistics.getHighestRating();
		}
	};

	PlayerProperty COMPLETED_GAMES = new PlayerProperty() {
		@Override
		public Comparable getProperty(Player player, Statistics statistics) {
			return statistics.getFinishedGames() - statistics.getTimeouts();
		}
	};

	Comparable getProperty(Player player, Statistics statistics);
}
