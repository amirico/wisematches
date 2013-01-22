package wisematches.playground.tracking.impl;

import org.apache.log4j.Logger;
import wisematches.core.Personality;
import wisematches.core.personality.PlayerManager;
import wisematches.core.personality.member.account.Account;
import wisematches.core.personality.member.account.AccountListener;
import wisematches.core.personality.member.account.AccountManager;
import wisematches.core.personality.proprietary.ProprietaryPlayer;
import wisematches.playground.*;
import wisematches.playground.tourney.TourneyWinner;
import wisematches.playground.tourney.regular.RegularTourneyListener;
import wisematches.playground.tourney.regular.RegularTourneyManager;
import wisematches.playground.tourney.regular.Tourney;
import wisematches.playground.tourney.regular.TourneyDivision;
import wisematches.playground.tracking.*;

import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class StatisticManagerImpl implements StatisticManager {
	private GamePlayManager gamePlayManager;
	private AccountManager accountManager;
	private RegularTourneyManager tourneyManager;

	private RatingManager ratingManager;
	private PlayerManager playerManager;
	private StatisticsTrapper statisticsTrapper;
	private PlayerTrackingCenterDao playerTrackingCenterDao;

	private final Lock statisticLock = new ReentrantLock();
	private final AccountListener accountListener = new TheAccountListener();
	private final GamePlayListener gamePlayListener = new TheGamePlayListener();
	private final RegularTourneyListener tourneyListener = new TheRegularTourneyListener();

	private final Set<StatisticsListener> statisticsListeners = new CopyOnWriteArraySet<>();

	private static final Logger log = Logger.getLogger("wisematches.playground.tracking");

	public StatisticManagerImpl() {
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
	public short getRating(final Personality person) {
		return ratingManager.getRating(person);
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
	public RatingCurve getRatingCurve(final Personality player, final int resolution, final Date startDate, final Date endDate) {
		return playerTrackingCenterDao.getRatingChangesCurve(player, resolution, startDate, endDate);
	}


	public void setGamePlayManager(GamePlayManager gamePlayManager) {
		if (this.gamePlayManager != null) {
			this.gamePlayManager.removeGamePlayListener(gamePlayListener);
		}
		this.gamePlayManager = gamePlayManager;

		if (this.gamePlayManager != null) {
			this.gamePlayManager.addGamePlayListener(gamePlayListener);
		}
	}

	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	public void setRatingManager(RatingManager ratingManager) {
		this.ratingManager = ratingManager;
	}

	public void setTourneyManager(RegularTourneyManager tourneyManager) {
		if (this.tourneyManager != null) {
			this.tourneyManager.removeRegularTourneyListener(tourneyListener);
		}

		this.tourneyManager = tourneyManager;

		if (this.tourneyManager != null) {
			this.tourneyManager.addRegularTourneyListener(tourneyListener);
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

	@SuppressWarnings("unchecked")
	protected void processGameStarted(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board) {
		final Collection<? extends GamePlayerHand> hands = board.getPlayers();
		for (GamePlayerHand hand : hands) {
			if (isPlayerIgnored(hand)) {
				continue;
			}

			final Personality personality = Personality.person(hand.getPlayerId());
			statisticLock.lock();
			try {
				final StatisticsEditor statistic = (StatisticsEditor) getPlayerStatistic(personality);
				statisticsTrapper.trapGameStarted(statistic);
				playerTrackingCenterDao.savePlayerStatistic(statistic);
				fireStatisticUpdated(personality, statistic);
			} catch (Throwable th) {
				log.error("Statistic can't be updated for player: " + personality, th);
			} finally {
				statisticLock.unlock();
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected void processGameMoveDone(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameMove move, GameMoveScore moveScore) {
		final GamePlayerHand hand = board.getPlayerHand(move.getPlayer());
		if (isPlayerIgnored(hand)) {
			return;
		}

		final Personality personality = Personality.person(hand.getPlayerId());
		statisticLock.lock();
		try {
			final StatisticsEditor statistic = (StatisticsEditor) getPlayerStatistic(personality);
			statisticsTrapper.trapGameMoveDone(board, move, moveScore, statistic);
			playerTrackingCenterDao.savePlayerStatistic(statistic);
			fireStatisticUpdated(personality, statistic);
		} catch (Throwable th) {
			log.error("Statistic can't be updated for player: " + personality, th);
		} finally {
			statisticLock.unlock();
		}
	}

	@SuppressWarnings("unchecked")
	protected void processGameFinished(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board) {
		final Collection<? extends GamePlayerHand> hands = board.getPlayers();
		for (GamePlayerHand hand : hands) {
			if (isPlayerIgnored(hand)) {
				continue;
			}

			final Personality personality = Personality.person(hand.getPlayerId());
			statisticLock.lock();
			try {
				final StatisticsEditor statistic = (StatisticsEditor) getPlayerStatistic(personality);
				statisticsTrapper.trapGameFinished(board, statistic);
				playerTrackingCenterDao.savePlayerStatistic(statistic);
				fireStatisticUpdated(personality, statistic);
			} catch (Throwable th) {
				log.error("Statistic can't be updated for player: " + personality, th);
			} finally {
				statisticLock.unlock();
			}
		}
	}

	protected void processTourneyFinished(TourneyDivision division) {
		final Collection<TourneyWinner> tourneyWinners = division.getTourneyWinners();
		for (TourneyWinner winner : tourneyWinners) {
			final Personality personality = Personality.person(winner.getPlayer());
			statisticLock.lock();
			try {
				final StatisticsEditor statistic = (StatisticsEditor) getPlayerStatistic(personality);
				statisticsTrapper.trapTourneyFinished(winner.getPlace(), statistic);
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
		return playerManager.getPlayer(hand.getPlayerId()) instanceof ProprietaryPlayer;
	}

	protected void fireStatisticUpdated(Personality player, StatisticsEditor statistic) {
		final Set<String> strings = statistic.takeChangedProperties();
		for (StatisticsListener statisticsListener : statisticsListeners) {
			statisticsListener.playerStatisticUpdated(player, strings, statistic);
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
		@SuppressWarnings("unchecked")
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

	private class TheGamePlayListener implements GamePlayListener {
		private TheGamePlayListener() {
		}

		@Override
		public void gameStarted(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board) {
			processGameStarted(board);
		}

		@Override
		public void gameMoveDone(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameMove move, GameMoveScore moveScore) {
			processGameMoveDone(board, move, moveScore);
		}

		@Override
		public void gameFinished(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameResolution resolution, Collection<? extends GamePlayerHand> winners) {
			processGameFinished(board);
		}
	}

	private class TheRegularTourneyListener implements RegularTourneyListener {
		private TheRegularTourneyListener() {
		}

		@Override
		public void tourneyAnnounced(Tourney tourney) {
		}

		@Override
		public void tourneyStarted(Tourney tourney) {
		}

		@Override
		public void tourneyFinished(Tourney tourney, TourneyDivision division) {
			processTourneyFinished(division);
		}
	}
}
