package wisematches.playground.scribble.memory.impl;

import org.easymock.Capture;
import org.junit.Test;
import wisematches.playground.GamePlayListener;
import wisematches.playground.GameResolution;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribblePlayManager;
import wisematches.playground.scribble.memory.MemoryWordManager;

import static org.easymock.EasyMock.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MemoryWordsCleanerTest {
	public MemoryWordsCleanerTest() {
	}

	@Test
	@SuppressWarnings("unchecked")
	public void test() {
		final ScribbleBoard board = createMock(ScribbleBoard.class);

		final MemoryWordManager wordManager = createMock(MemoryWordManager.class);
		wordManager.clearMemoryWords(board);
		replay(wordManager);

		final Capture<GamePlayListener> capture = new Capture<GamePlayListener>();

		final ScribblePlayManager manager = createMock(ScribblePlayManager.class);
		manager.addGamePlayListener(capture(capture));
		replay(manager);

		MemoryWordsCleaner cleaner = new MemoryWordsCleaner();
		cleaner.setMemoryWordManager(wordManager);
		cleaner.setScribbleBoardManager(manager);

		capture.getValue().gameFinished(board, GameResolution.FINISHED, null);

		verify(wordManager);
	}
}
