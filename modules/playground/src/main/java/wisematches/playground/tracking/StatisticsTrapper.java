package wisematches.playground.tracking;

import wisematches.personality.Personality;
import wisematches.playground.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class StatisticsTrapper<T extends StatisticsEditor> {
	private final Class<T> statisticType;

	protected StatisticsTrapper(Class<T> statisticType) {
		this.statisticType = statisticType;
	}

	public final Class<T> getStatisticType() {
		return statisticType;
	}

	public abstract T createStatisticsEditor(Personality person);


	public void trapGameStarted(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, T editor) {
		editor.setActiveGames(editor.getActiveGames() + 1);
	}

	public void trapGameMoveDone(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameMove move, T editor) {
		final int turnsCount = editor.getTurnsCount() + 1;

		editor.setTurnsCount(turnsCount);
		editor.setLastMoveTime(move.getMoveTime());

		final int points = move.getPoints();
		if (turnsCount == 1 || points > editor.getHighestPoints()) {
			editor.setHighestPoints(points);
		}
		if (turnsCount == 1 || points < editor.getLowestPoints()) {
			editor.setLowestPoints(points);
		}
		editor.setAveragePoints(average(editor.getAveragePoints(), points, turnsCount));

		final int moveTime = (int) (move.getMoveTime().getTime() - previousMoveTime(board).getTime());
		editor.setAverageMoveTime(average(editor.getAverageMoveTime(), moveTime, turnsCount));

		final PlayerMove playerMove = move.getPlayerMove();
		if (playerMove instanceof PassTurnMove) {
			editor.setPassesCount(editor.getPassesCount() + 1);
		}
	}

	public void trapGameFinished(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, RatingChanges changes, T editor) {
		editor.setActiveGames(editor.getActiveGames() - 1);
		editor.setFinishedGames(editor.getFinishedGames() + 1);


		// Update average moves per game
		int movesCount = 0;
		final List<GameMove> list = board.getGameMoves();
		for (GameMove gameMove : list) {
			if (gameMove.getPlayerMove().getPlayerId() == editor.getPlayerId()) {
				movesCount++;
			}
		}
		final int gamesCount = editor.getFinishedGames();
		editor.setAverageMovesPerGame(average(editor.getAverageMovesPerGame(), movesCount, gamesCount));

		if (!board.isRatedGame()) {
			editor.setUnratedGames(editor.getUnratedGames() + 1);
			return;
		}
		if (board.getGameResolution() == GameResolution.TIMEOUT) {
			editor.setTimeouts(editor.getTimeouts() + 1);
		}

		final Collection<? extends GamePlayerHand> wonPlayers = board.getWonPlayers();
		if (wonPlayers.isEmpty()) { // draw
			editor.setDraws(editor.getDraws() + 1);
		} else {
			boolean winner = false;
			for (GamePlayerHand hand : wonPlayers) {
				if (hand.getPlayerId() == editor.getPlayerId()) {
					winner = true;
					break;
				}
			}

			if (winner) {
				editor.setWins(editor.getWins() + 1);
			} else {
				editor.setLoses(editor.getLoses() + 1);
			}
		}

		final RatingChange currentRating = changes.getRatingChange(editor.getPlayerId());

		editor.setRating(currentRating.getNewRating());

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


		final short rating = currentRating.getNewRating();
		final short averageOpponentsRating = (short) (opponentsRatings / (board.getPlayersHands().size() - 1));
		editor.setAverageOpponentRating((short) average(editor.getAverageOpponentRating(), averageOpponentsRating, gamesCount));
		editor.setAverageRating((short) average(editor.getAverageRating(), rating, gamesCount));

		if (maxOpponent != null && editor.getHighestWonOpponentRating() < maxOpponent.getOldRating()) {
			editor.setHighestWonOpponentRating(maxOpponent.getOldRating());
			editor.setHighestWonOpponentId(maxOpponent.getPlayerId());
		}

		if (minOpponent != null &&
				(editor.getLowestLostOpponentRating() == 0 || editor.getLowestLostOpponentRating() > minOpponent.getOldRating())) {
			editor.setLowestLostOpponentRating(minOpponent.getOldRating());
			editor.setLowestLostOpponentId(minOpponent.getPlayerId());
		}

		if (editor.getLowestRating() == 0) {
			if (rating < currentRating.getOldRating()) {
				editor.setLowestRating(rating);
			} else {
				editor.setLowestRating(currentRating.getOldRating());
			}
		} else if (rating < editor.getLowestRating()) {
			editor.setLowestRating(rating);
		}

		if (editor.getHighestRating() == 0) {
			if (rating < currentRating.getOldRating()) {
				editor.setHighestRating(currentRating.getOldRating());
			} else {
				editor.setHighestRating(rating);
			}
		} else if (rating > editor.getHighestRating()) {
			editor.setHighestRating(rating);
		}
	}

	protected final Date previousMoveTime(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board) {
		final List<GameMove> list = board.getGameMoves();
		if (list.size() <= 1) {
			return board.getStartedTime();
		}
		return list.get(list.size() - 2).getMoveTime(); // previous move
	}

	protected final int average(final int previousAverage, final int newValue, final int newCount) {
		return (previousAverage * (newCount - 1) + newValue) / newCount;
	}
}
