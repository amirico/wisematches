package wisematches.server.web.player;

import wisematches.server.player.Player;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface WebPlayer extends Player {
	short getRating();

	boolean isGuest();

	boolean isRobot();
}
