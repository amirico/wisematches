package wisematches.server.gameplaying.dictionary.impl.file;

import wisematches.server.gameplaying.dictionary.DictionaryModificationException;
import wisematches.server.gameplaying.dictionary.IterableDictionary;
import wisematches.server.gameplaying.dictionary.Word;
import wisematches.server.gameplaying.dictionary.impl.AbstractExtensibleDictionary;

import java.io.*;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FileDictionary extends AbstractExtensibleDictionary implements IterableDictionary {
	private final File source;
	private final Map<String, Word> words;

	private final Lock lockChanges = new ReentrantLock();

	public FileDictionary(Locale locale, File source) {
		super(locale, source.getAbsolutePath());
		this.source = source;

		words = new TreeMap<String, Word>();
		loadDictionary();
	}

	private void loadDictionary() {
		try {
			final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(source), "UTF-8"));
			String word = bufferedReader.readLine();
			while (word != null) {
				words.put(word.toLowerCase(), null);
				word = bufferedReader.readLine();
			}
			bufferedReader.close();
		} catch (IOException ex) {
			throw new IllegalStateException("Dictionary can't be loaded", ex);
		}
	}

	public Word getWord(CharSequence chars) {
		String s = chars.toString().toLowerCase();
		Word word = words.get(s);
		if (word == null && words.containsKey(s)) {
			Word word1 = new Word(s, locale);
			words.put(s, word1);
			return word1;
		}
		return word;
	}

	public boolean addWord(Word word) throws DictionaryModificationException {
		if (addWordImpl(word)) {
			fireWordAdded(word);
			return true;
		}
		return false;
	}

	public boolean removeWord(Word word) throws DictionaryModificationException {
		if (removeWordImpl(word)) {
			fireWordRemoved(word);
			return true;
		}
		return false;
	}

	public boolean updateWord(Word oldWord, Word newWord) throws DictionaryModificationException {
		lockChanges.lock();
		try {
			if (removeWordImpl(oldWord)) {
				if (addWordImpl(newWord)) {
					fireWordUpdated(oldWord, newWord);
					return true;
				}
			}
			return false;
		} finally {
			lockChanges.unlock();
		}
	}

	public Iterator<String> iterator() {
		return words.keySet().iterator();
	}


	private boolean addWordImpl(Word word) throws DictionaryModificationException {
		lockChanges.lock();
		try {
			checkWord(word);
			if (words.containsKey(word.getText())) {
				return false;
			}

			try {
				PrintWriter w = new PrintWriter(new FileOutputStream(source, true));
				w.print("\n");
				w.print(word.getText());
				w.flush();
				w.close();
				words.put(word.getText(), word);
			} catch (IOException ex) {
				throw new DictionaryModificationException(ex);
			}
			return true;
		} finally {
			lockChanges.unlock();
		}
	}

	private boolean removeWordImpl(Word word) throws DictionaryModificationException {
		lockChanges.lock();
		try {
			checkWord(word);
			String s = word.getText();
			if (words.remove(s) != null) {
				try {
					return removeLineFromFile(s);
				} catch (IOException ex) {
					words.put(s, word);
					throw new DictionaryModificationException(ex);
				}
			}
			return false;
		} finally {
			lockChanges.unlock();
		}
	}

	private void checkWord(Word word) {
		if (word == null) {
			throw new IllegalArgumentException("Word can't be null");
		}

		if (!word.getLocale().equals(locale)) {
			throw new IllegalArgumentException("Word has incorrect locale. Requred " + locale +
					" but word's " + word.getLocale());
		}
	}

	private boolean removeLineFromFile(String lineToRemove) throws IOException {
		final File tempFile = File.createTempFile("fileDictionary", source.getName());

		final PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
		final BufferedReader br = new BufferedReader(new FileReader(source));

		boolean wasRemoved = false;
		String line;
		while ((line = br.readLine()) != null) {
			if (!line.trim().equals(lineToRemove)) {
				pw.print(line);
				pw.print("\n");
			} else {
				wasRemoved = true;
			}
		}
		pw.flush();
		pw.close();
		br.close();

		if (!source.delete()) {
			throw new IOException("Could not delete original file");
		}
		if (!tempFile.renameTo(source)) {
			throw new IOException("Could not copy temp file to original file");
		}
		return wasRemoved;
	}
}