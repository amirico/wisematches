package wisematches.server.gameplaying.scribble.room;

import wisematches.server.gameplaying.board.GameMoveListener;
import wisematches.server.gameplaying.board.GamePlayersListener;
import wisematches.server.gameplaying.board.GameStateListener;
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

	public void addGamePlayersListener(GamePlayersListener listener) {
		scribbleRoomManager.addGamePlayersListener(listener);
	}

	public void removeGamePlayersListener(GamePlayersListener listener) {
		scribbleRoomManager.removeGamePlayersListener(listener);
	}

	public void addGameMoveListener(GameMoveListener listener) {
		scribbleRoomManager.addGameMoveListener(listener);
	}

	public void removeGameMoveListener(GameMoveListener listener) {
		scribbleRoomManager.removeGameMoveListener(listener);
	}

	public void addGameStateListener(GameStateListener listener) {
		scribbleRoomManager.addGameStateListener(listener);
	}

	public void removeGameStateListener(GameStateListener listener) {
		scribbleRoomManager.removeGameStateListener(listener);
	}

	public ScribbleBoard openBoard(long gameId) throws BoardLoadingException {
		return scribbleRoomManager.openBoard(gameId);
	}

	public ScribbleRoomManager getRoomManager() {
		return scribbleRoomManager;
	}
}
