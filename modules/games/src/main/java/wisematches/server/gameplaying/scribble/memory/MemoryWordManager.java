package wisematches.server.gameplaying.scribble.memory;

import wisematches.server.gameplaying.scribble.Word;
import wisematches.server.gameplaying.scribble.board.ScribbleBoard;
import wisematches.server.gameplaying.scribble.board.ScribblePlayerHand;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface MemoryWordManager {
	void addMemoryWord(ScribbleBoard board, ScribblePlayerHand hand, Word word);

	void removeMemoryWord(ScribbleBoard board, ScribblePlayerHand hand, Word word);

	void clearMemoryWords(ScribbleBoard board, ScribblePlayerHand hand);

	Collection<Word> getMemoryWords(ScribbleBoard board, ScribblePlayerHand hand);
}
