package wisematches.server.core.words.dict;

import wisematches.server.core.words.dict.Word;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface DictionaryModificationListener {
    void wordAdded(Word word);

    void wordRemoved(Word word);

    void wordUpdated(Word oldWord, Word newWord); 
}
