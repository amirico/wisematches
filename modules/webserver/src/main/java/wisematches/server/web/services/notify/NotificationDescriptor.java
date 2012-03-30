package wisematches.server.web.services.notify;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class NotificationDescriptor {
	private final String code;
	private final boolean manageable;

	public NotificationDescriptor(String code, boolean manageable) {
		this.code = code;
		this.manageable = manageable;
	}
}
