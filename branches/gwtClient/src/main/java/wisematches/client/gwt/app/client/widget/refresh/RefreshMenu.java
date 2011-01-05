package wisematches.client.gwt.app.client.widget.refresh;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class RefreshMenu { //extends Menu {
//    private int refreshInterval = 15;
//
//    private final int menuNumber;
//    private final Map<String, CheckItem> items = new HashMap<String, CheckItem>();
//
//    private final CheckItemListener checkItemListener = new TheCheckItemListener();
//    private final Collection<RefreshListener> refreshListeners = new ArrayList<RefreshListener>();
//
//    private static int menusCount = 0;
//
//    public RefreshMenu() {
//        menuNumber = menusCount++;
//
////        final CheckItem checkItem = createCheckItem(MAPP.lblRefreshEachSeconds(5), "5");
//        final CheckItem checkItem1 = createCheckItem(MAPP.lblRefreshEachSeconds(15), "15");
////        final CheckItem checkItem2 = createCheckItem(MAPP.lblRefreshEachSeconds(30), "30");
////        final CheckItem checkItem3 = createCheckItem(MAPP.lblRefreshEachMinutes(1), "60");
////        final CheckItem checkItem4 = createCheckItem(MAPP.lblRefreshDisabled(), "0");
//
//        checkItem1.setChecked(true);
//
////        addItem(checkItem);
//        addItem(checkItem1);
////        addItem(checkItem2);
////        addItem(checkItem3);
////        addItem(checkItem4);
//    }
//
//    public void addRefreshListener(RefreshListener l) {
//        refreshListeners.add(l);
//    }
//
//    public void removeRefreshListener(RefreshListener l) {
//        refreshListeners.remove(l);
//    }
//
//    private CheckItem createCheckItem(final String name, final String value) {
//        final CheckItem checkItem = new CheckItem(name);
//        checkItem.setStateId(value);
//        checkItem.setGroup("refreshInterval" + menuNumber);
//        checkItem.addListener(checkItemListener);
//        items.put(value, checkItem);
//        return checkItem;
//    }
//
//    public int getRefreshInterval() {
//        return refreshInterval;
//    }
//
//    public void setRefreshInterval(int refreshInterval) {
//        if (this.refreshInterval != refreshInterval) {
//            int old = this.refreshInterval;
//            this.refreshInterval = refreshInterval;
//
//            final String stateid = String.valueOf(refreshInterval);
//            final CheckItem checkItem = items.get(stateid);
//
//            if (checkItem != null) {
//                checkItem.setChecked(true);
//            } else {
//                final CheckItem checkItem1 = createCheckItem(MAPP.lblRefreshEachSeconds(refreshInterval), stateid);
//                insert(getItems().length - 1, checkItem1);
//            }
//            fireRefreshIntervalChanged(old, refreshInterval);
//        }
//    }
//
//    private void fireRefreshIntervalChanged(int old, int refreshInterval) {
//        for (RefreshListener listener : refreshListeners) {
//            listener.refreshIntervalChanged(old, refreshInterval);
//        }
//    }
//
//    private class TheCheckItemListener extends CheckItemListenerAdapter {
//        @Override
//        public void onClick(BaseItem baseItem, EventObject eventObject) {
//            setRefreshInterval(Integer.parseInt(baseItem.getStateId()));
//        }
//    }
}
