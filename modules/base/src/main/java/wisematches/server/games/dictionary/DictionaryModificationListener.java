package wisematches.server.games.dictionary;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface DictionaryModificationListener {
	void wordAdded(Word word);

	void wordRemoved(Word word);

	void wordUpdated(Word oldWord, Word newWord);
}
