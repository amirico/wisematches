package wisematches.playground.tracking.impl;

import org.apache.log4j.Logger;
import wisematches.personality.Personality;
import wisematches.personality.account.Account;
import wisematches.personality.account.AccountListener;
import wisematches.personality.account.AccountManager;
import wisematches.personality.player.computer.ComputerPlayer;
import wisematches.playground.*;
import wisematches.playground.tracking.*;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PlayerStatisticManagerImpl implements PlayerStatisticManager {
    private BoardManager boardManager;
    private AccountManager accountManager;

    private RatingManager ratingManager;
    private StatisticsTrapper statisticsTrapper;
    private PlayerTrackingCenterDao playerTrackingCenterDao;

    private final Lock statisticLock = new ReentrantLock();
    private final AccountListener accountListener = new TheAccountListener();
    private final BoardStateListener boardStateListener = new TheBoardStateListener();

    private final Collection<StatisticsListener> statisticsListeners = new CopyOnWriteArraySet<StatisticsListener>();

    private static final Logger log = Logger.getLogger("wisematches.playground.tracking");

    public PlayerStatisticManagerImpl() {
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


    public void setRatingManager(RatingManager ratingManager) {
        this.ratingManager = ratingManager;
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

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
    protected void processGameMoveDone(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameMove move, GameMoveScore moveScore) {
        final GamePlayerHand hand = board.getPlayerHand(move.getPlayerMove().getPlayerId());
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
        final Collection<? extends GamePlayerHand> hands = board.getPlayersHands();
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

    private class TheBoardStateListener implements BoardStateListener {
        private TheBoardStateListener() {
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
}
