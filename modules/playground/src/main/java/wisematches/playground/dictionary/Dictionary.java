package wisematches.playground.dictionary;

import wisematches.core.Alphabet;

import java.util.Collection;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface Dictionary {
	void addWordEntry(WordEntry entry) throws DictionaryException;

	void updateWordEntry(WordEntry entry) throws DictionaryException;

	void removeWordEntry(WordEntry entry) throws DictionaryException;


	Alphabet getAlphabet();

	Alphabet getAlphabet(char ch);


	Date getLastModification();


	boolean contains(String word);


	WordEntry getWordEntry(String word);

	Collection<WordEntry> getWordEntries();

	Collection<WordEntry> getWordEntries(String prefix);


	void flush() throws DictionaryException;
}