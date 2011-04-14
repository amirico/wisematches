package wisematches.server.gameplaying.scribble.memory.impl;

import org.apache.log4j.Logger;
import wisematches.server.gameplaying.board.*;
import wisematches.server.gameplaying.room.board.BoardStateListener;
import wisematches.server.gameplaying.scribble.board.ScribbleBoard;
import wisematches.server.gameplaying.scribble.memory.MemoryWordManager;
import wisematches.server.gameplaying.scribble.room.ScribbleRoomManager;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MemoryWordsCleaner {
	private MemoryWordManager memoryWordManager;
	private ScribbleRoomManager scribbleRoomManager;

	private final TheBoardStateListener boardStateListener = new TheBoardStateListener();

	private static final Logger log = Logger.getLogger("wisematches.server.scribble.memory");

	public MemoryWordsCleaner() {
	}

	public void setMemoryWordManager(MemoryWordManager memoryWordManager) {
		this.memoryWordManager = memoryWordManager;
	}

	public void setScribbleRoomManager(ScribbleRoomManager scribbleRoomManager) {
		if (this.scribbleRoomManager != null) {
			this.scribbleRoomManager.getBoardManager().removeBoardStateListener(boardStateListener);
		}

		this.scribbleRoomManager = scribbleRoomManager;

		if (this.scribbleRoomManager != null) {
			this.scribbleRoomManager.getBoardManager().addBoardStateListener(boardStateListener);
		}
	}

	private class TheBoardStateListener implements BoardStateListener {
		private TheBoardStateListener() {
		}

		@Override
		public void gameStarted(GameBoard board) {
		}

		@Override
		public <S extends GameSettings, P extends GamePlayerHand> void gameMoveDone(GameBoard<S, P> board, GameMove move) {
		}

		@Override
		public <S extends GameSettings, P extends GamePlayerHand> void gameFinished(GameBoard<S, P> board, GameResolution resolution, Collection<P> wonPlayers) {
			if (log.isDebugEnabled()) {
				log.debug("Game on board" + board.getBoardId() + " has been finished and all memory words will be cleaned.");
			}
			memoryWordManager.clearMemoryWords((ScribbleBoard) board);
		}
	}
}
