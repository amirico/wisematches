package wisematches.playground.dictionary;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface Dictionary {
    void addWordEntry(WordEntry entry) throws DictionaryException;

    void updateWordEntry(WordEntry entry) throws DictionaryException;

    void removeWordEntry(WordEntry entry) throws DictionaryException;


    void addVocabularyWord(Vocabulary vocabulary, String word) throws DictionaryException;

    void removeVocabularyWord(Vocabulary vocabulary, String word) throws DictionaryException;


//    boolean containsWord(String word);

    WordEntry getWordEntry(String word);

    Collection<WordEntry> getWordEntries();

    Collection<WordEntry> getWordEntries(String prefix);


    Vocabulary getDefaultVocabulary();

    Vocabulary getVocabulary(String code);

    Collection<Vocabulary> getVocabularies();
}