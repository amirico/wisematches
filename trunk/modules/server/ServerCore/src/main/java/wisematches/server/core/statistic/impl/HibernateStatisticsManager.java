package wisematches.server.core.statistic.impl;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.server.core.statistic.PlayerStatistic;
import wisematches.server.core.statistic.PlayerStatisticListener;
import wisematches.server.core.statistic.StatisticsManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class HibernateStatisticsManager extends HibernateDaoSupport implements StatisticsManager {
    private final Lock lockLock = new ReentrantLock();
    private final Map<Long, ReentrantLock> locksMap = new HashMap<Long, ReentrantLock>();

    private final Collection<PlayerStatisticListener> listeners = new CopyOnWriteArraySet<PlayerStatisticListener>();

    public HibernateStatisticsManager() {
    }

    public void addPlayerStatisticListener(PlayerStatisticListener l) {
        listeners.add(l);
    }

    public void removePlayerStatisticListener(PlayerStatisticListener l) {
        listeners.remove(l);
    }

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public PlayerStatistic getPlayerStatistic(long playerId) {
        lockPlayerStatistic(playerId);
        try {
            final HibernateTemplate template = getHibernateTemplate();
            final PlayerStatistic statistic = (PlayerStatistic) template.get(PlayerStatistic.class, playerId);
            if (statistic == null) {
                // If does not exist create new one. It's possible that many objects will be created
                // if player is 'dead' and don't play no one game but this object will be created
                // only for short profile or full profile and no one will see profiles of 'dead' users.

                // In other case we should mark transaction as not read only
                return new PlayerStatistic(playerId);
            }
            return statistic;
        } finally {
            unlockPlayerStatistic(playerId);
        }
    }

    @Transactional(propagation = Propagation.MANDATORY, readOnly = false)
    public void updatePlayerStatistic(PlayerStatistic statistic) {
        final HibernateTemplate template = getHibernateTemplate();
        statistic.setUpdateTime(System.currentTimeMillis());
        template.saveOrUpdate(statistic);
        template.flush();

        for (PlayerStatisticListener listener : listeners) {
            listener.playerStatisticUpdated(statistic.getPlayerId(), statistic);
        }
    }

    public void lockPlayerStatistic(long playerId) {
        ReentrantLock lock;
        lockLock.lock();
        try {
            lock = locksMap.get(playerId);
            if (lock == null) {
                lock = new ReentrantLock();
                locksMap.put(playerId, lock);
            }
        } finally {
            lockLock.unlock();
        }
        lock.lock();
    }

    public void unlockPlayerStatistic(long playerId) {
        lockLock.lock();
        try {
            final ReentrantLock lock = locksMap.get(playerId);
            if (lock != null) {
                lock.unlock();
                if (!lock.isLocked()) {
                    locksMap.remove(playerId);
                }
            }
        } finally {
            lockLock.unlock();
        }
    }
}
