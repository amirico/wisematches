package wisematches.playground.tracking.impl;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import wisematches.personality.Personality;
import wisematches.personality.account.Account;
import wisematches.personality.account.AccountListener;
import wisematches.personality.account.AccountManager;
import wisematches.personality.player.computer.ComputerPlayer;
import wisematches.playground.*;
import wisematches.playground.rating.RatingSystem;
import wisematches.playground.tracking.*;

import java.sql.SQLException;
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
				final HibernateTemplate template = playerTrackingCenterDao.getHibernateTemplate();
				rating = template.execute(new HibernateCallback<Short>() {
					@Override
					public Short doInHibernate(Session session) throws HibernateException, SQLException {
						return ((Number) session.getNamedQuery("player.rating").setLong(0, person.getId()).uniqueResult()).shortValue();
					}
				});
			}
			ratings.put(person, rating);
		}
		return rating;
	}

	@Override
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

		final Map<Long, RatingChange> res = new HashMap<Long, RatingChange>();
		for (int i = 0; i < hands.length; i++) {
			@SuppressWarnings("unchecked")
			final GamePlayerHand hand = hands[i];
			final RatingChange change = new RatingChange(hand.getPlayerId(), board.getBoardId(), new Date(), oldRatings[i], newRatings[i], hand.getPoints());
			res.put(hand.getPlayerId(), change);
		}
		return new RatingChanges(res);
	}

	@Override
	public RatingChanges getRatingChanges(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board) {
		final HibernateTemplate template = playerTrackingCenterDao.getHibernateTemplate();

		@SuppressWarnings("unchecked")
		final Collection<RatingChange> ratingChanges = template.find(
				"from wisematches.playground.tracking.RatingChange rating where rating.boardId = ?", board.getBoardId());

		if (ratingChanges != null) {
			final Map<Long, RatingChange> map = new HashMap<Long, RatingChange>();
			for (RatingChange ratingChange : ratingChanges) {
				map.put(ratingChange.getPlayerId(), ratingChange);
			}

			for (GamePlayerHand hand : board.getPlayersHands()) {
				final ComputerPlayer cp = ComputerPlayer.getComputerPlayer(hand.getPlayerId());
				if (cp != null) {
					map.put(hand.getPlayerId(), new RatingChange(
							hand.getPlayerId(), board.getBoardId(),
							board.getFinishedTime(), cp.getRating(), cp.getRating(), hand.getPoints()));
				}
			}
			return new RatingChanges(map);
		}
		return null;
	}

	@Override
	public RatingChangesCurve getRatingChangesCurve(final Personality player, final int resolution, final Date startDate, final Date endDate) {
		final HibernateTemplate template = playerTrackingCenterDao.getHibernateTemplate();
		return template.execute(new HibernateCallback<RatingChangesCurve>() {
			@Override
			public RatingChangesCurve doInHibernate(Session session) throws HibernateException, SQLException {
				final Query namedQuery = session.getNamedQuery("rating.curve");
				namedQuery.setParameter("pid", player.getId());
				namedQuery.setParameter("resolution", resolution);
				namedQuery.setParameter("start", startDate);
				namedQuery.setParameter("end", endDate);
				return new RatingChangesCurve(resolution, startDate, endDate, namedQuery.list());
			}
		});
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
			playerTrackingCenterDao.getHibernateTemplate().save(ratingChange);

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
