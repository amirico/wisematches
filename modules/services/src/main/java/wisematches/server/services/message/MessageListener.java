package wisematches.server.services.message;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface MessageListener {
	void messageSent(Message message, boolean quite);
}
