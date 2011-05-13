package wisematches.tracking.notice;

/**
 * The player notification interface. Each notification has a type and name.
 * <p/>
 * Usuaaly this interface should be implemented by enumeration where {@code name} is name of field and
 * {@code type} is type of enumeratin (class name, for example).
 * <p/>
 * <pre>
 * public enum BoardNotification implements PlayerNotification {
 *      GAME_STARTED,
 *      ...
 *      GAME_FINISHED
 *
 *      public String type() {
 *          return "BOARD";
 *		 }
 * }
 * </pre>
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface PlayerNotification {
	/**
	 * The type of notification. Type is something like a group. Usually it's short name of
	 * enumeration class.
	 *
	 * @return the type of notification.
	 */
	String type();

	/**
	 * The name of notification. Each name of one type should be unique.
	 *
	 * @return the name of notification.
	 */
	String name();

	/**
	 * Checks is this notification can be sent when player is online.
	 *
	 * @return {@code true} if notification should be sent if player online or offline; {@code false}
	 *         if notification should be sent only to offline player.
	 */
	boolean isOnlineNotification();
}
