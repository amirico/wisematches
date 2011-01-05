package wisematches.client.gwt.app.client.events;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class EventsDispatcher {
//    private long defaultRefreshTime = 15000; // 5 seconds
//    private long refreshTime = defaultRefreshTime;
//
//    private boolean applicationDied = false;
//    private RefreshTimer refreshTimer = null;
//
//    private final Map<Correlator, EventsListener> listeners = new HashMap<Correlator, EventsListener>();
//
//    public EventsDispatcher() {
//        ExceptionHandler.addExceptionHandlerListener(new ExceptionHandlerListener() {
//            public void applicationSessionExpired() {
//                applicationDied = true;
//
//                if (refreshTimer != null) {
//                    refreshTimer.cancel();
//                }
//            }
//        });
//    }
//
//    /**
//     * Adds specified listener to this dispatcher.
//     * <p/>
//     * If dispatcher already has association between specified correlator and the listener old listener
//     * will be replaced to new one.
//     *
//     * @param listener   the listener to be added.
//     * @param correlator the correlator for preprocessing and filtering events.
//     */
//    public void addEventsListener(Correlator correlator, EventsListener listener) {
//        listeners.put(correlator, listener);
//    }
//
//    public void removeEventsListener(Correlator correlator, EventsListener listener) {
//        if (listener.equals(listeners.get(correlator))) {
//            listeners.remove(correlator);
//        }
//    }
//
//    /**
//     * Updates refresh timer instance.
//     */
//    private void updateRefreshTimer() {
//        if (applicationDied) {
//            return;
//        }
//
//        if (refreshTimer != null) {
//            refreshTimer.cancel();
//        } else {
//            refreshTimer = new RefreshTimer();
//        }
//        refreshTimer.scheduleRepeating((int) refreshTime);
//    }
//
//    /**
//     * Changes rempoorary refresh timeout. This method can change active refresh timeout to new value.
//     * <p/>
//     * Refresh timeout can be reseted to default by {@code resetTemporaryRefreshTime} method.
//     *
//     * @param refreshTime new refresh timeout.
//     */
//    public void setTemporaryRefreshTime(long refreshTime) {
//        this.refreshTime = refreshTime;
//        updateRefreshTimer();
//    }
//
//    /**
//     * Changes timeout to default value.
//     */
//    public void resetTemporaryRefreshTime() {
//        this.refreshTime = defaultRefreshTime;
//        updateRefreshTimer();
//    }
//
//    /**
//     * Changes default refresh timeout. This method should called only if refresh settings is changed.
//     * In case when component want change timeout for small period use a {@code setTemporaryRefreshTime}
//     * method instead.
//     *
//     * @param refreshTime new refresh timeout in milliseconds in milliseconds
//     */
//    public void setDefaultRefreshTime(long refreshTime) {
//        if (refreshTime < 1000) {
//            throw new IllegalArgumentException("Refresh time can't be less than 1000ms.");
//        }
//        this.defaultRefreshTime = refreshTime;
//        updateRefreshTimer();
//    }
//
//    /**
//     * Returns default refresh timeout.
//     *
//     * @return the default refresh timeout in milliseconds.
//     */
//    public long getDefaultRefreshTime() {
//        return defaultRefreshTime;
//    }
//
//    /**
//     * Fires specified events to listeners.
//     *
//     * @param events the events to fire.
//     */
//    protected void fireEventsByListener(Collection<Event> events) {
//        for (Map.Entry<Correlator, EventsListener> entry : listeners.entrySet()) {
//            @SuppressWarnings("unchecked")
//            final Correlator<Event> correlator = entry.getKey();
//            @SuppressWarnings("unchecked")
//            final EventsListener<Event> listener = entry.getValue();
//
//            final List<Event> res = new ArrayList<Event>(events.size());
//            for (final Event event : events) {
//                if (!correlator.isEventSupported(event)) {
//                    continue;
//                }
//
//                boolean add = true;
//                for (Iterator<Event> it1 = res.iterator(); it1.hasNext();) {
//                    final Event re = it1.next();
//
//                    final Correlation correlation = correlator.eventsColleration(event, re);
//                    if (correlation == Correlation.EXCLUSION) {
//                        it1.remove();
//                    } else if (correlation == Correlation.MUTUAL_EXCLUSION) {
//                        it1.remove();
//                        add = false;
//                    }
//                }
//
//                if (add) {
//                    res.add(event);
//                }
//            }
//
//            if (res.size() != 0) {
//                listener.eventsReceived(res);
//            }
//        }
//    }
//
//    /**
//     * Invokes servlet to get events from server.
//     */
//    private void processEvents() {
//        final EventsDispatcherServiceAsync service = EventsDispatcherService.App.getInstance();
//
//        service.getEvents(new AsyncCallback<Collection<Event>>() {
//            public void onFailure(Throwable throwable) {
//                if (refreshTimer != null) { // Cancel timer if it's can't be updated. Something wrong...
//                    refreshTimer.cancel();
//                }
//                ExceptionHandler.showSystemError(throwable);
//            }
//
//            public void onSuccess(Collection<Event> events) {
//                if (events.size() != 0) {
//                    fireEventsByListener(events);
//                }
//            }
//        });
//    }
//
//    public void requestEvents() {
//        processEvents();
//    }
//
//    private class RefreshTimer extends Timer {
//        @Override
//        public void run() {
//            processEvents();
//        }
//    }
}
