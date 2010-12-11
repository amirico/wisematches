package wisematches.client.gwt.app.client.content.profile.edit;

import com.smartgwt.client.widgets.Canvas;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class NotificationsPanel extends Canvas {
//    private final Map<String, Checkbox> checkboxes = new HashMap<String, Checkbox>();
//
//    public NotificationsPanel() {
//        initPanel();
//    }
//
//    private void initPanel() {
//        setTitle(APP.lblNotifications());
//        setBodyBorder(false);
//
//        final FormLayout formLayout = new FormLayout();
//        formLayout.setLabelWidth(5);
//        setLayout(formLayout);
//        setPaddings(10, 0, 0, 10);
//
//        final FieldSet gameNotifications = new FieldSet();
//        gameNotifications.setLabelWidth(5);
//        gameNotifications.setTitle(APP.lblGaneBoardNotifications());
//        gameNotifications.setBorder(false);
//
//        final NotificationInfo[] notificationTypes = NotificationInfo.values();
//        for (NotificationInfo notificationInfo : notificationTypes) {
//            final Checkbox checkbox = new Checkbox(notificationInfo.getName(), notificationInfo.name());
//            checkbox.setValue(true);
//
//            final ToolTip tooltip = new ToolTip();
//            tooltip.setHtml(notificationInfo.getInfo());
//            tooltip.applyTo(checkbox);
//
//            checkboxes.put(notificationInfo.getType(), checkbox);
//
//            gameNotifications.add(checkbox);
//        }
//        add(gameNotifications);
//    }
//
//    public void updatePanelData(Set<String> disabledNotifications) {
//        if (disabledNotifications == null || disabledNotifications.size() == 0) {
//            return;
//        }
//
//        for (Map.Entry<String, Checkbox> entry : checkboxes.entrySet()) {
//            final Checkbox checkbox = entry.getValue();
//            checkbox.setValue(!disabledNotifications.contains(entry.getKey()));
//        }
//    }
//
//    public void updateBeanData(PlayerSettingsBean settingsBean) {
//        final Set<String> enabled = settingsBean.getEnabledNotifications();
//        final Set<String> disabled = settingsBean.getDisabledNotifications();
//
//        enabled.clear();
//        disabled.clear();
//
//        for (Map.Entry<String, Checkbox> entry : checkboxes.entrySet()) {
//            final String type = entry.getKey();
//            final Checkbox checkbox = entry.getValue();
//            if (checkbox.getValue()) {
//                enabled.add(type);
//            } else {
//                disabled.add(type);
//            }
//        }
//    }
}
