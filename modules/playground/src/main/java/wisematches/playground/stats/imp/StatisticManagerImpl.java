package wisematches.playground.stats.imp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.personality.Personality;
import wisematches.personality.account.Account;
import wisematches.personality.account.AccountListener;
import wisematches.personality.account.AccountManager;
import wisematches.personality.player.computer.ComputerPlayer;
import wisematches.playground.*;
import wisematches.playground.stats.PlayerStatistic;
import wisematches.playground.stats.StatisticListener;
import wisematches.playground.stats.StatisticManager;
import wisematches.playground.tracking.StatisticsEditor;
import wisematches.playground.tracking.StatisticsTrapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class StatisticManagerImpl implements StatisticManager {
	private BoardManager boardManager;
	private AccountManager accountManager;
	private PlayerStatisticDao playerStatisticDao;
	private StatisticsTrapper statisticsTrapper;

	private final Lock lockLock = new ReentrantLock();

	private final TheAccountListener accountListener = new TheAccountListener();
	private final TheBoardStateListener boardStateListener = new TheBoardStateListener();

	private final Map<Personality, ReentrantLock> locksMap = new HashMap<Personality, ReentrantLock>();
	private final Collection<StatisticListener> listeners = new CopyOnWriteArraySet<StatisticListener>();

	private static final Log log = LogFactory.getLog("wisematches.server.statistic");

	public StatisticManagerImpl() {
	}

	@Override
	public void addPlayerStatisticListener(StatisticListener l) {
		if (l != null) {
			listeners.add(l);
		}
	}

	@Override
	public void removePlayerStatisticListener(StatisticListener l) {
		listeners.remove(l);
	}

	@Override
	public PlayerStatistic getPlayerStatistic(Personality personality) {
		lock(personality);
		try {
			return playerStatisticDao.loadPlayerStatistic(statisticsTrapper.getStatisticType(), personality);
		} finally {
			unlock(personality);
		}
	}

	protected <S extends GameSettings, P extends GamePlayerHand> void processGameStarted(GameBoard<S, P> board) {
		final Collection<P> hands = board.getPlayersHands();
		for (P hand : hands) {
			if (isPlayerIgnored(hand)) {
				continue;
			}

			final Personality personality = Personality.person(hand.getPlayerId());
			lock(personality);
			try {
				final StatisticsEditor statistic = (StatisticsEditor) getPlayerStatistic(personality);
				statisticsTrapper.trapGameStarted(board, statistic);
				updatePlayerStatistic(personality, statistic);
			} catch (Throwable th) {
				log.error("Statistic can't be updated for player: " + personality, th);
			} finally {
				unlock(personality);
			}
		}
	}

	protected <S extends GameSettings, P extends GamePlayerHand> void processPlayerMoved(GameBoard<S, P> board, GameMove move) {
		final P hand = board.getPlayerHand(move.getPlayerMove().getPlayerId());
		if (isPlayerIgnored(hand)) {
			return;
		}

		final Personality personality = Personality.person(hand.getPlayerId());
		lock(personality);
		try {
			final StatisticsEditor statistic = (StatisticsEditor) getPlayerStatistic(personality);
			statisticsTrapper.trapGameMoveDone(board, move, statistic);
			updatePlayerStatistic(personality, statistic);
		} catch (Throwable th) {
			log.error("Statistic can't be updated for player: " + personality, th);
		} finally {
			unlock(personality);
		}
	}

	public <S extends GameSettings, P extends GamePlayerHand> void processGameFinished(GameBoard<S, P> board, GameResolution resolution, Collection<P> wonPlayers) {
		final Collection<P> hands = board.getPlayersHands();
		for (P hand : hands) {
			if (isPlayerIgnored(hand)) {
				continue;
			}

			final Personality personality = Personality.person(hand.getPlayerId());
			lock(personality);
			try {
				final StatisticsEditor statistic = (StatisticsEditor) getPlayerStatistic(personality);
				statisticsTrapper.trapGameFinished(board, , statistic);
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

	protected void updatePlayerStatistic(Personality personality, StatisticsEditor statistic) {
		playerStatisticDao.savePlayerStatistic(statistic);

		for (StatisticListener listener : listeners) {
			listener.playerStatisticUpdated(personality, statistic);
		}
	}

	public void setBoardManager(BoardManager boardManager) {
		if (this.boardManager != null) {
			this.boardManager.removeBoardStateListener(boardStateListener);
		}

		this.boardManager = boardManager;

		if (this.boardManager != null) {
			this.boardManager.addBoardStateListener(boardStateListener);
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

	public void setStatisticsTrapper(StatisticsTrapper statisticsTrapper) {
		this.statisticsTrapper = statisticsTrapper;
	}

	@SuppressWarnings("unchecked")
	private class TheBoardStateListener implements BoardStateListener {
		private TheBoardStateListener() {
		}

		@Override
		public <S extends GameSettings, P extends GamePlayerHand> void gameStarted(GameBoard<S, P> board) {
			processGameStarted(board);
		}

		@Override
		public <S extends GameSettings, P extends GamePlayerHand> void gameMoveDone(GameBoard<S, P> board, GameMove move) {
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
			playerStatisticDao.savePlayerStatistic(statisticsTrapper.createStatisticsEditor(account));
		}

		@Override
		public void accountRemove(Account account) {
			StatisticsEditor hibernatePlayerStatistic = playerStatisticDao.loadPlayerStatistic(statisticsTrapper.getStatisticType(), account);
			if (hibernatePlayerStatistic != null) {
				playerStatisticDao.removePlayerStatistic(hibernatePlayerStatistic);
			}
		}

		@Override
		public void accountUpdated(Account oldAccount, Account newAccount) {
		}
	}
}