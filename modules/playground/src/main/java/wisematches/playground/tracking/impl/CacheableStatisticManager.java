package wisematches.playground.tracking.impl;

import wisematches.core.Player;
import wisematches.playground.tracking.RatingCurve;
import wisematches.playground.tracking.StatisticManager;
import wisematches.playground.tracking.Statistics;
import wisematches.playground.tracking.StatisticsListener;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class CacheableStatisticManager<S extends Statistics> implements StatisticManager<S> {
	private StatisticManager<S> playerStatisticManager;

	private final Lock lock = new ReentrantLock();
	private final Map<Player, S> cache = new WeakHashMap<>();

	private final TheStatisticsListener statisticsListener = new TheStatisticsListener();
	private final Collection<StatisticsListener> listeners = new CopyOnWriteArraySet<>();

	public CacheableStatisticManager() {
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
	public short getRating(Player player) {
		return playerStatisticManager.getRating(player);
	}

	@Override
	public S getStatistic(Player player) {
		lock.lock();
		try {
			S statistics = cache.get(player);
			if (statistics == null) {
				statistics = playerStatisticManager.getStatistic(player);
				cache.put(player, statistics);
			}
			return statistics;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public RatingCurve getRatingCurve(Player player, int resolution, Date startDate, Date endDate) {
		return playerStatisticManager.getRatingCurve(player, resolution, startDate, endDate);
	}

	public void setPlayerStatisticManager(StatisticManager<S> playerStatisticManager) {
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
		@SuppressWarnings("unchecked")
		public void playerStatisticUpdated(Player player, Statistics statistic, Set<String> changes) {
			lock.lock();
			try {
				if (cache.containsKey(player)) {
					cache.put(player, (S) statistic);
				}
			} finally {
				lock.unlock();
			}

			for (StatisticsListener listener : listeners) {
				listener.playerStatisticUpdated(player, statistic, changes);
			}
		}
	}
}
