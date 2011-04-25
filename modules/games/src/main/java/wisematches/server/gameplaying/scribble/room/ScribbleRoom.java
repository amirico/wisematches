package wisematches.server.gameplaying.scribble.room;

import wisematches.server.gameplaying.room.Room;
import wisematches.server.gameplaying.scribble.board.ScribbleBoard;
import wisematches.server.gameplaying.scribble.board.ScribbleSettings;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class ScribbleRoom implements Room<ScribbleSettings, ScribbleBoard> {
    public static final ScribbleRoom name = new ScribbleRoom();

    private ScribbleRoom() {
    }
}
