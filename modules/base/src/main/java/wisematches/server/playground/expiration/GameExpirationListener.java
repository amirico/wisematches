package wisematches.server.playground.expiration;

import wisematches.server.playground.room.Room;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GameExpirationListener {
	void gameExpiring(long boardId, Room room, GameExpirationType expiration);
}
