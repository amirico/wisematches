package wisematches.server.player.lock;

import java.io.Serializable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface LockUsernameInfo extends Serializable {
	String getUsername();

	String getReason();
}
