package wisematches.client.gwt.app.client.widget.refresh;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RefreshButton {//extends ToolbarMenuButton {
//    private RefreshMenu refreshMenu;
//
//    private RefreshTimer refreshTimer = null;
//
//    private final Collection<RefreshButtonListener> listeners = new ArrayList<RefreshButtonListener>();
//
//    public RefreshButton() {
//        super(APP.btnRefresh());
//        setTooltip(APP.ttpRefreshButton());
//
//        addListener(new ButtonListenerAdapter() {
//            @Override
//            public void onClick(Button button, EventObject eventObject) {
//                fireRefreshData(true);
//            }
//        });
//
//        refreshMenu = new RefreshMenu();
//        refreshMenu.addRefreshListener(new RefreshListener() {
//            public void refreshIntervalChanged(int oldInterval, int newInterval) {
//                setRefreshInterval(oldInterval, newInterval);
//            }
//        });
//        setMenu(refreshMenu);
//    }
//
//    public void addRefreshButtonListener(RefreshButtonListener l) {
//        if (!listeners.contains(l)) {
//            listeners.add(l);
//        }
//    }
//
//    public void removeRefreshButtonListener(RefreshButtonListener l) {
//        listeners.remove(l);
//    }
//
//    public void activate() {
//        int refreshInterval = getRefreshInterval();
//        if (refreshTimer == null && refreshInterval != 0) {
//            refreshTimer = new RefreshTimer();
//            refreshTimer.scheduleRepeating(refreshInterval * 1000);
//        }
//    }
//
//    public void deactivate() {
//        if (refreshTimer != null) {
//            refreshTimer.cancel();
//            refreshTimer = null;
//        }
//    }
//
//    public boolean isActive() {
//        return refreshTimer != null;
//    }
//
//    private void fireRefreshData(boolean handRefresh) {
//        for (RefreshButtonListener listener : listeners) {
//            listener.refreshData(handRefresh);
//        }
//    }
//
//    private void fireRefreshIntervalChanged(int oldInterval, int newInterval) {
//        for (RefreshButtonListener listener : listeners) {
//            listener.refreshIntervalChanged(oldInterval, newInterval);
//        }
//    }
//
//    public void setRefreshInterval(int interval) {
//        setRefreshInterval(getRefreshInterval(), interval);
//    }
//
//    private void setRefreshInterval(int oldInterval, int interval) {
//        if (oldInterval != interval) {
//            refreshMenu.setRefreshInterval(interval);
//
//            if (refreshTimer != null) {
//                refreshTimer.cancel();
//                if (interval != 0) {
//                    refreshTimer.scheduleRepeating(interval * 1000);
//                }
//            }
//
//            fireRefreshIntervalChanged(oldInterval, interval);
//        }
//    }
//
//    public int getRefreshInterval() {
//        return refreshMenu.getRefreshInterval();
//    }
//
//    private class RefreshTimer extends Timer {
//        public void run() {
//            fireRefreshData(false);
//        }
//    }
}
