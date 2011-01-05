package wisematches.client.gwt.app.client.content.alarms;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class AlarmFacilityWidget extends FlexTable {
	private int notificationNumber = 0;

	private Alarm activeAlarm = null;

	private final TitleBlicker titleBlicker = new TitleBlicker();
	private final Map<String, Alarm> alarms = new HashMap<String, Alarm>();

	private static final AlarmFacilityWidget INSTANCE = new AlarmFacilityWidget();

	private AlarmFacilityWidget() {
		initPanel();
		exportStaticMethod();
	}

	public static AlarmFacilityWidget getInstance() {
		return INSTANCE;
	}

	private native void exportStaticMethod() /*-{
       $wnd.alarmsClearAlarm = @wisematches.client.gwt.app.client.content.alarms.AlarmFacilityWidget::jsniClearAlarm(Ljava/lang/String;);
    }-*/;

	private void initPanel() {
		setStyleName("alarm-box");

		setHTML(0, 0, "&nbsp;");
		setWidget(0, 1, createCloseButton());

		final FlexTable.FlexCellFormatter formatter = getFlexCellFormatter();
		formatter.setWidth(0, 0, "100%");
		formatter.setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);

		formatter.setStyleName(0, 0, "alarm-message");
		setVisible(false);
	}

	private Widget createCloseButton() {
		final Image image = new Image();
		image.setUrl(GWT.getModuleBaseURL() + "/images/alarms/alarm-close.gif");
		image.setStyleName("alarm-close");

		image.addMouseOverHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent mouseOverEvent) {
				image.addStyleName("alarm-close-active");
			}
		});
		image.addMouseOutHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent mouseOutEvent) {
				image.removeStyleName("alarm-close-active");
			}
		});
		image.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent clickEvent) {
				if (activeAlarm != null) {
					image.removeStyleName("alarm-close-active");
					clearAlarm(activeAlarm.getAlarmId());
				}
			}
		});
		return image;
	}

	public String raiseNotification(String instanceId, String title, String message, AlarmLevel level) {
		final String alarmID = String.valueOf(++notificationNumber);

		int index = 0;
		String result = "";
		while (true) {
			int k = message.indexOf("<a", index);
			if (k != -1) {
				result += message.substring(index, k + 2);
				result += " onclick=\"alarmsClearAlarm('" + alarmID + "');\"";
				index = k + 2;
			} else {
				break;
			}
		}
		result += message.substring(index);

		final Alarm alarm = new Alarm(alarmID, instanceId, title, result, level);
		alarms.put(alarm.getAlarmId(), alarm);
		if (activeAlarm == null) {
			showAlarm(alarm);
		}
		return alarm.getAlarmId();
	}

	private static void jsniClearAlarm(String alarmId) {
		INSTANCE.clearAlarm(alarmId);
	}

	public void clearAlarm(String alarmID) {
		alarms.remove(alarmID);
		showNextAlarm();
	}

	public void clearAlarms(String instanceID) {
		for (Iterator<Alarm> it = alarms.values().iterator(); it.hasNext();) {
			final Alarm alarm = it.next();
			if (alarm.getInstanceID().equals(instanceID)) {
				it.remove();
			}
		}
		showNextAlarm();
	}

	public void clearAlarms() {
		alarms.clear();
		showNextAlarm();
	}

	private void showAlarm(Alarm alarm) {
		activeAlarm = alarm;

		setHTML(0, 0, alarm.getMessage());
		setVisible(true);

		if (alarm.getAlarmLevel() == AlarmLevel.CRITICAL) {
			titleBlicker.scheduleNotification(alarm.getTitle());
		}
	}

	private void showNextAlarm() {
		if (alarms.size() == 0) {
			titleBlicker.cancel();
			setVisible(false);
			activeAlarm = null;
		} else {
			showAlarm(alarms.values().iterator().next());
		}
	}

	private static class TitleBlicker extends Timer {
		private String windowTitle;
		private String title;

		private TitleBlicker() {
		}

		@Override
		public void run() {
			if (title.equals(Window.getTitle())) {
				Window.setTitle(windowTitle);
			} else {
				windowTitle = Window.getTitle();
				Window.setTitle(title);
			}
		}

		public void scheduleNotification(String title) {
			this.title = "*** " + title + " ***";
			scheduleRepeating(500);
		}

		@Override
		public void cancel() {
			super.cancel();

			if (title != null && title.equals(Window.getTitle())) {
				Window.setTitle(windowTitle);
			}
		}
	}
}
