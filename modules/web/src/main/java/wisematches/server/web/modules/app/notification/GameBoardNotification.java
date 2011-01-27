package wisematches.server.web.modules.app.notification;

import wisematches.server.testimonial.notice.PlayerNotification;

/**
 * Enum of notification types. {@code NotificationsSender} can fires only predefined numbers of notifications.
 * <p/>
 * Each notification can be sent only if player is online.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public enum GameBoardNotification implements PlayerNotification {
	GAME_STARTED(false),
	PLAYER_ADDED(false),
	PLAYER_REMOVED(false),
	PLAYER_MOVED(false),
	YOUR_TURN(false),
	TIME_IS_RUNNING(false),
	GAME_FINISHED(true);

	private final boolean eventOnline;

	private GameBoardNotification(boolean eventOnline) {
		this.eventOnline = eventOnline;
	}

	public String type() {
		return "BOARD";
	}

	public boolean isOnlineNotification() {
		return eventOnline;
	}
}
