package wisematches.tracking.notice;

/**
 * This interface allows manage player notifications and accessible from {@code Player} object.
 * <p/>
 * By default all notifications are enabled and only disabled notifications stored into storage.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface PlayerNotifications {
	/**
	 * Adds specified notification to disabled set.
	 * <p/>
	 * If notification disabled {@code #isNotificationEnabled} method will return {@code false}.
	 * <p/>
	 * If notification already disabled nothing is happend.
	 *
	 * @param type the type of notification.
	 * @throws NullPointerException if {@code type} is null.
	 * @see #isNotificationEnabled(PlayerNotification)
	 */
	public void addDisabledNotification(PlayerNotification type);

	/**
	 * Removes specified notification from disabled set.
	 * <p/>
	 * If notification already enabled nothing is happend.
	 *
	 * @param type the type of notification.
	 * @throws NullPointerException if {@code type} is null.
	 * @see #isNotificationEnabled(PlayerNotification)
	 */
	public void removeDisabledNotification(PlayerNotification type);

	/**
	 * Checks if notification of specified type is enabled or not.
	 *
	 * @param type the type of notification.
	 * @return {@code true} if notification enabled and allowed; {@code false} - otherwise.
	 * @throws NullPointerException if {@code type} is null.
	 */
	public boolean isNotificationEnabled(PlayerNotification type);
}
