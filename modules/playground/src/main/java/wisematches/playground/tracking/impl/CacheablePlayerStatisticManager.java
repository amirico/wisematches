package wisematches.playground.tracking.impl;

import wisematches.personality.Personality;
import wisematches.playground.tracking.PlayerStatisticManager;
import wisematches.playground.tracking.RatingCurve;
import wisematches.playground.tracking.Statistics;
import wisematches.playground.tracking.StatisticsListener;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class CacheablePlayerStatisticManager implements PlayerStatisticManager {
    private PlayerStatisticManager playerStatisticManager;

    private final Lock lock = new ReentrantLock();
    private final Map<Personality, Statistics> cache = new WeakHashMap<Personality, Statistics>();

    private final TheStatisticsListener statisticsListener = new TheStatisticsListener();
    private final Collection<StatisticsListener> listeners = new CopyOnWriteArraySet<StatisticsListener>();

    public CacheablePlayerStatisticManager() {
    }

    @Override
    public void addStatisticListener(StatisticsListener l) {
        if (l != null) {
            listeners.add(l);
        }
    }

    @Override
    public void removeStatisticListener(StatisticsListener l) {
        listeners.remove(l);
    }

    @Override
    public short getRating(Personality person) {
        return playerStatisticManager.getRating(person);
    }

    @Override
    public Statistics getPlayerStatistic(Personality personality) {
        lock.lock();
        try {
            Statistics statistics = cache.get(personality);
            if (statistics == null) {
                statistics = playerStatisticManager.getPlayerStatistic(personality);
                cache.put(personality, statistics);
            }
            return statistics;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public RatingCurve getRatingCurve(Personality player, int resolution, Date startDate, Date endDate) {
        return playerStatisticManager.getRatingCurve(player, resolution, startDate, endDate);
    }

    public void setPlayerStatisticManager(PlayerStatisticManager playerStatisticManager) {
        if (this.playerStatisticManager != null) {
            this.playerStatisticManager.removeStatisticListener(statisticsListener);
        }

        this.playerStatisticManager = playerStatisticManager;

        if (this.playerStatisticManager != null) {
            this.playerStatisticManager.addStatisticListener(statisticsListener);
        }
    }

    private class TheStatisticsListener implements StatisticsListener {
        private TheStatisticsListener() {
        }

        @Override
        public void playerStatisticUpdated(Personality personality, Statistics statistic) {
            lock.lock();
            try {
                if (cache.containsKey(personality)) {
                    cache.put(personality, statistic);
                }
            } finally {
                lock.unlock();
            }

            for (StatisticsListener listener : listeners) {
                listener.playerStatisticUpdated(personality, statistic);
            }
        }
    }
}
