package wisematches.playground.tracking;

import wisematches.core.Personality;
import wisematches.core.personality.proprietary.robot.RobotPlayer;
import wisematches.core.personality.proprietary.robot.RobotType;
import wisematches.playground.*;
import wisematches.playground.tourney.TourneyPlace;

import java.util.*;

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


	public void trapGameStarted(T editor) {
		editor.setActiveGames(editor.getActiveGames() + 1);
	}

	public void trapGameMoveDone(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameMove move, GameMoveScore moveScore, T editor) {
		final int turnsCount = editor.getTurnsCount() + 1;

		editor.setTurnsCount(turnsCount);
		editor.setLastMoveTime(move.getMoveTime());

		final int points = move.getPoints();
		if ((editor.getLowestPoints() == 0 || points < editor.getLowestPoints()) && points != 0) {
			editor.setLowestPoints(points);
		}
		if (editor.getHighestPoints() == 0 || points > editor.getHighestPoints()) {
			editor.setHighestPoints(points);
		}
		editor.setAveragePoints(average(editor.getAveragePoints(), points, turnsCount));

		final int moveTime = (int) (move.getMoveTime().getTime() - previousMoveTime(board, move).getTime());
		editor.setAverageMoveTime(average(editor.getAverageMoveTime(), moveTime, turnsCount));

		final PlayerMove playerMove = move.getPlayerMove();
		if (playerMove instanceof PassTurnMove) {
			editor.setPassesCount(editor.getPassesCount() + 1);
		}
	}

	public void trapGameFinished(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, T editor) {
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

		final GamePlayerHand playerTurn = board.getPlayerTurn();
		final GameResolution resolution = board.getGameResolution();
		if (resolution == GameResolution.TIMEOUT && playerTurn.getPlayerId() == editor.getPlayerId()) {
			editor.setTimeouts(editor.getTimeouts() + 1);
		} else if (resolution == GameResolution.RESIGNED && playerTurn.getPlayerId() == editor.getPlayerId()) {
			editor.setResigned(editor.getResigned() + 1);
		} else if (resolution == GameResolution.STALEMATE) {
			editor.setStalemates(editor.getStalemates() + 1);
		}

		if (!board.isRatedGame()) {
			return;
		}

		final List<? extends GamePlayerHand> playersHands = board.getPlayersHands();
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

				final Set<RobotType> types = new HashSet<>(3);
				for (GamePlayerHand hand : playersHands) {
					final long pid = hand.getPlayerId();
					if (RobotPlayer.isRobotPlayer(pid)) {
						final RobotPlayer robot = RobotPlayer.getComputerPlayer(pid, RobotPlayer.class);
						types.add(robot.getRobotType());
					}
				}

				if (types.size() != 0) {
					for (RobotType type : types) {
						editor.setRobotWins(type, editor.getRobotWins(type) + 1);
					}
				}
			} else {
				editor.setLoses(editor.getLoses() + 1);
			}
		}

		final GamePlayerHand playerHand = board.getPlayerHand(editor.getPlayerId());
		final GameRatingChange currentRating = board.getRatingChange(playerHand);

		editor.setRating(currentRating.getNewRating());

		int opponentsRatings = 0;
		GameRatingChange maxOpponent = null;
		GameRatingChange minOpponent = null;
		for (GameRatingChange change : board.getRatingChanges()) {
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
		final int averageOpponentsRating = opponentsRatings / (playersHands.size() - 1);
		editor.setAverageOpponentRating(average(editor.getAverageOpponentRating(), averageOpponentsRating, gamesCount));
		editor.setAverageRating(average(editor.getAverageRating(), rating, gamesCount));

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

	public void trapTourneyFinished(TourneyPlace place, StatisticsEditor editor) {
		editor.setTourneyWins(place, editor.getTourneyWins(place) + 1);
	}

	protected final Date previousMoveTime(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameMove move) {
		final List<GameMove> list = board.getGameMoves();
		final int i = list.lastIndexOf(move);
		if (i < 1) {
			return board.getStartedTime();
		}
		return list.get(i - 1).getMoveTime();
	}

	protected final float average(final float previousAverage, final int newValue, final int newCount) {
		return (previousAverage * (newCount - 1f) + newValue) / newCount;
	}
}
