package wisematches.playground.tracking.impl;

import wisematches.core.Personality;
import wisematches.core.Player;
import wisematches.core.Robot;
import wisematches.core.RobotType;
import wisematches.playground.*;
import wisematches.playground.tourney.TourneyPlace;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class StatisticsTrapper<E extends StatisticsEditor> {
	protected StatisticsTrapper() {
	}

	public void trapGameStarted(Player player, E editor) {
		editor.setActiveGames(editor.getActiveGames() + 1);
	}

	public void trapGameFinished(Player player, E editor, GameBoard<? extends GameSettings, ? extends GamePlayerHand, ? extends GameMove> board) {
		editor.setActiveGames(editor.getActiveGames() - 1);
		editor.setFinishedGames(editor.getFinishedGames() + 1);

		// Update average moves per game
		int movesCount = 0;
		final List<? extends GameMove> list = board.getGameMoves();
		for (GameMove gameMove : list) {
			if (gameMove.getPlayer().equals(player)) {
				movesCount++;
			}
		}
		final int gamesCount = editor.getFinishedGames();
		editor.setAverageMovesPerGame(average(editor.getAverageMovesPerGame(), movesCount, gamesCount));

		final Personality playerTurn = board.getPlayerTurn();
		final GameResolution resolution = board.getResolution();
		if (resolution == GameResolution.INTERRUPTED && playerTurn.getId().equals(player.getId())) {
			editor.setTimeouts(editor.getTimeouts() + 1);
		} else if (resolution == GameResolution.RESIGNED && playerTurn.getId().equals(player.getId())) {
			editor.setResigned(editor.getResigned() + 1);
		} else if (resolution == GameResolution.STALEMATE) {
			editor.setStalemates(editor.getStalemates() + 1);
		}

		if (!board.isRated()) {
			return;
		}

		final List<Personality> players = board.getPlayers();
		final Collection<Personality> wonPlayers = board.getWonPlayers();
		if (wonPlayers.isEmpty()) { // draw
			editor.setDraws(editor.getDraws() + 1);
		} else {
			boolean winner = false;
			for (Personality personality : wonPlayers) {
				if (personality.getId().equals(player.getId())) {
					winner = true;
					break;
				}
			}

			if (winner) {
				editor.setWins(editor.getWins() + 1);

				final Set<RobotType> types = new HashSet<>(3);
				for (Personality hand : players) {
					if (hand instanceof Robot) {
						Robot robot = (Robot) hand;
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

		final GamePlayerHand currentHand = board.getPlayerHand(player);

		editor.setRating(currentHand.getNewRating());

		int opponentsRatings = 0;

		Personality maxOpponent = null;
		GamePlayerHand maxOpponentHand = null;

		Personality minOpponent = null;
		GamePlayerHand minOpponentHand = null;

		for (Personality p : players) {
			if (!p.equals(player)) { // Exclude current player
				final GamePlayerHand h = board.getPlayerHand(p);
				final int oppRating = h.getOldRating();
				if (h.getPoints() < currentHand.getPoints() && //you won
						(maxOpponentHand == null || oppRating > maxOpponentHand.getOldRating())) {
					maxOpponent = p;
					maxOpponentHand = h;
				}
				if (h.getPoints() > currentHand.getPoints() && //you lose
						(minOpponentHand == null || oppRating < minOpponentHand.getOldRating())) {
					minOpponent = p;
					minOpponentHand = h;
				}
				opponentsRatings += h.getOldRating();
			}
		}


		final short rating = currentHand.getNewRating();
		final int averageOpponentsRating = opponentsRatings / (players.size() - 1);
		editor.setAverageOpponentRating(average(editor.getAverageOpponentRating(), averageOpponentsRating, gamesCount));
		editor.setAverageRating(average(editor.getAverageRating(), rating, gamesCount));

		if (maxOpponent != null && editor.getHighestWonOpponentRating() < maxOpponentHand.getOldRating()) {
			editor.setHighestWonOpponentId(maxOpponent.getId());
			editor.setHighestWonOpponentRating(maxOpponentHand.getOldRating());
		}

		if (minOpponent != null &&
				(editor.getLowestLostOpponentRating() == 0 || editor.getLowestLostOpponentRating() > minOpponentHand.getOldRating())) {
			editor.setLowestLostOpponentId(minOpponent.getId());
			editor.setLowestLostOpponentRating(minOpponentHand.getOldRating());
		}

		if (editor.getLowestRating() == 0) {
			if (rating < currentHand.getOldRating()) {
				editor.setLowestRating(rating);
			} else {
				editor.setLowestRating(currentHand.getOldRating());
			}
		} else if (rating < editor.getLowestRating()) {
			editor.setLowestRating(rating);
		}

		if (editor.getHighestRating() == 0) {
			if (rating < currentHand.getOldRating()) {
				editor.setHighestRating(currentHand.getOldRating());
			} else {
				editor.setHighestRating(rating);
			}
		} else if (rating > editor.getHighestRating()) {
			editor.setHighestRating(rating);
		}
	}

	public void trapGameMoveDone(Player player, E editor, GameBoard<? extends GameSettings, ? extends GamePlayerHand, ? extends GameMove> board, GameMove move, GameMoveScore score) {
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
	}

	public void trapTourneyFinished(Player player, E editor, TourneyPlace place) {
		editor.setTourneyWins(place, editor.getTourneyWins(place) + 1);
	}

	protected final Date previousMoveTime(GameBoard<? extends GameSettings, ? extends GamePlayerHand, ? extends GameMove> board, GameMove move) {
		final List<? extends GameMove> list = board.getGameMoves();
		final int i = list.lastIndexOf(move);
		if (i < 1) {
			return board.getStartedDate();
		}
		return list.get(i - 1).getMoveTime();
	}

	protected final float average(final float previousAverage, final int newValue, final int newCount) {
		return (previousAverage * (newCount - 1f) + newValue) / newCount;
	}
}
