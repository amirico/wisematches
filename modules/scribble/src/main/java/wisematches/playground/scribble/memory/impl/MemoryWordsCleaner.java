package wisematches.playground.scribble.memory.impl;

import org.apache.log4j.Logger;
import wisematches.playground.*;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribbleBoardManager;
import wisematches.playground.scribble.memory.MemoryWordManager;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MemoryWordsCleaner {
	private MemoryWordManager memoryWordManager;
	private ScribbleBoardManager scribbleBoardManager;

	private final TheBoardStateListener boardStateListener = new TheBoardStateListener();

	private static final Logger log = Logger.getLogger("wisematches.server.scribble.memory");

	public MemoryWordsCleaner() {
	}

	public void setMemoryWordManager(MemoryWordManager memoryWordManager) {
		this.memoryWordManager = memoryWordManager;
	}


	public void setScribbleBoardManager(ScribbleBoardManager scribbleBoardManager) {
		if (this.scribbleBoardManager != null) {
			this.scribbleBoardManager.removeBoardStateListener(boardStateListener);
		}

		this.scribbleBoardManager = scribbleBoardManager;

		if (this.scribbleBoardManager != null) {
			this.scribbleBoardManager.addBoardStateListener(boardStateListener);
		}
	}

	private class TheBoardStateListener implements BoardStateListener {
		private TheBoardStateListener() {
		}

		@Override
		public void gameStarted(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board) {
		}

		@Override
		public void gameMoveDone(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameMove move) {
		}

		@Override
		public void gameFinished(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameResolution resolution, Collection<? extends GamePlayerHand> wonPlayers) {
			if (log.isDebugEnabled()) {
				log.debug("Game on board" + board.getBoardId() + " has been finished and all memory words will be cleaned.");
			}
			memoryWordManager.clearMemoryWords((ScribbleBoard) board);
		}
	}
}
