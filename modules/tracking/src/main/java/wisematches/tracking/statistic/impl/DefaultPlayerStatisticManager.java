package wisematches.tracking.statistic.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.personality.Personality;
import wisematches.personality.account.Account;
import wisematches.personality.account.AccountListener;
import wisematches.personality.account.AccountManager;
import wisematches.personality.player.computer.ComputerPlayer;
import wisematches.server.playground.room.RoomManager;
import wisematches.server.playground.room.RoomsManager;
import wisematches.tracking.statistic.PlayerStatistic;
import wisematches.tracking.statistic.PlayerStatisticListener;
import wisematches.tracking.statistic.PlayerStatisticManager;
import wisematches.tracking.statistic.statistician.PlayerStatisticEditor;

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
	private PlayerStatisticFactory playerStatisticFactory;

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
			return playerStatisticDao.loadPlayerStatistic(playerStatisticFactory.getStatisticType(), personality);
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
				final PlayerStatisticEditor statistic = (PlayerStatisticEditor) getPlayerStatistic(personality);
				playerStatisticFactory.getGamesStatistician().updateGamesStatistic(board, statistic, statistic.getGamesStatisticEditor());
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
			final PlayerStatisticEditor statistic = (PlayerStatisticEditor) getPlayerStatistic(personality);
			playerStatisticFactory.getMovesStatistician().updateMovesStatistic(board, move, statistic, statistic.getMovesStatisticEditor());
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
				final PlayerStatisticEditor statistic = (PlayerStatisticEditor) getPlayerStatistic(personality);
				playerStatisticFactory.getGamesStatistician().updateGamesStatistic(board, resolution, wonPlayers, statistic, statistic.getGamesStatisticEditor());
				playerStatisticFactory.getRatingsStatistician().updateRatingsStatistic(board, resolution, wonPlayers, statistic, statistic.getRatingsStatisticEditor());
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

	protected void updatePlayerStatistic(Personality personality, PlayerStatisticEditor statistic) {
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

	public void setPlayerStatisticFactory(PlayerStatisticFactory playerStatisticFactory) {
		this.playerStatisticFactory = playerStatisticFactory;
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
			playerStatisticDao.savePlayerStatistic(playerStatisticFactory.createPlayerStatistic(account));
		}

		@Override
		public void accountRemove(Account account) {
			PlayerStatisticEditor hibernatePlayerStatistic = playerStatisticDao.loadPlayerStatistic(playerStatisticFactory.getStatisticType(), account);
			if (hibernatePlayerStatistic != null) {
				playerStatisticDao.removePlayerStatistic(hibernatePlayerStatistic);
			}
		}

		@Override
		public void accountUpdated(Account oldAccount, Account newAccount) {
		}
	}
}
