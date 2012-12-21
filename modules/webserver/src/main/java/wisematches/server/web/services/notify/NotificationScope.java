package wisematches.server.web.services.notify;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum NotificationScope {
	/**
	 * Indicates that notification should be sent only to internal publisher.
	 */
	INTERNAL,
	/**
	 * Indicates that notification should be sent only to external publisher.
	 */
	EXTERNAL,
	/**
	 * Indicates that notification should be sent to both transports.
	 */
	GLOBAL
}
