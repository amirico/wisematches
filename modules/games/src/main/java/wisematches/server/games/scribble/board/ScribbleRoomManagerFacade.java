package wisematches.server.games.scribble.board;

import wisematches.server.core.board.GameMoveListener;
import wisematches.server.core.board.GamePlayersListener;
import wisematches.server.core.board.GameStateListener;
import wisematches.server.core.room.BoardLoadingException;
import wisematches.server.core.room.RoomManagerFacade;

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
