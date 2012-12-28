package wisematches.playground.vocabulary.impl;

import wisematches.personality.Language;
import wisematches.playground.vocabulary.Vocabulary;
import wisematches.playground.vocabulary.Word;
import wisematches.playground.vocabulary.WordGender;

import java.io.*;
import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class FileVocabulary implements Vocabulary {
	private final String id;
	private final String name;
	private final Language language;
	private final String description;
	private final Date modificationDate;

	private final Map<String, Word> wordMap = new TreeMap<>();

	public FileVocabulary(File file) throws IOException {
		id = file.getName();

		final BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		language = Language.valueOf(r.readLine().toUpperCase());
		name = r.readLine();

		String tmp;

		final StringBuilder b = new StringBuilder();
		tmp = r.readLine();
		while (tmp.trim().length() != 0) {
			b.append(tmp);
			b.append(System.getProperty("line.separator"));
			tmp = r.readLine();
		}
		description = b.toString();

		tmp = r.readLine();
		while (tmp != null) {
			final String name = tmp.substring(0, tmp.indexOf("|")).toLowerCase();
			if (name.length() != 0) { // TODO: broken word
				final WordGender gender = WordGender.decode(tmp.charAt(name.length() + 1));
				final String description = tmp.substring(name.length() + 3);

				wordMap.put(name, new Word(name, gender, description));
			}
			tmp = r.readLine();
		}
		r.close();

		modificationDate = new Date(file.lastModified());
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Language getLanguage() {
		return language;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Date getModificationDate() {
		return modificationDate;
	}

	@Override
	public int getSize() {
		return wordMap.size();
	}

	@Override
	public Word getWord(String name) {
		return wordMap.get(name.toLowerCase());
	}

	@Override
	public Collection<Word> searchWords(String prefix) {
		boolean found = false;

		prefix = prefix.toLowerCase();
		final Collection<Word> res = new ArrayList<>();
		for (Word word : wordMap.values()) {
			if (word.getText().startsWith(prefix)) {
				res.add(word);
				found = true;
			} else if (found) { // we already found. Nothing to do more.
				break;
			}
		}
		return res;
	}

	@Override
	public Iterator<Word> iterator() {
		return wordMap.values().iterator();
	}
}
