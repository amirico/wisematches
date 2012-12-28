package wisematches.playground.dictionary.impl.memory;

import wisematches.playground.dictionary.DictionaryModificationException;
import wisematches.playground.dictionary.IterableDictionary;
import wisematches.playground.dictionary.Word;
import wisematches.playground.dictionary.impl.AbstractExtensibleDictionary;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MemoryDictionary extends AbstractExtensibleDictionary implements IterableDictionary {
	private final Map<String, Word> words = new TreeMap<>();

	public MemoryDictionary(Locale locale) {
		super(locale);
	}

	public MemoryDictionary(Locale locale, String... words) {
		super(locale);
		for (String word : words) {
			this.words.put(word, new Word(word, locale));
		}
	}

	@Override
	public boolean addWord(Word word) throws DictionaryModificationException {
		words.put(word.getText(), word);
		return true;
	}

	@Override
	public boolean removeWord(Word word) throws DictionaryModificationException {
		words.remove(word.getText());
		return true;
	}

	@Override
	public boolean updateWord(Word oldWord, Word newWord) throws DictionaryModificationException {
		words.put(newWord.getText(), newWord);
		return true;
	}

	@Override
	public Word getWord(CharSequence chars) {
		return words.get(chars.toString());
	}

	@Override
	public Iterator<String> iterator() {
		return words.keySet().iterator();
	}


}
