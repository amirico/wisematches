package wisematches.server.web.modules.app.sessions;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class EventsQueueBeanImpl implements EventsQueueBean {
/*    private final Lock lock = new ReentrantLock();

    private final Queue<Event> eventQueue = new LinkedList<Event>();

    public void offerEvent(Event event) {
        lock.lock();
        try {
            boolean addNewEvent = true;
            if (event instanceof Correlative) {
                for (Iterator<Event> it = eventQueue.iterator(); it.hasNext();) {
                    final Correlative correlative = (Correlative) event;
                    final Correlation eventCorrelation = correlative.eventCorrelation(it.next());
                    if (eventCorrelation == Correlation.EXCLUSION) {
                        it.remove();
                    } else if (eventCorrelation == Correlation.MUTUAL_EXCLUSION) {
                        it.remove();
                        addNewEvent = false;
                    }
                }
            }

            if (addNewEvent) {
                eventQueue.add(event);
            }
        } finally {
            lock.unlock();
        }
    }

    public Queue<Event> poolEvents() {
        lock.lock();
        try {
            final Queue<Event> res = new LinkedList<Event>(eventQueue);
            eventQueue.clear();
            return res;
        } finally {
            lock.unlock();
        }
    }

    *//**
     * This is JUnit test method and does not has synchronization.
     *
     * @return the events queue with applyed filters.
     *//*
    Queue<Event> peekEvents() {
        return eventQueue;
    }*/
}