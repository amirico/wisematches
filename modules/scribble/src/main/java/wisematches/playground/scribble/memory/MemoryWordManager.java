package wisematches.playground.scribble.memory;

import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribblePlayerHand;
import wisematches.playground.scribble.Word;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface MemoryWordManager {
	void addMemoryWord(ScribbleBoard board, ScribblePlayerHand hand, Word word);

	void removeMemoryWord(ScribbleBoard board, ScribblePlayerHand hand, Word word);

	void clearMemoryWords(ScribbleBoard board);

	void clearMemoryWords(ScribbleBoard board, ScribblePlayerHand hand);

	int getMemoryWordsCount(ScribbleBoard board, ScribblePlayerHand hand);

	Collection<Word> getMemoryWords(ScribbleBoard board, ScribblePlayerHand hand);
}
