package wisematches.server.deprecated.web.modules.app.events;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class EventRouter {
/*

    private ExecutorService executor;
    private PlayerSessionsManager playerSessionsManager;

    private final EventNotificator eventNotificator = new TheEventNotificator();

    private final BlockingQueue<Event> blockingQueue = new LinkedBlockingDeque<Event>();
    private final Collection<EventProducer> eventProducers = new ArrayList<EventProducer>();

    private static final Log log = LogFactory.getLog("wisematches.server.events.router");

    public EventRouter() {
    }


    public void resign() {
        log.info("Stop event router and destroy all waiting threads...");

        blockingQueue.clear();
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    */
/**
 * This method just offers specified event to all {@code PlayerEventsQueueBean}s taken from
 * {@code PlayerSessionsManager}.
 *
 * @param event the event to be processed.
 *//*

    private void processEvent(Event event) {
        if (log.isDebugEnabled()) {
            log.debug("Process event: " + event);
        }

        final Collection<PlayerEventsQueueBean> playerSessionBeans = playerSessionsManager.getPlayerSessionBeans();
        for (PlayerEventsQueueBean bean : playerSessionBeans) {
            bean.offerEvent(event);
        }
    }

    */
/**
 * Changes number of event engines that can process a events. One engine has it own thread for event processing.
 *
 * @param count number of engines. Number of engines must equals or be grate that one.
 * @throws IllegalArgumentException if {@code count < 1}
 *//*

    public void setEventEnginesCount(int count) {
        if (count < 1) {
            throw new IllegalArgumentException("Number of engines can be less than one");
        }

        if (log.isDebugEnabled()) {
            log.debug("Changes event engines count to " + count);
        }

        if (executor != null) { // If previous executor exist - shutdown it gracefull
            executor.shutdown();
        }
        if (count == 1) {
            executor = Executors.newSingleThreadExecutor(new ThreadFactory() {
                public Thread newThread(Runnable r) {
                    return new Thread(r, "EventRouterEngine");
                }
            });
        } else {
            executor = Executors.newFixedThreadPool(count, new ThreadFactory() {
                private int count = 0;

                public Thread newThread(Runnable r) {
                    return new Thread(r, "EventRouterEngine#" + (count++));
                }
            });
        }

        // Now start requred number of event processors...
        for (int i = 0; i < count; i++) {
            executor.execute(new EventProcessor());
        }
    }

    public void setEventProducers(Collection<EventProducer> eventProducers) {
        if (log.isDebugEnabled()) {
            log.debug("Register new event producers: " + eventProducers);
        }

        for (EventProducer producer : this.eventProducers) {
            producer.deactivateProducer();
        }
        this.eventProducers.clear();

        if (eventProducers != null) {
            for (EventProducer producer : eventProducers) {
                producer.activateProducer(eventNotificator);
                this.eventProducers.add(producer);
            }
        }
    }

    public void setPlayerSessionsManager(PlayerSessionsManager playerSessionsManager) {
        this.playerSessionsManager = playerSessionsManager;
    }


    private final class EventProcessor implements Runnable {
        public void run() {
            if (log.isDebugEnabled()) {
                log.debug(Thread.currentThread().getName() + " started");
            }

            while (true) {
                final Event event;
                try {
                    if (log.isDebugEnabled()) {
                        log.debug(Thread.currentThread().getName() + " waiting next event");
                    }
                    event = blockingQueue.take();
                } catch (InterruptedException e) {
                    break;
                }

                if (log.isDebugEnabled()) {
                    log.debug(Thread.currentThread().getName() + " process event: " + event);
                }
                if (event != null) {
                    processEvent(event);
                }
            }

            if (log.isDebugEnabled()) {
                log.debug(Thread.currentThread().getName() + " destroyed");
            }
        }
    }

    private final class TheEventNotificator implements EventNotificator {
        public void fireEvent(Event event) {
            if (event == null) {
                throw new NullPointerException("Event can't be null");
            }

            if (log.isDebugEnabled()) {
                log.debug("Event fired to router: " + event);
            }

            try {
                blockingQueue.put(event);
            } catch (InterruptedException e) {
                log.warn("Event can't be putted into queue because thread was interrupted");
            }
        }
    }
*/
}
