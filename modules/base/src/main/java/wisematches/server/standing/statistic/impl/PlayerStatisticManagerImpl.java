package wisematches.server.standing.statistic.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.server.playground.board.*;
import wisematches.server.playground.room.RoomManager;
import wisematches.server.playground.room.RoomsManager;
import wisematches.server.playground.board.BoardStateListener;
import wisematches.server.personality.Personality;
import wisematches.server.personality.account.Account;
import wisematches.server.personality.account.AccountListener;
import wisematches.server.personality.account.AccountManager;
import wisematches.server.personality.player.computer.ComputerPlayer;
import wisematches.server.standing.rating.PlayerRatingManager;
import wisematches.server.standing.rating.RatingChange;
import wisematches.server.standing.statistic.PlayerStatisticListener;
import wisematches.server.standing.statistic.PlayerStatisticManager;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PlayerStatisticManagerImpl implements PlayerStatisticManager {
	private final Lock lockLock = new ReentrantLock();
	private final Map<Personality, ReentrantLock> locksMap = new HashMap<Personality, ReentrantLock>();

	private RoomsManager roomsManager;
	private AccountManager accountManager;
	private PlayerRatingManager ratingManager;
	private PlayerStatisticDao playerStatisticDao;

	private final TheAccountListener accountListener = new TheAccountListener();
	private final TheBoardStateListener boardStateListener = new TheBoardStateListener();
	private final Collection<PlayerStatisticListener> listeners = new CopyOnWriteArraySet<PlayerStatisticListener>();

	private static final Log log = LogFactory.getLog(PlayerStatisticManagerImpl.class);

	public PlayerStatisticManagerImpl() {
	}

	public void addPlayerStatisticListener(PlayerStatisticListener l) {
		if (l != null) {
			listeners.add(l);
		}
	}

	public void removePlayerStatisticListener(PlayerStatisticListener l) {
		listeners.remove(l);
	}

	@Override
	public HibernatePlayerStatistic getPlayerStatistic(Personality personality) {
		lock(personality);
		try {
			return playerStatisticDao.loadPlayerStatistic(personality);
		} finally {
			unlock(personality);
		}
	}

	protected void processGameStarted(GameBoard board) {
		@SuppressWarnings("unchecked")
		final Collection<GamePlayerHand> hands = board.getPlayersHands();
		for (GamePlayerHand hand : hands) {
			if (isPlayerIgnored(hand)) {
				continue;
			}

			final Personality personality = Personality.person(hand.getPlayerId());
			lock(personality);
			try {
				final HibernatePlayerStatistic statistic = getPlayerStatistic(personality);
				statistic.incrementActiveGames();

				if (log.isDebugEnabled()) {
					log.debug("Increase active games for player " + personality + " to " + statistic.getActiveGames());
				}
				updatePlayerStatistic(personality, statistic);
			} catch (Throwable th) {
				log.error("Statistic can't be updated for player: " + personality, th);
			} finally {
				unlock(personality);
			}
		}
	}

	protected void processPlayerMoved(GameBoard board, GameMove move) {
		final long playerId = move.getPlayerMove().getPlayerId();
		if (isPlayerIgnored(playerId)) {
			return;
		}
		final Personality personality = Personality.person(playerId);
		lock(personality);
		try {
			final HibernatePlayerStatistic statistic = getPlayerStatistic(personality);
			final Date currentMoveTime = move.getMoveTime();
			final Date previousMoveTime = previousMoveTime(board);

			statistic.incrementTurnsCount();
			statistic.setAverageTurnTime(
					average(statistic.getAverageTurnTime(),
							(int) (currentMoveTime.getTime() - previousMoveTime.getTime()),
							statistic.getTurnsCount()
					)
			);
			statistic.setLastMoveTime(currentMoveTime);
			updatePlayerStatistic(personality, statistic);
		} finally {
			unlock(personality);
		}
	}

	protected <S extends GameSettings, P extends GamePlayerHand> void processGameFinished(GameBoard<S, P> board, GameResolution resolution, Collection<P> wonPlayers) {
		final boolean ratedGame = board.isRatedGame();
		final Collection<P> hands = board.getPlayersHands();
		final Collection<RatingChange> ratingChanges = ratingManager.getRatingChanges(board);
		for (P hand : hands) {
			if (isPlayerIgnored(hand)) {
				continue;
			}
			final Personality personality = Personality.person(hand.getPlayerId());
			lock(personality);
			try {
				final HibernatePlayerStatistic statistic = getPlayerStatistic(personality);
				statistic.decrementActiveGames();
				if (ratedGame) { // If game is not rated just ignore it
					if (resolution == GameResolution.TIMEOUT) {
						statistic.incrementTimeouts();
					}

					if (wonPlayers.isEmpty()) { // draw
						statistic.incrementDrawGames();
					} else {
						if (wonPlayers.contains(hand)) {
							statistic.incrementWonGames();
						} else {
							statistic.incrementLostGames();
						}
					}
					updateRatingsInfo(board, ratingChanges, statistic);
				}
				updatePlayerStatistic(personality, statistic);
			} finally {
				unlock(personality);
			}
		}
	}

	protected void updateRatingsInfo(GameBoard board, Collection<RatingChange> changes, HibernatePlayerStatistic statistic) {
		updateRatingInfo(board, statistic, statistic.getAllGamesStatisticRating(), changes);
/*
		updateRatingInfo(statistic, statistic.getNinetyDaysRatingInfo(), board, hand);
		updateRatingInfo(statistic, statistic.getYearRatingInfo(), board, hand);
		updateRatingInfo(statistic, statistic.getThirtyDaysRatingInfo(), board, hand);
*/
	}

	protected void updateRatingInfo(GameBoard board, HibernatePlayerStatistic ps, HibernatePlayerStatisticRating psr, Collection<RatingChange> changes) {
		// Update average moves per game
		int movesCount = 0;
		@SuppressWarnings("unchecked")
		final List<GameMove> list = board.getGameMoves();
		for (GameMove gameMove : list) {
			if (gameMove.getPlayerMove().getPlayerId() == ps.getPlayerId()) {
				movesCount++;
			}
		}
		final int gamesCount = ps.getFinishedGames();
		psr.setAverageMovesPerGame(average(psr.getAverageMovesPerGame(), movesCount, gamesCount));

		RatingChange currentRating = null;
		for (RatingChange change : changes) {
			if (change.getPlayerId() == ps.getPlayerId()) { // Exclude current player
				currentRating = change;
				break;
			}
		}
		if (currentRating == null) {
			log.warn("Player rating can't be found: " + ps.getPlayerId() + " at board " + board.getBoardId());
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
		psr.setAverageOpponentRating(average(psr.getAverageOpponentRating(), averageOpponentsRating, gamesCount));
		psr.setAverageRating(average(psr.getAverageRating(), rating, gamesCount));

		if (maxOpponent != null && psr.getHighestWonOpponentRating() < maxOpponent.getOldRating()) {
			psr.setHighestWonOpponentRating(maxOpponent.getOldRating());
			psr.setHighestWonOpponentId(maxOpponent.getPlayerId());
		}

		if (minOpponent != null &&
				(psr.getLowestLostOpponentRating() == 0 || psr.getLowestLostOpponentRating() > minOpponent.getOldRating())) {
			psr.setLowestLostOpponentRating(minOpponent.getOldRating());
			psr.setLowestLostOpponentId(minOpponent.getPlayerId());
		}

		if (psr.getLowestRating() == 0) {
			if (rating < currentRating.getOldRating()) {
				psr.setLowestRating(rating);
			} else {
				psr.setLowestRating(currentRating.getOldRating());
			}
		} else if (rating < psr.getLowestRating()) {
			psr.setLowestRating(rating);
		}

		if (psr.getHighestRating() == 0) {
			if (rating < currentRating.getOldRating()) {
				psr.setHighestRating(currentRating.getOldRating());
			} else {
				psr.setHighestRating(rating);
			}
		} else if (rating > psr.getHighestRating()) {
			psr.setHighestRating(rating);
		}
	}

	protected void lock(Personality personality) {
		ReentrantLock lock;
		lockLock.lock();
		try {
			lock = locksMap.get(personality);
			if (lock == null) {
				lock = new ReentrantLock();
				locksMap.put(personality, lock);
			}
		} finally {
			lockLock.unlock();
		}
		lock.lock();
	}

	protected void unlock(Personality personality) {
		lockLock.lock();
		try {
			final ReentrantLock lock = locksMap.get(personality);
			if (lock != null) {
				lock.unlock();
				if (!lock.isLocked()) {
					locksMap.remove(personality);
				}
			}
		} finally {
			lockLock.unlock();
		}
	}

	protected Date previousMoveTime(GameBoard board) {
		@SuppressWarnings("unchecked")
		final List<GameMove> list = board.getGameMoves();
		if (list.size() <= 1) {
			return board.getStartedTime();
		}
		return list.get(list.size() - 2).getMoveTime(); // previous move
	}

	protected int average(final int previousAverage, final int newValue, final int newCount) {
		return (previousAverage * (newCount - 1) + newValue) / newCount;
	}

	protected boolean isPlayerIgnored(final GamePlayerHand hand) {
		return isPlayerIgnored(hand.getPlayerId());
	}

	protected boolean isPlayerIgnored(final long playerId) {
		return ComputerPlayer.getComputerPlayer(playerId) != null;
	}

	protected void updatePlayerStatistic(Personality personality, HibernatePlayerStatistic statistic) {
		statistic.setUpdateTime(new Date());

		playerStatisticDao.savePlayerStatistic(statistic);

		for (PlayerStatisticListener listener : listeners) {
			listener.playerStatisticUpdated(personality, statistic);
		}
	}

	public void setRoomsManager(RoomsManager roomsManager) {
		if (this.roomsManager != null) {
			final Collection<RoomManager> managers = this.roomsManager.getRoomManagers();
			for (RoomManager manager : managers) {
				manager.getBoardManager().removeBoardStateListener(boardStateListener);
			}
		}

		this.roomsManager = roomsManager;

		if (this.roomsManager != null) {
			final Collection<RoomManager> managers = this.roomsManager.getRoomManagers();
			for (RoomManager manager : managers) {
				manager.getBoardManager().addBoardStateListener(boardStateListener);
			}
		}
	}

	public void setAccountManager(AccountManager accountManager) {
		if (this.accountManager != null) {
			this.accountManager.removeAccountListener(accountListener);
		}

		this.accountManager = accountManager;

		if (this.accountManager != null) {
			this.accountManager.addAccountListener(accountListener);
		}
	}

	public void setRatingManager(PlayerRatingManager ratingManager) {
		this.ratingManager = ratingManager;
	}

	public void setPlayerStatisticDao(PlayerStatisticDao playerStatisticDao) {
		this.playerStatisticDao = playerStatisticDao;
	}

	private class TheBoardStateListener implements BoardStateListener {
		private TheBoardStateListener() {
		}

		@Override
		public void gameStarted(GameBoard board) {
			processGameStarted(board);
		}

		@Override
		public void gameMoveDone(GameBoard board, GameMove move) {
			processPlayerMoved(board, move);
		}

		@Override
		public <S extends GameSettings, P extends GamePlayerHand> void gameFinished(GameBoard<S, P> board, GameResolution resolution, Collection<P> wonPlayers) {
			processGameFinished(board, resolution, wonPlayers);
		}
	}

	private class TheAccountListener implements AccountListener {
		private TheAccountListener() {
		}

		@Override
		public void accountCreated(Account account) {
			playerStatisticDao.savePlayerStatistic(new HibernatePlayerStatistic(account));
		}

		@Override
		public void accountRemove(Account account) {
			HibernatePlayerStatistic hibernatePlayerStatistic = playerStatisticDao.loadPlayerStatistic(account);
			if (hibernatePlayerStatistic != null) {
				playerStatisticDao.removePlayerStatistic(hibernatePlayerStatistic);
			}
		}

		@Override
		public void accountUpdated(Account oldAccount, Account newAccount) {
		}
	}
}
