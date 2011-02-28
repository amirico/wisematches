package wisematches.server.gameplaying.scribble.room;

import wisematches.server.gameplaying.board.GameBoardListener;
import wisematches.server.gameplaying.room.BoardLoadingException;
import wisematches.server.gameplaying.room.RoomManagerFacade;
import wisematches.server.gameplaying.scribble.board.ScribbleBoard;
import wisematches.server.gameplaying.scribble.board.ScribbleSettings;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class ScribbleRoomManagerFacade implements RoomManagerFacade<ScribbleBoard, ScribbleSettings> {
	private final ScribbleRoomManager scribbleRoomManager;

	public ScribbleRoomManagerFacade(ScribbleRoomManager scribbleRoomManager) {
		this.scribbleRoomManager = scribbleRoomManager;
	}

	public void addGameBoardListener(GameBoardListener listener) {
		scribbleRoomManager.addGameBoardListener(listener);
	}

	public void removeGameBoardListener(GameBoardListener listener) {
		scribbleRoomManager.removeGameBoardListener(listener);
	}

	public ScribbleBoard openBoard(long gameId) throws BoardLoadingException {
		return scribbleRoomManager.openBoard(gameId);
	}

	public ScribbleRoomManager getRoomManager() {
		return scribbleRoomManager;
	}
}
