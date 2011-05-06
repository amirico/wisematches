package wisematches.server.standing.statistic.impl.statistician;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.server.playground.board.GameBoard;
import wisematches.server.playground.board.GameResolution;
import wisematches.server.standing.rating.PlayerRatingManager;
import wisematches.server.standing.rating.RatingChange;
import wisematches.server.standing.statistic.PlayerStatistic;
import wisematches.server.standing.statistic.statistician.RatingsStatisticEditor;
import wisematches.server.standing.statistic.statistician.RatingsStatistician;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultRatingsStatistician extends AbstractStatistician implements RatingsStatistician {
	private PlayerRatingManager ratingManager;

	private static final Log log = LogFactory.getLog("wisematches.server.statistic.rating");

	public DefaultRatingsStatistician() {
	}

	@Override
	public void updateRatingsStatistic(GameBoard board, GameResolution resolution, Collection wonPlayers, PlayerStatistic statistic, RatingsStatisticEditor editor) {
		final Collection<RatingChange> changes = ratingManager.getRatingChanges(board);

		RatingChange currentRating = null;
		for (RatingChange change : changes) {
			if (change.getPlayerId() == statistic.getPlayerId()) { // Exclude current player
				currentRating = change;
				break;
			}
		}
		if (currentRating == null) {
			log.warn("Player rating can't be found: " + statistic.getPlayerId() + " at board " + board.getBoardId());
			return;
		}

		int opponentsRatings = 0;
		RatingChange maxOpponent = null;
		RatingChange minOpponent = null;
		for (RatingChange change : changes) {
			if (change == currentRating) { // Exclude current player
				continue;
			}

			final int oppRating = change.getOldRating();
			if (change.getPoints() < currentRating.getPoints() && //you won
					(maxOpponent == null || oppRating > maxOpponent.getOldRating())) {
				maxOpponent = change;
			}
			if (change.getPoints() > currentRating.getPoints() && //you lose
					(minOpponent == null || oppRating < minOpponent.getOldRating())) {
				minOpponent = change;
			}
			opponentsRatings += change.getOldRating();
		}


		final short averageOpponentsRating;
		final short rating = currentRating.getNewRating();
		if (changes.size() < 2) {
			averageOpponentsRating = (short) opponentsRatings;
		} else {
			averageOpponentsRating = (short) (opponentsRatings / (changes.size() - 1));
		}
		final int gamesCount = statistic.getGamesStatistic().getFinished();
		editor.setAverageOpponentRating((short) average(editor.getAverageOpponentRating(), averageOpponentsRating, gamesCount));
		editor.setAverage((short) average(editor.getAverage(), rating, gamesCount));

		if (maxOpponent != null && editor.getHighestWonOpponentRating() < maxOpponent.getOldRating()) {
			editor.setHighestWonOpponentRating(maxOpponent.getOldRating());
			editor.setHighestWonOpponentId(maxOpponent.getPlayerId());
		}

		if (minOpponent != null &&
				(editor.getLowestLostOpponentRating() == 0 || editor.getLowestLostOpponentRating() > minOpponent.getOldRating())) {
			editor.setLowestLostOpponentRating(minOpponent.getOldRating());
			editor.setLowestLostOpponentId(minOpponent.getPlayerId());
		}

		if (editor.getLowest() == 0) {
			if (rating < currentRating.getOldRating()) {
				editor.setLowest(rating);
			} else {
				editor.setLowest(currentRating.getOldRating());
			}
		} else if (rating < editor.getLowest()) {
			editor.setLowest(rating);
		}

		if (editor.getHighest() == 0) {
			if (rating < currentRating.getOldRating()) {
				editor.setHighest(currentRating.getOldRating());
			} else {
				editor.setHighest(rating);
			}
		} else if (rating > editor.getHighest()) {
			editor.setHighest(rating);
		}
	}

	public void setRatingManager(PlayerRatingManager playerRatingManager) {
		this.ratingManager = playerRatingManager;
	}
}
