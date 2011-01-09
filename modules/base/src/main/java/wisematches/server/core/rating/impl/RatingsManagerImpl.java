package wisematches.server.core.rating.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.kernel.player.Player;
import wisematches.server.core.rating.PlayerRatingEvent;
import wisematches.server.core.rating.PlayerRatingListener;
import wisematches.server.core.rating.RatingsManager;
import wisematches.server.core.rating.TopPlayersListener;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RatingsManagerImpl implements RatingsManager {
    private int topPlayersNumber = DEFAULT_TOP_PLAYERS_NUMBER;

    private RatingsManagerDao ratingsManagerDao;
    private RatingsCalculationCenter ratingsCaculationManager;

    private final LinkedList<Player> topPlayers = new LinkedList<Player>();

    private final PlayerRatingListener ratingsChangeListener = new TheRatingsChangeListener();
    private final Collection<PlayerRatingListener> listeners = new CopyOnWriteArraySet<PlayerRatingListener>();
    private final Collection<TopPlayersListener> topListeners = new CopyOnWriteArraySet<TopPlayersListener>();

    private final Lock lock = new ReentrantLock();

    private static final Log log = LogFactory.getLog(RatingsManagerImpl.class);

    private static final int DEFAULT_TOP_PLAYERS_NUMBER = 5;

    public RatingsManagerImpl() {
    }

    public void addRatingsChangeListener(PlayerRatingListener l) {
        listeners.add(l);
    }

    public void removeRatingsChangeListener(PlayerRatingListener l) {
        listeners.remove(l);
    }

    public void addTopPlayersListener(TopPlayersListener l) {
        topListeners.add(l);
    }

    public void removeTopPlayersListener(TopPlayersListener l) {
        topListeners.remove(l);
    }

    public List<Player> getTopRatedPlayers() {
        return Collections.unmodifiableList(topPlayers);
    }

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public List<Player> getPlayersRating(long fromPosition, int playersCount, SortType sortType) {
        return ratingsManagerDao.getPlayersRating(fromPosition, playersCount, sortType);
    }

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public long getPlayersCount() {
        return ratingsManagerDao.getPlayersCount();
    }

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public long getPlayerPosition(long playerId) {
        return ratingsManagerDao.getPlayerPosition(playerId);
    }

    /**
     * Updates top player list and returns result of updating.
     *
     * @param changedPlayer the changed player
     * @return {@code true} if top players list was updated; {@code false} - otherwise.
     */
    @Transactional(propagation = Propagation.MANDATORY)
    protected boolean updateTopRatedPlayers(Player changedPlayer) {
        if (log.isDebugEnabled()) {
            log.debug("Update top rated players after rating update: " + changedPlayer);
        }

        lock.lock();
        try {
            final int newRating = changedPlayer.getRating();
            final int lowestRating = topPlayers.size() == 0 ? 0 : topPlayers.getLast().getRating();

            final int playerPosition = topPlayers.indexOf(changedPlayer);
            if (log.isDebugEnabled()) {
                log.debug("Current player position: " + playerPosition + ". Current rating: " + newRating + ". Lowest top rating: " + lowestRating);
            }
            if (playerPosition == -1) {
                if (newRating >= lowestRating) {
                    for (ListIterator<Player> it = topPlayers.listIterator(); it.hasNext();) {
                        final Player p = it.next();
                        if (p.getRating() < newRating) {
                            it.previous(); //move one step back
                            it.add(changedPlayer);
                            break;
                        }
                    }
                    if (log.isInfoEnabled()) {
                        log.info("Player " + changedPlayer + " was added to top players at " +
                                topPlayers.indexOf(changedPlayer) + " position");
                    }

                    if (topPlayers.size() >= topPlayersNumber) {
                        final Player player = topPlayers.removeLast();

                        if (log.isInfoEnabled()) {
                            log.info("Player " + player + " was removed from top players");
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            } else {
                topPlayers.remove(changedPlayer);
                if (newRating >= lowestRating) {
                    for (ListIterator<Player> it = topPlayers.listIterator(); it.hasNext();) {
                        final Player p = it.next();
                        if (p.getRating() < newRating) {
                            it.previous(); //move one step back
                            it.add(changedPlayer);
                            break;
                        }
                    }

                    // If player is not present in topPlayers list we should add it to the end.
                    if (!topPlayers.contains(changedPlayer) && topPlayers.size() < topPlayersNumber) {
                        topPlayers.add(changedPlayer);
                    }

                    if (log.isInfoEnabled()) {
                        log.info("Player " + changedPlayer + " was moved from " + playerPosition +
                                " to " + topPlayers.indexOf(changedPlayer) + " position in top players");
                    }
                    return true;
                } else {
                    if (log.isInfoEnabled()) {
                        log.info("Player " + changedPlayer + " was removed from top players");
                    }
                    topPlayers.addAll(ratingsManagerDao.getPlayersRating(topPlayers.size(),
                            topPlayersNumber - topPlayers.size(), SortType.DESC));
                    return true;
                }
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Changes number of top players
     *
     * @param count the count of top players.
     * @throws IllegalArgumentException if count is negative
     */
    public void setTopPlayersCount(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Top players count can't be less that zero");
        }

        if (topPlayersNumber != count) {
            topPlayersNumber = count;

            if (ratingsManagerDao != null) {
                lock.lock();
                try {
                    if (topPlayers.size() > count) {
                        while (topPlayers.size() > count) {
                            topPlayers.removeLast();
                        }
                    } else {
                        final int playersCount = topPlayers.size();
                        final int needPlayers = count - playersCount;
                        topPlayers.addAll(ratingsManagerDao.getPlayersRating(playersCount, needPlayers, SortType.DESC));
                    }

                    if (log.isDebugEnabled()) {
                        log.debug("Top players list updated: " + topPlayers);
                    }

                    for (TopPlayersListener topListener : topListeners) {
                        topListener.topRatingsUpdated();
                    }
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public void setRatingsManagerDao(RatingsManagerDao ratingsManagerDao) {
        this.ratingsManagerDao = ratingsManagerDao;

        topPlayers.addAll(ratingsManagerDao.getPlayersRating(0, topPlayersNumber, SortType.DESC));

        if (log.isDebugEnabled()) {
            log.debug("Top players list updated: " + topPlayers);
        }
    }

    public void setRatingsCalculationManager(RatingsCalculationCenter ratingsCaculationManager) {
        if (this.ratingsCaculationManager != null) {
            this.ratingsCaculationManager.removeRatingsChangeListener(ratingsChangeListener);
        }

        this.ratingsCaculationManager = ratingsCaculationManager;

        if (ratingsCaculationManager != null) {
            ratingsCaculationManager.addRatingsChangeListener(ratingsChangeListener);
        }
    }

    private final class TheRatingsChangeListener implements PlayerRatingListener {
        public void playerRaitingChanged(PlayerRatingEvent event) {
            if (updateTopRatedPlayers(event.getPlayer())) {
                for (TopPlayersListener topListener : topListeners) {
                    topListener.topRatingsUpdated();
                }
            }

            for (PlayerRatingListener listener : listeners) {
                listener.playerRaitingChanged(event);
            }
        }
    }
}
