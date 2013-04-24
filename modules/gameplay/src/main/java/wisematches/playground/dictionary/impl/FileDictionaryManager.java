package wisematches.playground.dictionary.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wisematches.core.Language;
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.dictionary.DictionaryException;
import wisematches.playground.dictionary.DictionaryManager;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FileDictionaryManager implements DictionaryManager {
	private final Map<Language, Dictionary> dictionaries = new HashMap<>();

	private static final Logger log = LoggerFactory.getLogger("wisematches.dictionary.FileDictionaryManager");

	public FileDictionaryManager() {
	}

	@Override
	public Collection<Language> getLanguages() {
		return Collections.unmodifiableCollection(dictionaries.keySet());
	}

	@Override
	public Dictionary getDictionary(Language language) {
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
			final String name = f.getName();
			if (name.endsWith(".dic")) {
				Language lang = Language.valueOf(name.substring(0, name.length() - 4).toUpperCase());
				try {
					dictionaries.put(lang, new FileDictionary(lang, f, false));
				} catch (DictionaryException ex) {
					log.error("Dictionary for language {} can't be loaded from {}", lang, file.getAbsolutePath(), ex);
					throw ex;
				}
			}
		}
		if (dictionaries.size() == 0) {
			throw new DictionaryException("No dictionaries in folder: " + file.getAbsolutePath());
		}
	}
}