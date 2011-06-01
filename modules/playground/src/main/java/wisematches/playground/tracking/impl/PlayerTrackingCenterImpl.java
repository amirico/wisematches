package wisematches.playground.tracking.impl;

import org.apache.log4j.Logger;
import wisematches.personality.Personality;
import wisematches.personality.account.Account;
import wisematches.personality.account.AccountListener;
import wisematches.personality.account.AccountManager;
import wisematches.personality.player.computer.ComputerPlayer;
import wisematches.playground.*;
import wisematches.playground.rating.RatingSystem;
import wisematches.playground.tracking.*;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PlayerTrackingCenterImpl implements PlayerTrackingCenter {
	private BoardManager boardManager;
	private AccountManager accountManager;

	private RatingSystem ratingSystem;
	private StatisticsTrapper statisticsTrapper;
	private PlayerTrackingCenterDao playerTrackingCenterDao;

	private final Lock statisticLock = new ReentrantLock();
	private final Map<Personality, Short> ratings = new WeakHashMap<Personality, Short>();
	private final AccountListener accountListener = new TheAccountListener();
	private final BoardStateListener boardStateListener = new TheBoardStateListener();

	private final Collection<StatisticsListener> statisticsListeners = new CopyOnWriteArraySet<StatisticsListener>();

	private static final Logger log = Logger.getLogger("wisematches.playground.tracking");

	public PlayerTrackingCenterImpl() {
	}

	@Override
	public void addStatisticListener(StatisticsListener l) {
		if (l != null) {
			statisticsListeners.add(l);
		}
	}

	@Override
	public void removeStatisticListener(StatisticsListener l) {
		if (l != null) {
			statisticsListeners.remove(l);
		}
	}

	@Override
	public synchronized short getRating(final Personality person) {
		Short rating = ratings.get(person);
		if (rating == null) {
			ComputerPlayer computerPlayer = ComputerPlayer.getComputerPlayer(person.getId());
			if (computerPlayer != null) {
				rating = computerPlayer.getRating();
			} else {
				rating = playerTrackingCenterDao.getRating(person);
			}
			ratings.put(person, rating);
		}
		return rating;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Statistics getPlayerStatistic(Personality personality) {
		statisticLock.lock();
		try {
			return playerTrackingCenterDao.loadPlayerStatistic(statisticsTrapper.getStatisticType(), personality);
		} finally {
			statisticLock.unlock();
		}
	}

	@Override
	public RatingChanges forecastRatingChanges(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board) {
		final Collection<? extends GamePlayerHand> playersHands = board.getPlayersHands();
		final GamePlayerHand[] hands = playersHands.toArray(new GamePlayerHand[playersHands.size()]);

		final short[] points = new short[hands.length];
		final short[] oldRatings = new short[hands.length];
		for (int i = 0; i < hands.length; i++) {
			final GamePlayerHand hand = hands[i];
			points[i] = hand.getPoints();
			oldRatings[i] = getRating(Personality.person(hand.getPlayerId()));
		}

		final short[] newRatings;
		if (board.isRatedGame()) {
			newRatings = ratingSystem.calculateRatings(oldRatings, points);
		} else {
			newRatings = oldRatings.clone(); // if game is not rated - no changes.
		}

		final Collection<RatingChange> res = new ArrayList<RatingChange>();
		for (int i = 0; i < hands.length; i++) {
			final GamePlayerHand hand = hands[i];
			res.add(new RatingChange(hand.getPlayerId(), board.getBoardId(), new Date(), oldRatings[i], newRatings[i], hand.getPoints()));
		}
		return new RatingChanges(res);
	}

	@Override
	public RatingChanges getRatingChanges(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board) {
		final Collection<RatingChange> ratingChanges = playerTrackingCenterDao.getRatingChanges(board.getBoardId());
		if (ratingChanges != null) {
			for (GamePlayerHand hand : board.getPlayersHands()) {
				final ComputerPlayer cp = ComputerPlayer.getComputerPlayer(hand.getPlayerId());
				if (cp != null) {
					ratingChanges.add(
							new RatingChange(hand.getPlayerId(), board.getBoardId(),
									board.getFinishedTime(), cp.getRating(), cp.getRating(), hand.getPoints()));
				}
			}
			return new RatingChanges(ratingChanges);
		}
		return null;
	}

	@Override
	public RatingChangesCurve getRatingChangesCurve(final Personality player, final int resolution, final Date startDate, final Date endDate) {
		return playerTrackingCenterDao.getRatingChangesCurve(player, resolution, startDate, endDate);
	}


	public void setRatingSystem(RatingSystem ratingSystem) {
		this.ratingSystem = ratingSystem;
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

	public void setStatisticsTrapper(StatisticsTrapper statisticsTrapper) {
		this.statisticsTrapper = statisticsTrapper;
	}

	public void setPlayerTrackingCenterDao(PlayerTrackingCenterDao playerTrackingCenterDao) {
		this.playerTrackingCenterDao = playerTrackingCenterDao;
	}

	protected void processGameStarted(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board) {
		final Collection<? extends GamePlayerHand> hands = board.getPlayersHands();
		for (GamePlayerHand hand : hands) {
			if (isPlayerIgnored(hand)) {
				continue;
			}

			final Personality personality = Personality.person(hand.getPlayerId());
			statisticLock.lock();
			try {
				final StatisticsEditor statistic = (StatisticsEditor) getPlayerStatistic(personality);
				statisticsTrapper.trapGameStarted(board, statistic);
				playerTrackingCenterDao.savePlayerStatistic(statistic);
				fireStatisticUpdated(personality, statistic);
			} catch (Throwable th) {
				log.error("Statistic can't be updated for player: " + personality, th);
			} finally {
				statisticLock.unlock();
			}
		}
	}

	protected void processGameMoveDone(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameMove move) {
		final GamePlayerHand hand = board.getPlayerHand(move.getPlayerMove().getPlayerId());
		if (isPlayerIgnored(hand)) {
			return;
		}

		final Personality personality = Personality.person(hand.getPlayerId());
		statisticLock.lock();
		try {
			final StatisticsEditor statistic = (StatisticsEditor) getPlayerStatistic(personality);
			statisticsTrapper.trapGameMoveDone(board, move, statistic);
			playerTrackingCenterDao.savePlayerStatistic(statistic);
			fireStatisticUpdated(personality, statistic);
		} catch (Throwable th) {
			log.error("Statistic can't be updated for player: " + personality, th);
		} finally {
			statisticLock.unlock();
		}
	}

	protected void processGameFinished(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, RatingChanges changes) {
		final Collection<? extends GamePlayerHand> hands = board.getPlayersHands();
		for (GamePlayerHand hand : hands) {
			if (isPlayerIgnored(hand)) {
				continue;
			}

			// store rating change
			final RatingChange ratingChange = changes.getRatingChange(hand);
			playerTrackingCenterDao.saveRatingChange(ratingChange);

			final Personality personality = Personality.person(hand.getPlayerId());
			statisticLock.lock();
			try {
				final StatisticsEditor statistic = (StatisticsEditor) getPlayerStatistic(personality);
				statisticsTrapper.trapGameFinished(board, changes, statistic);
				playerTrackingCenterDao.savePlayerStatistic(statistic);
				fireStatisticUpdated(personality, statistic);
			} catch (Throwable th) {
				log.error("Statistic can't be updated for player: " + personality, th);
			} finally {
				statisticLock.unlock();
			}
		}
	}

	protected boolean isPlayerIgnored(final GamePlayerHand hand) {
		return ComputerPlayer.getComputerPlayer(hand.getPlayerId()) != null;
	}

	protected void fireStatisticUpdated(Personality player, Statistics statistic) {
		for (StatisticsListener statisticsListener : statisticsListeners) {
			statisticsListener.playerStatisticUpdated(player, statistic);
		}
	}


	private class TheAccountListener implements AccountListener {
		private TheAccountListener() {
		}

		@Override
		public void accountCreated(Account account) {
			playerTrackingCenterDao.savePlayerStatistic(statisticsTrapper.createStatisticsEditor(account));
		}

		@Override
		public void accountRemove(Account account) {
			StatisticsEditor hibernatePlayerStatistic = playerTrackingCenterDao.loadPlayerStatistic(statisticsTrapper.getStatisticType(), account);
			if (hibernatePlayerStatistic != null) {
				playerTrackingCenterDao.removePlayerStatistic(hibernatePlayerStatistic);
			}
		}

		@Override
		public void accountUpdated(Account oldAccount, Account newAccount) {
		}
	}

	private class TheBoardStateListener implements BoardStateListener {
		private TheBoardStateListener() {
		}

		@Override
		public void gameStarted(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board) {
			processGameStarted(board);
		}

		@Override
		public void gameMoveDone(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameMove move) {
			processGameMoveDone(board, move);
		}

		@Override
		public void gameFinished(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameResolution resolution, Collection<? extends GamePlayerHand> wonPlayers) {
			processGameFinished(board, forecastRatingChanges(board));
		}
	}
}
