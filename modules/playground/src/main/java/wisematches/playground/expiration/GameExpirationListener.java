package wisematches.playground.expiration;

import wisematches.playground.room.Room;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GameExpirationListener {
	void gameExpiring(long boardId, Room room, GameExpirationType expiration);
}
