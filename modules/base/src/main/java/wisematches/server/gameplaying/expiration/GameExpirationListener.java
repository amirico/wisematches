package wisematches.server.gameplaying.expiration;

import wisematches.server.gameplaying.room.Room;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GameExpirationListener {
	void gameExpiring(long boardId, Room room, GameExpirationType expiration);
}
