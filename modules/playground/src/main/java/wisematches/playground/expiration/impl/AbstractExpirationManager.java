package wisematches.playground.expiration.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import wisematches.playground.expiration.ExpirationListener;
import wisematches.playground.expiration.ExpirationManager;
import wisematches.playground.expiration.ExpirationType;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * TODO: no test case
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class AbstractExpirationManager<ID, T extends Enum<? extends ExpirationType>> implements ExpirationManager<ID, T> {
    private TaskScheduler taskScheduler;

    protected final Lock lock = new ReentrantLock();
    protected TransactionTemplate transactionTemplate;

    private final T[] expirationTypes;
    private final Map<ID, ScheduledFuture> scheduledExpirations = new HashMap<ID, ScheduledFuture>();
    private final Collection<ExpirationListener<ID, T>> listeners = new CopyOnWriteArraySet<ExpirationListener<ID, T>>();

    protected final Log log = LogFactory.getLog("wisematches.playground.expiration." + getClass().getSimpleName().toLowerCase());

    protected AbstractExpirationManager(Class<T> typesClass) {
        if (typesClass == null) {
            throw new NullPointerException("Types class can't be null");
        }
        expirationTypes = typesClass.getEnumConstants();
    }

    @Override
    public final void addExpirationListener(ExpirationListener<ID, T> l) {
        if (l != null) {
            listeners.add(l);
        }
    }

    @Override
    public final void removeExpirationListener(ExpirationListener<ID, T> l) {
        listeners.remove(l);
    }


    protected final void scheduleTermination(final ID id, Date extinctionTime) {
        lock.lock();
        try {
            ScheduledFuture scheduledFuture = scheduledExpirations.get(id);
            if (scheduledFuture != null) {
                scheduledFuture.cancel(false);
            }

            final ScheduledFuture schedule;
            final T type = nextExpiringPoint(extinctionTime);
            final ExpirationTask task = new ExpirationTask(id, extinctionTime, type);
            if (type == null) { // expired
                log.info("Board is expired: " + id + " and will be terminated");
                schedule = taskScheduler.schedule(task, extinctionTime);
            } else {
                final Date triggerTime = new Date(((ExpirationType) type).getTriggerTime(extinctionTime.getTime()));
                log.info("Start expiration scheduler: " + id + " to " + triggerTime + "(" + type + ")");
                schedule = taskScheduler.schedule(task, triggerTime);
            }
            scheduledExpirations.put(id, schedule);
        } finally {
            lock.unlock();
        }
    }

    protected final void cancelTermination(final ID id) {
        lock.lock();
        try {
            log.info("Cancel board termination " + id);

            ScheduledFuture scheduledFuture = scheduledExpirations.get(id);
            if (scheduledFuture != null) {
                scheduledFuture.cancel(false);
            }
        } finally {
            lock.unlock();
        }
    }

    protected abstract boolean executeTermination(ID id);

    protected final T nextExpiringPoint(Date extinctionTime) {
        final long currentTime = System.currentTimeMillis();
        for (T type : expirationTypes) {
            if (((ExpirationType) type).getTriggerTime(extinctionTime.getTime()) >= currentTime) {
                return type;
            }
        }
        return null;
    }

    private boolean terminateOrNotify(final ID id, final T type) {
        if (type == null) {
            return !executeTermination(id);
        } else {
            for (ExpirationListener<ID, T> listener : listeners) {
                listener.expirationTriggered(id, type);
            }
            return true;
        }
    }

    private void processExpiration(final ID id, final Date extinctionTime, final T type) {
        lock.lock();
        try {
            log.info("Process game expiration: " + id + ": " + type);
            final ScheduledFuture scheduledFuture = scheduledExpirations.get(id);
            if (scheduledFuture != null && !scheduledFuture.isCancelled()) {
                final boolean reschedulingRequired;
                if (transactionTemplate != null) {
                    reschedulingRequired = transactionTemplate.execute(new TransactionCallback<Boolean>() {
                        @Override
                        public Boolean doInTransaction(TransactionStatus status) {
                            return terminateOrNotify(id, type);
                        }
                    });
                } else {
                    reschedulingRequired = terminateOrNotify(id, type);
                }

                if (reschedulingRequired) {
                    scheduleTermination(id, extinctionTime);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void destroy() {
        lock.lock();
        try {
            for (ScheduledFuture scheduledFuture : scheduledExpirations.values()) {
                scheduledFuture.cancel(false);
            }
        } finally {
            lock.unlock();
        }
    }

    public void setTaskScheduler(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    private class ExpirationTask implements Runnable {
        private final ID id;
        private final Date extinctionTime;
        private final T type;

        private ExpirationTask(ID id, Date extinctionTime, T type) {
            this.id = id;
            this.extinctionTime = extinctionTime;
            this.type = type;
        }

        @Override
        public void run() {
            processExpiration(id, extinctionTime, type);
        }
    }
}
