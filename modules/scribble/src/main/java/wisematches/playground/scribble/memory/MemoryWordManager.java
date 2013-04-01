package wisematches.playground.scribble.memory;

import wisematches.core.Player;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.Word;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface MemoryWordManager {
	int getMemoryWordsCount(ScribbleBoard board, Player player);

	Collection<Word> getMemoryWords(ScribbleBoard board, Player player);


	void addMemoryWord(ScribbleBoard board, Player player, Word word);

	void removeMemoryWord(ScribbleBoard board, Player player, Word word);


	void clearMemoryWords(ScribbleBoard board);

	void clearMemoryWords(ScribbleBoard board, Player player);
}
