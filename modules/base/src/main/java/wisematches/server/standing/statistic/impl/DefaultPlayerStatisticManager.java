package wisematches.server.standing.statistic.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.server.personality.Personality;
import wisematches.server.personality.account.Account;
import wisematches.server.personality.account.AccountListener;
import wisematches.server.personality.account.AccountManager;
import wisematches.server.personality.player.computer.ComputerPlayer;
import wisematches.server.playground.board.*;
import wisematches.server.playground.room.RoomManager;
import wisematches.server.playground.room.RoomsManager;
import wisematches.server.standing.statistic.PlayerStatistic;
import wisematches.server.standing.statistic.PlayerStatisticListener;
import wisematches.server.standing.statistic.PlayerStatisticManager;
import wisematches.server.standing.statistic.statistician.GamesStatistician;
import wisematches.server.standing.statistic.statistician.MovesStatistician;
import wisematches.server.standing.statistic.statistician.RatingsStatistician;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultPlayerStatisticManager<S extends GameSettings, P extends GamePlayerHand, B extends GameBoard<S, P>> implements PlayerStatisticManager {
	private RoomsManager roomsManager;
	private AccountManager accountManager;
	private PlayerStatisticDao playerStatisticDao;

	private GamesStatistician<S, P, B> gamesStatistician;
	private MovesStatistician<S, P, B> movesStatistician;
	private RatingsStatistician<S, P, B> ratingsStatistician;

	private final Lock lockLock = new ReentrantLock();
	private final Map<Personality, ReentrantLock> locksMap = new HashMap<Personality, ReentrantLock>();

	private final TheAccountListener accountListener = new TheAccountListener();
	private final TheBoardStateListener boardStateListener = new TheBoardStateListener();

	private final Collection<PlayerStatisticListener> listeners = new CopyOnWriteArraySet<PlayerStatisticListener>();

	private static final Log log = LogFactory.getLog("wisematches.server.statistic");

	public DefaultPlayerStatisticManager() {
	}

	@Override
	public void addPlayerStatisticListener(PlayerStatisticListener l) {
		if (l != null) {
			listeners.add(l);
		}
	}

	@Override
	public void removePlayerStatisticListener(PlayerStatisticListener l) {
		listeners.remove(l);
	}

	@Override
	public PlayerStatistic getPlayerStatistic(Personality personality) {
		lock(personality);
		try {
			return playerStatisticDao.loadPlayerStatistic(personality);
		} finally {
			unlock(personality);
		}
	}

	protected void processGameStarted(B board) {
		final Collection<P> hands = board.getPlayersHands();
		for (P hand : hands) {
			if (isPlayerIgnored(hand)) {
				continue;
			}

			final Personality personality = Personality.person(hand.getPlayerId());
			lock(personality);
			try {
				final HibernatePlayerStatistic statistic = (HibernatePlayerStatistic) getPlayerStatistic(personality);
				gamesStatistician.updateGamesStatistic(board, statistic, statistic.getGamesStatisticEditor());
				updatePlayerStatistic(personality, statistic);
			} catch (Throwable th) {
				log.error("Statistic can't be updated for player: " + personality, th);
			} finally {
				unlock(personality);
			}
		}
	}

	protected void processPlayerMoved(B board, GameMove move) {
		final P hand = board.getPlayerHand(move.getPlayerMove().getPlayerId());
		if (isPlayerIgnored(hand)) {
			return;
		}

		final Personality personality = Personality.person(hand.getPlayerId());
		lock(personality);
		try {
			final HibernatePlayerStatistic statistic = (HibernatePlayerStatistic) getPlayerStatistic(personality);
			movesStatistician.updateMovesStatistic(board, move, statistic, statistic.getMovesStatisticEditor());
			updatePlayerStatistic(personality, statistic);
		} catch (Throwable th) {
			log.error("Statistic can't be updated for player: " + personality, th);
		} finally {
			unlock(personality);
		}
	}

	protected void processGameFinished(B board, GameResolution resolution) {
		final Collection<P> hands = board.getPlayersHands();
		final Collection<P> wonPlayers = board.getWonPlayers();
		for (P hand : hands) {
			if (isPlayerIgnored(hand)) {
				continue;
			}

			final Personality personality = Personality.person(hand.getPlayerId());
			lock(personality);
			try {
				final HibernatePlayerStatistic statistic = (HibernatePlayerStatistic) getPlayerStatistic(personality);
				gamesStatistician.updateGamesStatistic(board, resolution, wonPlayers, statistic, statistic.getGamesStatisticEditor());
				ratingsStatistician.updateRatingsStatistic(board, resolution, wonPlayers, statistic, statistic.getRatingsStatisticEditor());
				updatePlayerStatistic(personality, statistic);
			} catch (Throwable th) {
				log.error("Statistic can't be updated for player: " + personality, th);
			} finally {
				unlock(personality);
			}
		}
	}

	protected boolean isPlayerIgnored(final GamePlayerHand hand) {
		return ComputerPlayer.getComputerPlayer(hand.getPlayerId()) != null;
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

	protected void updatePlayerStatistic(Personality personality, HibernatePlayerStatistic statistic) {
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

	public void setPlayerStatisticDao(PlayerStatisticDao playerStatisticDao) {
		this.playerStatisticDao = playerStatisticDao;
	}

	public void setGamesStatistician(GamesStatistician<S, P, B> gamesStatistician) {
		this.gamesStatistician = gamesStatistician;
	}

	public void setMovesStatistician(MovesStatistician<S, P, B> movesStatistician) {
		this.movesStatistician = movesStatistician;
	}

	public void setRatingsStatistician(RatingsStatistician<S, P, B> ratingsStatistician) {
		this.ratingsStatistician = ratingsStatistician;
	}

	@SuppressWarnings("unchecked")
	private class TheBoardStateListener implements BoardStateListener {
		private TheBoardStateListener() {
		}

		@Override
		public <S extends GameSettings, P extends GamePlayerHand> void gameStarted(GameBoard<S, P> board) {
			processGameStarted((B) board);
		}

		@Override
		public <S extends GameSettings, P extends GamePlayerHand> void gameMoveDone(GameBoard<S, P> board, GameMove move) {
			processPlayerMoved((B) board, move);
		}

		@Override
		public <S extends GameSettings, P extends GamePlayerHand> void gameFinished(GameBoard<S, P> board, GameResolution resolution, Collection<P> wonPlayers) {
			processGameFinished((B) board, resolution);
		}
	}

	@SuppressWarnings("unchecked")
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
