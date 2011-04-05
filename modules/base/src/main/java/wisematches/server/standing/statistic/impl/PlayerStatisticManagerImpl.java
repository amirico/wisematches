package wisematches.server.standing.statistic.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.server.gameplaying.board.*;
import wisematches.server.gameplaying.room.RoomManager;
import wisematches.server.gameplaying.room.RoomsManager;
import wisematches.server.gameplaying.room.board.BoardStateListener;
import wisematches.server.personality.Personality;
import wisematches.server.personality.player.computer.ComputerPlayer;
import wisematches.server.standing.rating.PlayerRatingManager;
import wisematches.server.standing.rating.RatingChange;
import wisematches.server.standing.statistic.PlayerStatistic;
import wisematches.server.standing.statistic.PlayerStatisticListener;
import wisematches.server.standing.statistic.PlayerStatisticManager;
import wisematches.server.standing.statistic.PlayerStatisticRating;

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
    private PlayerRatingManager ratingManager;
    private PlayerStatisticDao playerStatisticDao;

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
    public PlayerStatistic getPlayerStatistic(Personality personality) {
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
                final PlayerStatistic statistic = getPlayerStatistic(personality);
                statistic.setActiveGames(statistic.getActiveGames() + 1);

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
            final PlayerStatistic statistic = getPlayerStatistic(personality);
            final Date currentMoveTime = move.getMoveTime();
            final Date previousMoveTime = previousMoveTime(board);

            statistic.setTurnsCount(statistic.getTurnsCount() + 1);
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

    protected <S extends GameSettings, P extends GamePlayerHand> void processGameFinished(GameBoard<S, P> board, Collection<P> wonPlayers) {
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
                final PlayerStatistic statistic = getPlayerStatistic(personality);
                statistic.setActiveGames(statistic.getActiveGames() - 1);
                if (ratedGame) { // If game is not rated just ignore it
                    if (wonPlayers.isEmpty()) { // draw
                        statistic.setDrawGames(statistic.getDrawGames() + 1);
                    } else {
                        if (wonPlayers.contains(hand)) {
                            statistic.setWonGames(statistic.getWonGames() + 1);
                        } else {
                            statistic.setLostGames(statistic.getLostGames() + 1);
                        }
                    }
                    updateRatingsInfo(board, hand, ratingChanges, statistic);
                }
                updatePlayerStatistic(personality, statistic);
            } finally {
                unlock(personality);
            }
        }
    }

    protected void updateRatingsInfo(GameBoard board, GamePlayerHand hand,
                                     Collection<RatingChange> changes, PlayerStatistic statistic) {
        updateRatingInfo(board, hand, changes, statistic, statistic.getAllGamesStatisticRating());
/*
		updateRatingInfo(statistic, statistic.getNinetyDaysRatingInfo(), board, hand);
		updateRatingInfo(statistic, statistic.getYearRatingInfo(), board, hand);
		updateRatingInfo(statistic, statistic.getThirtyDaysRatingInfo(), board, hand);
*/
    }

    protected void updateRatingInfo(GameBoard board, GamePlayerHand hand, Collection<RatingChange> changes, PlayerStatistic statistic, PlayerStatisticRating ri) {
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

        int opponentsRatings = 0;
        RatingChange currentRating = null;
        RatingChange maxOpponent = null;
        RatingChange minOpponent = null;
        for (RatingChange change : changes) {
            if (change.getPlayerId() == hand.getPlayerId()) { // Exclude current player
                currentRating = change;
                continue;
            }

            final int oppRating = change.getOldRating();
            if (change.getPoints() < hand.getPoints() && //you won
                    (maxOpponent == null || oppRating > maxOpponent.getOldRating())) {
                maxOpponent = change;
            }
            if (change.getPoints() > hand.getPoints() && //you lose
                    (minOpponent == null || oppRating < minOpponent.getOldRating())) {
                minOpponent = change;
            }
            opponentsRatings += change.getOldRating();
        }

        if (currentRating == null) {
            log.warn("Player rating can't be found: " + hand.getPlayerId() + " at board " + board.getBoardId());
            return;
        }

        final short rating = currentRating.getNewRating();
        final short averageOpponentsRating = (short) (opponentsRatings / (changes.size() - 1));
        ri.setAverageOpponentRating(average(ri.getAverageOpponentRating(), averageOpponentsRating, gamesCount));
        ri.setAverageRating(average(ri.getAverageRating(), rating, gamesCount));

        if (maxOpponent != null && ri.getHighestWonOpponentRating() < maxOpponent.getOldRating()) {
            ri.setHighestWonOpponentRating(maxOpponent.getOldRating());
            ri.setHighestWonOpponentId(maxOpponent.getPlayerId());
        }

        if (minOpponent != null &&
                (ri.getLowestLostOpponentRating() == 0 || ri.getLowestLostOpponentRating() > minOpponent.getOldRating())) {
            ri.setLowestLostOpponentRating(minOpponent.getOldRating());
            ri.setLowestLostOpponentId(minOpponent.getPlayerId());
        }

        if (ri.getLowestRating() == 0) {
            if (rating < currentRating.getOldRating()) {
                ri.setLowestRating(rating);
            } else {
                ri.setLowestRating(currentRating.getOldRating());
            }
        } else if (rating < ri.getLowestRating()) {
            ri.setLowestRating(rating);
        }

        if (ri.getHighestRating() == 0) {
            if (rating < currentRating.getOldRating()) {
                ri.setHighestRating(currentRating.getOldRating());
            } else {
                ri.setHighestRating(rating);
            }
        } else if (rating > ri.getHighestRating()) {
            ri.setHighestRating(rating);
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

    protected void updatePlayerStatistic(Personality personality, PlayerStatistic statistic) {
        statistic.setUpdateTime(new Date());

        playerStatisticDao.savePlayerStatistic(personality, statistic);

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

    public void setPlayerRatingManager(PlayerRatingManager ratingManager) {
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
        public <S extends GameSettings, P extends GamePlayerHand> void gameFinished(GameBoard<S, P> board, GameResolution gameResolution, Collection<P> wonPlayers) {
            processGameFinished(board, wonPlayers);
        }
    }
}
