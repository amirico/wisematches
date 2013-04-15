package wisematches.playground.tracking.impl;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import wisematches.core.Player;
import wisematches.core.cache.NoOpCache;
import wisematches.playground.tracking.RatingCurve;
import wisematches.playground.tracking.Statistics;
import wisematches.playground.tracking.StatisticsListener;
import wisematches.playground.tracking.StatisticsManager;

import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class CacheableStatisticsManager<S extends Statistics> implements StatisticsManager<S> {
	private Cache ratingCache = NoOpCache.INSTANCE;
	private Cache statisticCache = NoOpCache.INSTANCE;

	private StatisticsManager<S> playerStatisticManager;

	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private final Lock readLock = lock.readLock();
	private final Lock writeLock = lock.writeLock();

	private final TheStatisticsListener statisticsListener = new TheStatisticsListener();
	private final Collection<StatisticsListener> listeners = new CopyOnWriteArraySet<>();

	public CacheableStatisticsManager() {
	}

	@Override
	public void addStatisticsListener(StatisticsListener l) {
		if (l != null) {
			listeners.add(l);
		}
	}

	@Override
	public void removeStatisticsListener(StatisticsListener l) {
		listeners.remove(l);
	}

	@Override
	public short getRating(Player player) {
		readLock.lock();
		try {
			short rating;

			final Cache.ValueWrapper valueWrapper = ratingCache.get(player);
			if (valueWrapper != null) {
				rating = (short) valueWrapper.get();
			} else {
				rating = playerStatisticManager.getRating(player);
				ratingCache.put(player, rating);
			}
			return rating;
		} finally {
			readLock.unlock();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public S getStatistic(Player player) {
		readLock.lock();
		try {
			S statistics;
			final Cache.ValueWrapper valueWrapper = statisticCache.get(player);
			if (valueWrapper != null) {
				statistics = (S) valueWrapper.get();
			} else {
				statistics = playerStatisticManager.getStatistic(player);
				statisticCache.put(player, statistics);
			}
			return statistics;
		} finally {
			readLock.unlock();
		}
	}

	@Override
	public RatingCurve getRatingCurve(Player player, int resolution, Date startDate, Date endDate) {
		return playerStatisticManager.getRatingCurve(player, resolution, startDate, endDate);
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.ratingCache = cacheManager.getCache("rating");
		this.statisticCache = cacheManager.getCache("statistic");

		if (this.ratingCache == null) {
			throw new IllegalArgumentException("CacheManager doesn't have 'rating' cache");
		}
		if (this.statisticCache == null) {
			throw new IllegalArgumentException("CacheManager doesn't have 'statistic' cache");
		}
	}

	public void setPlayerStatisticManager(StatisticsManager<S> playerStatisticManager) {
		if (this.playerStatisticManager != null) {
			this.playerStatisticManager.removeStatisticsListener(statisticsListener);
		}

		this.playerStatisticManager = playerStatisticManager;

		if (this.playerStatisticManager != null) {
			this.playerStatisticManager.addStatisticsListener(statisticsListener);
		}
	}

	private class TheStatisticsListener implements StatisticsListener {
		private TheStatisticsListener() {
		}

		@Override
		@SuppressWarnings("unchecked")
		public void playerStatisticUpdated(Player player, Statistics statistic, Set<String> changes) {
			writeLock.lock();
			try {
				final Cache.ValueWrapper ratingWrapper = ratingCache.get(player);
				if (ratingWrapper != null) {
					ratingCache.put(player, statistic.getRating());
				}

				final Cache.ValueWrapper statisticWrapper = statisticCache.get(player);
				if (statisticWrapper != null) {
					statisticCache.put(player, statistic);
				}
			} finally {
				writeLock.unlock();
			}

			for (StatisticsListener listener : listeners) {
				listener.playerStatisticUpdated(player, statistic, changes);
			}
		}
	}
}
