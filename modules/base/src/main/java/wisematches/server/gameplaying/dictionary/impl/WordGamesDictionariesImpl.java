package wisematches.server.gameplaying.dictionary.impl;

import wisematches.server.gameplaying.dictionary.*;
import wisematches.server.gameplaying.dictionary.impl.file.FileDictionaryManager;

import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WordGamesDictionariesImpl implements WordGamesDictionaries {
	private DictionaryManager lexicalManager;
	private FileDictionaryManager slanglyManager;

	public Dictionary getDictionary(Locale locale, boolean allowSlangWords) throws DictionaryNotFoundException {
		return new TheDictionary(locale, allowSlangWords);
	}

	public boolean addSlanglyWord(Word word) throws DictionaryModificationException {
		return getSlanglyDictionary(word).addWord(word);
	}

	public boolean removeSlanglyWord(Word word) throws DictionaryModificationException {
		return getSlanglyDictionary(word).removeWord(word);
	}

	public boolean approveSlanglyWord(Word word) throws DictionaryModificationException {
		Dictionary dictionary = lexicalManager.getDictionary(word.getLocale());
		if (dictionary instanceof ExtensibleDictionary) {
			ExtensibleDictionary ed = (ExtensibleDictionary) dictionary;
			if (getSlanglyDictionary(word).removeWord(word)) {
				return ed.addWord(word);
			}
		}
		return false;
	}

	public boolean doubtSlanglyWord(Word word) throws DictionaryModificationException {
		Dictionary dictionary = lexicalManager.getDictionary(word.getLocale());
		if (dictionary instanceof ExtensibleDictionary) {
			ExtensibleDictionary ed = (ExtensibleDictionary) dictionary;
			if (ed.removeWord(word)) {
				return getSlanglyDictionary(word).addWord(word);
			}
		}
		return false;
	}

	public Iterator<String> iterateSlanglyWords(Locale locale) throws DictionaryNotFoundException {
		final IterableDictionary dictionary = (IterableDictionary) slanglyManager.getDictionary(locale);
		return dictionary.iterator();
	}

	public Dictionary getDictionary(Locale locale) throws DictionaryNotFoundException {
		return getDictionary(locale, true);
	}

	private ExtensibleDictionary getSlanglyDictionary(Word word) throws DictionaryNotFoundException {
		return (ExtensibleDictionary) slanglyManager.getDictionary(word.getLocale());
	}

	public DictionaryManager getLexicalManager() {
		return lexicalManager;
	}

	public void setLexicalManager(DictionaryManager lexicalManager) {
		this.lexicalManager = lexicalManager;
	}

	public FileDictionaryManager getSlanglyManager() {
		return slanglyManager;
	}

	public void setSlanglyManager(FileDictionaryManager slanglyManager) {
		if (!slanglyManager.isCreateEmptyDictionary()) {
			throw new IllegalArgumentException("Manager doesn't support creating empty dictionaries");
		}
		this.slanglyManager = slanglyManager;
	}

	private final class TheDictionary extends AbstractDictionary implements IterableDictionary {
		private final Dictionary lexical;
		private final IterableDictionary slangly;
		private final boolean allowSlangWords;

		public TheDictionary(Locale locale, boolean allowSlangWords) throws DictionaryNotFoundException {
			super(locale, "WordGamesDictionary");
			this.allowSlangWords = allowSlangWords;
			lexical = lexicalManager.getDictionary(locale);
			slangly = slanglyManager.getDictionary(locale);
		}

		public Word getWord(CharSequence chars) {
			Word word = lexical.getWord(chars);
			if (word == null && allowSlangWords) {
				word = slangly.getWord(chars);
			}
			return word;
		}

		public Iterator<String> iterator() {
			return new TheIterator(this);
		}
	}

	private static class TheIterator implements Iterator<String> {
		private final TheDictionary dictionary;

		private Iterator<String> lexicalIterator;
		private Iterator<String> slanglyIterator;

		private TheIterator(TheDictionary dictionary) {
			this.dictionary = dictionary;

			if (dictionary.lexical instanceof IterableDictionary) {
				lexicalIterator = ((IterableDictionary) dictionary.lexical).iterator();
			}
			slanglyIterator = dictionary.slangly.iterator();
		}

		public boolean hasNext() {
			if (lexicalIterator != null && !lexicalIterator.hasNext()) {
				lexicalIterator = null;
			}
			if (slanglyIterator != null && !slanglyIterator.hasNext()) {
				slanglyIterator = null;
			}
			return lexicalIterator != null || slanglyIterator != null;
		}

		public String next() {
			if (lexicalIterator != null) {
				return lexicalIterator.next();
			} else if (slanglyIterator != null) {
				return slanglyIterator.next();
			} else {
				throw new NoSuchElementException();
			}
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}