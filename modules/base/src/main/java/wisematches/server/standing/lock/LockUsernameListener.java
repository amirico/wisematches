package wisematches.server.standing.lock;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface LockUsernameListener {
	void usernameLocked(String username, String reason);

	void usernameUnlocked(String username);
}
