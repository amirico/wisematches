package wisematches.server.standing.lock;

import wisematches.server.player.Player;

import java.io.Serializable;
import java.util.Date;

/**
 * Information about lock of account. Who, when and why locked the player.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface LockAccountInfo extends Serializable {
	Player getPlayer();

	String getPublicReason();

	String getPrivateReason();

	Date getLockDate();

	Date getUnlockDate();
}
