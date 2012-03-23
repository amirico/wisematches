package wisematches.playground.tracking.impl;

import wisematches.personality.Personality;
import wisematches.playground.tracking.PlayerStatisticManager;
import wisematches.playground.tracking.RatingCurve;
import wisematches.playground.tracking.Statistics;
import wisematches.playground.tracking.StatisticsListener;

import java.util.Date;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class CacheablePlayerStatisticManager implements PlayerStatisticManager {
	private PlayerStatisticManager playerStatisticManager;

	private final Lock lock = new ReentrantLock();
	private final Map<Personality, Statistics> cache = new WeakHashMap<Personality, Statistics>();

	public CacheablePlayerStatisticManager() {
	}

	@Override
	public void addStatisticListener(StatisticsListener l) {
		playerStatisticManager.addStatisticListener(l);
	}

	@Override
	public void removeStatisticListener(StatisticsListener l) {
		playerStatisticManager.removeStatisticListener(l);
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
		this.playerStatisticManager = playerStatisticManager;
	}
}
