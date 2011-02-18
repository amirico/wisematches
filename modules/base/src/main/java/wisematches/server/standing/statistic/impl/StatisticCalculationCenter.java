package wisematches.server.standing.statistic.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import wisematches.server.gameplaying.board.*;
import wisematches.server.gameplaying.room.*;
import wisematches.server.standing.statistic.PlayerRatingInfo;
import wisematches.server.standing.statistic.PlayerStatistic;
import wisematches.server.standing.statistic.PlayerStatisticsManager;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class StatisticCalculationCenter {
	private RoomsManager roomsManager;
	private PlayerStatisticsManager playerStatisticsManager;
	private PlatformTransactionManager transactionManager;

	private final TheBoardListener boardListener = new TheBoardListener();

	private final RoomBoardsListener roomBoardsListener = new TheRoomBoardsListener();
	private static final Log log = LogFactory.getLog(StatisticCalculationCenter.class);

	protected void processGameStarted(GameBoard board) {
		@SuppressWarnings("unchecked")
		final List<GamePlayerHand> hands = board.getPlayersHands();
		for (GamePlayerHand hand : hands) {
			final long playerId = hand.getPlayerId();
			playerStatisticsManager.lockPlayerStatistic(playerId);
			try {
				final PlayerStatistic statistic = getPlayerStatistic(playerId);
				statistic.setActiveGames(statistic.getActiveGames() + 1);

				if (log.isDebugEnabled()) {
					log.debug("Increase active games for player " + playerId + " to " + statistic.getActiveGames());
				}
				playerStatisticsManager.updatePlayerStatistic(statistic);
			} catch (Throwable th) {
				log.error("Statistic can't be updated by system error", th);
			} finally {
				playerStatisticsManager.unlockPlayerStatistic(playerId);
			}
		}
	}

	protected void processGameFinished(GameBoard board, GamePlayerHand wonPlayer) {
		final boolean ratedGame = board.isRatedGame();
		@SuppressWarnings("unchecked")
		final List<GamePlayerHand> hands = board.getPlayersHands();
		for (GamePlayerHand hand : hands) {
			final long playerId = hand.getPlayerId();

			playerStatisticsManager.lockPlayerStatistic(playerId);
			try {
				final PlayerStatistic statistic = getPlayerStatistic(playerId);
				statistic.setActiveGames(statistic.getActiveGames() - 1);
				if (log.isDebugEnabled()) {
					log.debug("Decrease active games for player " + playerId + " to " + statistic.getActiveGames());
				}

				if (ratedGame) { // If game is not rated just ignore it
					if (hand == wonPlayer) {
						statistic.setWonGames(statistic.getWonGames() + 1);
						if (log.isDebugEnabled()) {
							log.debug("Increase won games for player " + playerId + " to " + statistic.getWonGames());
						}
					} else {
						statistic.setLostGames(statistic.getLostGames() + 1);
						if (log.isDebugEnabled()) {
							log.debug("Increase lost games for player " + playerId + " to " + statistic.getLostGames());
						}
					}
					updateRatingsInfo(board, hand, statistic);
				}
				playerStatisticsManager.updatePlayerStatistic(statistic);
			} finally {
				playerStatisticsManager.unlockPlayerStatistic(playerId);
			}
		}
	}

	protected void processGameDraw(GameBoard board) {
		final boolean ratedGame = board.isRatedGame();

		@SuppressWarnings("unchecked")
		final List<GamePlayerHand> hands = board.getPlayersHands();
		for (GamePlayerHand hand : hands) {
			final long playerId = hand.getPlayerId();

			playerStatisticsManager.lockPlayerStatistic(playerId);
			try {
				final PlayerStatistic statistic = getPlayerStatistic(playerId);
				statistic.setActiveGames(statistic.getActiveGames() - 1);
				if (log.isDebugEnabled()) {
					log.debug("Decrease active games for player " + playerId + " to " + statistic.getActiveGames());
				}

				if (ratedGame) {
					statistic.setDrawGames(statistic.getDrawGames() + 1);
					if (log.isDebugEnabled()) {
						log.debug("Increate draw games for player " + playerId + " to " + statistic.getDrawGames());
					}
					updateRatingsInfo(board, hand, statistic);
				}
				playerStatisticsManager.updatePlayerStatistic(statistic);
			} finally {
				playerStatisticsManager.unlockPlayerStatistic(playerId);
			}
		}
	}

	protected void processGameInterrupted(GameBoard board, GamePlayerHand interrupterPlayer, boolean byTimeout) {
		if (board.isRatedGame() && byTimeout) {
			final long playerId = interrupterPlayer.getPlayerId();
			playerStatisticsManager.lockPlayerStatistic(playerId);
			try {
				final PlayerStatistic statistic = getPlayerStatistic(playerId);
				statistic.setTimeouts(statistic.getTimeouts() + 1);
				if (log.isDebugEnabled()) {
					log.debug("Increate by timeouts games for player " + playerId + " to " + statistic.getTimeouts());
				}
			} finally {
				playerStatisticsManager.unlockPlayerStatistic(playerId);
			}
		}

		final GamePlayerHand wonPlayer = board.getWonPlayer();
		if (wonPlayer == null) {
			processGameDraw(board);
		} else {
			processGameFinished(board, wonPlayer);
		}
	}

	protected void processPlayerMoved(GameBoard board, GamePlayerHand movedPlayer, GameMove move) {
		if (!board.isRatedGame()) { //If game is not rated just ignore it
			return;
		}

		final long playerId = movedPlayer.getPlayerId();
		playerStatisticsManager.lockPlayerStatistic(playerId);
		try {
			final PlayerStatistic statistic = getPlayerStatistic(playerId);
			updateTurnsStatistic(statistic, getPreviousMoveTime(board), move.getMoveTime());
			playerStatisticsManager.updatePlayerStatistic(statistic);
		} finally {
			playerStatisticsManager.unlockPlayerStatistic(playerId);
		}
	}

	protected void updateRatingsInfo(GameBoard board, GamePlayerHand hand, PlayerStatistic statistic) {
		updateRatingInfo(statistic, statistic.getAllGamesRatingInfo(), board, hand);
		updateRatingInfo(statistic, statistic.getNinetyDaysRatingInfo(), board, hand);
		updateRatingInfo(statistic, statistic.getYearRatingInfo(), board, hand);
		updateRatingInfo(statistic, statistic.getThirtyDaysRatingInfo(), board, hand);
	}

	protected void updateRatingInfo(PlayerStatistic statistic, PlayerRatingInfo ri, GameBoard board, GamePlayerHand hand) {
		@SuppressWarnings("unchecked")
		final List<GamePlayerHand> hands = board.getPlayersHands();
		final int rating = hand.getRating();

		// Update average moves per game
		int movesCount = 0;
		@SuppressWarnings("unchecked")
		final List<GameMove> list = board.getGameMoves();
		for (GameMove gameMove : list) {
			if (gameMove.getPlayerMove().getPlayerId() == hand.getPlayerId()) {
				movesCount++;
			}
		}
		final int gamesCount = statistic.getFinishedGames();
		ri.setAverageMovesPerGame(average(ri.getAverageMovesPerGame(), movesCount, gamesCount));

		int opponentsRaitings = 0;
		GamePlayerHand maxOpponentHand = null;
		GamePlayerHand minOpponentHand = null;
		for (GamePlayerHand opponent : hands) {
			if (opponent == hand) { // Exclude current player
				continue;
			}

			final int oppRating = opponent.getPreviousRating();
			if (opponent.getPoints() < hand.getPoints() && //you won
					(maxOpponentHand == null || oppRating > maxOpponentHand.getPreviousRating())) {
				maxOpponentHand = opponent;
			}
			if (opponent.getPoints() > hand.getPoints() && //you lose
					(minOpponentHand == null || oppRating < minOpponentHand.getPreviousRating())) {
				minOpponentHand = opponent;
			}
			opponentsRaitings += opponent.getPreviousRating();
		}

		final int averageOpponentsRating = opponentsRaitings / (hands.size() - 1);
		ri.setAverageOpponentRating(average(ri.getAverageOpponentRating(), averageOpponentsRating, gamesCount));
		ri.setAverageRating(average(ri.getAverageRating(), rating, gamesCount));

		if (maxOpponentHand != null && ri.getHighestWonOpponentRating() < maxOpponentHand.getPreviousRating()) {
			ri.setHighestWonOpponentRating(maxOpponentHand.getPreviousRating());
			ri.setHighestWonOpponentId(maxOpponentHand.getPlayerId());
		}

		if (minOpponentHand != null &&
				(ri.getLowestLostOpponentRating() == 0 || ri.getLowestLostOpponentRating() > minOpponentHand.getPreviousRating())) {
			ri.setLowestLostOpponentRating(minOpponentHand.getPreviousRating());
			ri.setLowestLostOpponentId(minOpponentHand.getPlayerId());
		}

		if (ri.getLowestRating() == 0) {
			if (rating < hand.getPreviousRating()) {
				ri.setLowestRating(rating);
			} else {
				ri.setLowestRating(hand.getPreviousRating());
			}
		} else if (rating < ri.getLowestRating()) {
			ri.setLowestRating(rating);
		}

		if (ri.getHighestRating() == 0) {
			if (rating < hand.getPreviousRating()) {
				ri.setHighestRating(hand.getPreviousRating());
			} else {
				ri.setHighestRating(rating);
			}
		} else if (rating > ri.getHighestRating()) {
			ri.setHighestRating(rating);
		}
	}

	/**
	 * Updates player's turn statistic. This method updated {@code averageTurnTime}, {@code turnsCount} and
	 * {@code lastMoveTime} properties.
	 *
	 * @param statistic		the statistic that should be updated.
	 * @param previousMoveTime the time of previous move
	 * @param currentMoveTime  the current move time.
	 */
	protected void updateTurnsStatistic(PlayerStatistic statistic, Date previousMoveTime, Date currentMoveTime) {
		statistic.setTurnsCount(statistic.getTurnsCount() + 1);
		if (log.isDebugEnabled()) {
			log.debug("Increase turns count for player " + statistic.getPlayerId() + " to " + statistic.getTurnsCount());
		}

		if (previousMoveTime == null) {
			return;
		}

		final int turnsCount = statistic.getTurnsCount();
		final int turnTime = (int) (currentMoveTime.getTime() - previousMoveTime.getTime());
		if (log.isDebugEnabled()) {
			log.debug("Turn move time for " + statistic.getPlayerId() + ": " + turnTime);
		}

		final int averageTurnTime = statistic.getAverageTurnTime();
		statistic.setAverageTurnTime(average(averageTurnTime, turnTime, turnsCount));

		if (log.isDebugEnabled()) {
			log.debug("Update averageTurnTime for player " + statistic.getPlayerId() + " from " + averageTurnTime + " to " +
					statistic.getAverageTurnTime() + ". Last move time - " + previousMoveTime +
					", current move time - " + currentMoveTime + ", turns count - " + turnsCount);
		}

		statistic.setLastMoveTime(currentMoveTime);

		if (log.isDebugEnabled()) {
			log.debug("Update last move time for player " + statistic.getPlayerId() + " to " + statistic.getLastMoveTime());
		}
	}

	/**
	 * Returns time of move that was maden before current move of time when game was started if no one previous
	 * move.
	 *
	 * @param board the board to get move.
	 * @return the previous move time for specified player or start time of player has made no one
	 */
	protected Date getPreviousMoveTime(GameBoard board) {
		@SuppressWarnings("unchecked")
		final List<GameMove> list = board.getGameMoves();
		if (list.size() <= 1) {
			return board.getStartedTime();
		}
		return list.get(list.size() - 2).getMoveTime(); // previous move
	}

	/**
	 * Calculates average value by previous average value, new value and new counts it elements.
	 *
	 * @param previousAverage the previous average value.
	 * @param newValue		the value that should be added.
	 * @param newCount		the count of elements include new value.
	 * @return the average value.
	 */
	private int average(final int previousAverage, int newValue, int newCount) {
		return (previousAverage * (newCount - 1) + newValue) / newCount;
	}

	private PlayerStatistic getPlayerStatistic(long playerId) {
		return playerStatisticsManager.getPlayerStatistic(playerId);
	}


	public void setRoomsManager(RoomsManager roomsManager) {
		if (this.roomsManager != null) {
			final Collection<RoomManager> managers = this.roomsManager.getRoomManagers();
			for (RoomManager manager : managers) {
				manager.removeRoomBoardsListener(roomBoardsListener);
			}
		}

		this.roomsManager = roomsManager;

		if (roomsManager != null) {
			final Collection<RoomManager> roomManagerCollection = roomsManager.getRoomManagers();
			for (RoomManager roomManager : roomManagerCollection) {
				roomManager.addRoomBoardsListener(roomBoardsListener);

				@SuppressWarnings("unchecked")
				final Collection<GameBoard> openedBoards = roomManager.getOpenedBoards();
				for (GameBoard openedBoard : openedBoards) {
					openedBoard.addGameStateListener(boardListener);
					openedBoard.addGameMoveListener(boardListener);
				}
			}
		}
	}

	public void setPlayerStatisticsManager(PlayerStatisticsManager managerPlayer) {
		this.playerStatisticsManager = managerPlayer;
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}


	private class TheRoomBoardsListener implements RoomBoardsListener {
		public void boardOpened(Room room, long boardId) {
			final RoomManager roomManager = roomsManager.getRoomManager(room);
			try {
				final GameBoard board = roomManager.openBoard(boardId);
				board.addGameStateListener(boardListener);
				board.addGameMoveListener(boardListener);
			} catch (BoardLoadingException ex) {
				log.error("Board can't be loaded in boardOpened event processing", ex);
			}
		}

		public void boardClosed(Room room, long boardId) {
		}
	}

	private class TheBoardListener implements GameStateListener, GameMoveListener {
		public void gameStarted(GameBoard board, GamePlayerHand playerTurn) {
			final TransactionStatus status = newTrasaction();
			try {
				processGameStarted(board);
				transactionManager.commit(status);
			} catch (Throwable th) {
				log.error("Statistic can't be updated", th);
				transactionManager.rollback(status);
			}
		}

		public void gameFinished(GameBoard board, GamePlayerHand wonPlayer) {
			final TransactionStatus status = newTrasaction();
			try {
				processGameFinished(board, wonPlayer);
				transactionManager.commit(status);
			} catch (Throwable th) {
				log.error("Statistic can't be updated", th);
				transactionManager.rollback(status);
			}
		}

		public void gameDraw(GameBoard board) {
			final TransactionStatus status = newTrasaction();
			try {
				processGameDraw(board);
				transactionManager.commit(status);
			} catch (Throwable th) {
				log.error("Statistic can't be updated", th);
				transactionManager.rollback(status);
			}
		}

		public void gameInterrupted(GameBoard board, GamePlayerHand interrupterPlayer, boolean byTimeout) {
			final TransactionStatus status = newTrasaction();
			try {
				processGameInterrupted(board, interrupterPlayer, byTimeout);
				transactionManager.commit(status);
			} catch (Throwable th) {
				log.error("Statistic can't be updated", th);
				transactionManager.rollback(status);
			}
		}

		public void playerMoved(GameMoveEvent event) {
			final TransactionStatus status = newTrasaction();
			try {
				final GameMove gameMove = event.getGameMove();
				processPlayerMoved(event.getGameBoard(), event.getPlayer(), gameMove);
				transactionManager.commit(status);
			} catch (Throwable th) {
				log.error("Statistic can't be updated", th);
				transactionManager.rollback(status);
			}
		}

		private TransactionStatus newTrasaction() {
			return transactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
		}
	}
}
