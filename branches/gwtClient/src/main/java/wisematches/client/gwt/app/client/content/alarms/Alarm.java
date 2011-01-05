package wisematches.client.gwt.app.client.content.alarms;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
class Alarm {
    private final String alarmId;
    private final String title;
    private final String instanceID;
    private final String message;
    private final AlarmLevel alarmLevel;

    Alarm(String alarmId, String instanceID, String title, String message, AlarmLevel alarmLevel) {
        this.alarmId = alarmId;
        this.instanceID = instanceID;
        this.title = title;
        this.message = message;
        this.alarmLevel = alarmLevel;
    }

    public String getAlarmId() {
        return alarmId;
    }

    public String getTitle() {
        return title;
    }

    public String getInstanceID() {
        return instanceID;
    }

    public String getMessage() {
        return message;
    }

    public AlarmLevel getAlarmLevel() {
        return alarmLevel;
    }
}
