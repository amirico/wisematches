package wisematches.client.gwt.app.client.content.profile.edit;

import static wisematches.client.gwt.app.client.content.i18n.AppRes.APP;

/**
 * This notification info contains information about {@code GameBoardNotification}s.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public enum NotificationInfo {
    GAME_STARTED("BOARD-GAME_STARTED", APP.lblNotificationGameStarted(), APP.ttpNotificationGameStarted()),
    PLAYER_ADDED("BOARD-PLAYER_ADDED", APP.lblNotificationPlayerAdded(), APP.ttpNotificationPlayerAdded()),
    PLAYER_REMOVED("BOARD-PLAYER_REMOVED", APP.lblNotificationPlayerRemoved(), APP.ttpNotificationPlayerRemoved()),
    PLAYER_MOVED("BOARD-PLAYER_MOVED", APP.lblNotificationPlayerMoved(), APP.ttpNotificationPlayerMoved()),
    YOUR_TURN("BOARD-YOUR_TURN", APP.lblNotificationYourTurn(), APP.ttpNotificationYourTurn()),
    TIME_IS_RUNNING("BOARD-TIME_IS_RUNNING", APP.lblNotificationTimeRunning(), APP.ttpNotificationTimeRunning()),
    GAME_FINISHED("BOARD-GAME_FINISHED", APP.lblNotificationGameFinished(), APP.ttpNotificationGameFinished());

    private final String name;
    private final String info;
    private final String type;

    NotificationInfo(String type, String name, String info) {
        this.name = name;
        this.info = info;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public String getType() {
        return type;
    }
}
