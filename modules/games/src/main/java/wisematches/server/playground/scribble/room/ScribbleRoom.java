package wisematches.server.playground.scribble.room;

import wisematches.server.playground.room.Room;
import wisematches.server.playground.scribble.board.ScribbleBoard;
import wisematches.server.playground.scribble.board.ScribbleSettings;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class ScribbleRoom implements Room<ScribbleSettings, ScribbleBoard> {
	public static final ScribbleRoom name = new ScribbleRoom();

	private ScribbleRoom() {
	}
}
