package wisematches.playground.dictionary.impl;

import wisematches.playground.dictionary.DictionaryModificationListener;
import wisematches.playground.dictionary.ExtensibleDictionary;
import wisematches.playground.dictionary.Word;

import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class AbstractExtensibleDictionary extends AbstractDictionary implements ExtensibleDictionary {
	private final Collection<DictionaryModificationListener> listeners =
			new CopyOnWriteArraySet<DictionaryModificationListener>();

	public AbstractExtensibleDictionary(Locale locale) {
		super(locale);
	}

	public AbstractExtensibleDictionary(Locale locale, String source) {
		super(locale, source);
	}

	public void addDictionaryModificationListener(DictionaryModificationListener l) {
		listeners.add(l);
	}

	public void removeDictionaryModificationListener(DictionaryModificationListener l) {
		listeners.remove(l);
	}

	protected void fireWordAdded(Word word) {
		for (DictionaryModificationListener listener : listeners) {
			listener.wordAdded(word);
		}
	}

	protected void fireWordRemoved(Word word) {
		for (DictionaryModificationListener listener : listeners) {
			listener.wordAdded(word);
		}
	}

	protected void fireWordUpdated(Word oldWord, Word newWord) {
		for (DictionaryModificationListener listener : listeners) {
			listener.wordUpdated(oldWord, newWord);
		}
	}
}
