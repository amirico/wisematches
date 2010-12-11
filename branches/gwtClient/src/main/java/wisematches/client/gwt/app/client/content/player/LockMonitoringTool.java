package wisematches.client.gwt.app.client.content.player;

import wisematches.client.gwt.app.client.ApplicationFrame;
import wisematches.client.gwt.app.client.ApplicationTool;
import wisematches.client.gwt.app.client.ToolReadyCallback;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class LockMonitoringTool implements ApplicationTool {
//    private static final DateTimeFormat DATE_TIME_FORMAT = DateTimeFormat.getMediumDateTimeFormat();
//
//    public void registerJSCallbacks() {
//    }
//
//    public void initializeTool(ApplicationFrame applicationFrame, ToolReadyCallback callback) {
//        final PlayerLockListener listener = new PlayerLockListener();
//
//        final EventsDispatcher eventsDispatcher = applicationFrame.getApplicationTool(EventsDispatcherTool.class).getEventsDispatcher();
//        eventsDispatcher.addEventsListener(listener, listener);
//
//        callback.toolReady(this);
//    }
//
//    private void shownPlayerLockedMessage(String reasone, long unlockTime) {
//        if (unlockTime != 0) {
//            MessageBox.alert(MCOMMON.tltAccountLocked(),
//                    MCOMMON.msgAccountLocked(reasone, DATE_TIME_FORMAT.format(new Date(unlockTime))));
//        } else {
//            MessageBox.alert(MCOMMON.tltAccountLocked(), MCOMMON.msgAccountLockedNever(reasone));
//        }
//    }
//
//    private void shownPlayerUnlockedMessage() {
//        MessageBox.alert(MCOMMON.tltAccountUnlocked(), MCOMMON.msgAccountUnlocked());
//    }
//
//    private class PlayerLockListener implements EventsListener<PlayerStateEvent>, Correlator<PlayerStateEvent> {
//        public void eventsReceived(Collection<PlayerStateEvent> evens) {
//            for (PlayerStateEvent even : evens) {
//                if (even instanceof PlayerLockedEvent) {
//                    final PlayerLockedEvent e = (PlayerLockedEvent) even;
//                    shownPlayerLockedMessage(e.getReasone(), e.getUnlockTime());
//                } else if (even instanceof PlayerUnlockedEvent) {
//                    shownPlayerUnlockedMessage();
//                }
//            }
//        }
//
//        public boolean isEventSupported(Event e) {
//            return e instanceof PlayerLockedEvent || e instanceof PlayerUnlockedEvent;
//        }
//
//        public Correlation eventsColleration(PlayerStateEvent original, PlayerStateEvent checking) {
//            return Correlation.NEUTRAL;
//        }
//    }

	@Override
	public void registerJSCallbacks() {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void initializeTool(ApplicationFrame applicationFrame, ToolReadyCallback callback) {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
