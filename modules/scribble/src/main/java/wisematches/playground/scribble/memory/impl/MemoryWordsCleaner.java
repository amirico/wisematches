package wisematches.playground.scribble.memory.impl;

import org.apache.log4j.Logger;
import wisematches.core.Personality;
import wisematches.playground.*;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribblePlayManager;
import wisematches.playground.scribble.memory.MemoryWordManager;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MemoryWordsCleaner {
	private MemoryWordManager memoryWordManager;
	private ScribblePlayManager scribbleBoardManager;

	private final TheGamePlayListener boardStateListener = new TheGamePlayListener();

	private static final Logger log = Logger.getLogger("wisematches.server.scribble.memory");

	public MemoryWordsCleaner() {
	}

	public void setMemoryWordManager(MemoryWordManager memoryWordManager) {
		this.memoryWordManager = memoryWordManager;
	}


	public void setScribbleBoardManager(ScribblePlayManager scribbleBoardManager) {
		if (this.scribbleBoardManager != null) {
			this.scribbleBoardManager.removeBoardListener(boardStateListener);
		}

		this.scribbleBoardManager = scribbleBoardManager;

		if (this.scribbleBoardManager != null) {
			this.scribbleBoardManager.addBoardListener(boardStateListener);
		}
	}

	private class TheGamePlayListener implements BoardListener {
		private TheGamePlayListener() {
		}

		@Override
		public void gameStarted(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board) {
		}

		@Override
		public void gameMoveDone(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameMove move, GameMoveScore moveScore) {
		}

		@Override
		public void gameFinished(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameResolution resolution, Collection<Personality> winners) {
			if (log.isDebugEnabled()) {
				log.debug("Game on board" + board.getBoardId() + " has been finished and all memory words will be cleaned.");
			}
			memoryWordManager.clearMemoryWords((ScribbleBoard) board);
		}
	}
}
