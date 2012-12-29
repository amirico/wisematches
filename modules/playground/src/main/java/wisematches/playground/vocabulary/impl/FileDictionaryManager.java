package wisematches.playground.vocabulary.impl;

import wisematches.personality.Language;
import wisematches.playground.vocabulary.Dictionary;
import wisematches.playground.vocabulary.DictionaryManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FileDictionaryManager implements DictionaryManager {
	private final Map<Language, Dictionary> dictionaries = new HashMap<>();

	public FileDictionaryManager() {
	}

	@Override
	public Dictionary getDictionary(Language language) {
		if (!dictionaries.containsKey(language)) {
			throw new IllegalArgumentException("Unsupported language: " + language);
		}
		return dictionaries.get(language);
	}

	public void setDictionariesFolder(File file) throws IOException, DictionaryException {
		if (file == null) {
			throw new NullPointerException("Folder can't be null");
		}
		final File[] files = file.listFiles();
		if (!file.isDirectory() || files == null) {
			throw new IllegalArgumentException("File is not dictionary");
		}
		dictionaries.clear();
		for (File f : files) {
			Language lang = Language.valueOf(f.getName().toUpperCase());
			dictionaries.put(lang, new FileDictionary(f));
		}
	}
}