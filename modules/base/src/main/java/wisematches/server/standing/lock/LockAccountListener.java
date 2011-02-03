package wisematches.server.standing.lock;

import wisematches.server.player.Player;

import java.util.Date;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface LockAccountListener {
	void accountLocked(Player player, String publicReason, String privateReason, Date unlockdate);

	void accountUnlocked(Player player);
}
