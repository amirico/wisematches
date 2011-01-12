package wisematches.server.games.dictionary.impl.file;

import wisematches.server.games.dictionary.DictionaryManager;
import wisematches.server.games.dictionary.DictionaryNotFoundException;
import wisematches.server.games.dictionary.IterableDictionary;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FileDictionaryManager implements DictionaryManager {
	private File dictionariesFolder;
	private String filePrefix = "dictionary";
	private String filePostfix = "dic";

	private boolean createEmptyDictionary = false;

	private final Map<Locale, IterableDictionary> dictionaryMap = new HashMap<Locale, IterableDictionary>();

	public FileDictionaryManager() {
	}

	public FileDictionaryManager(File dictionariesFolder) {
		this.dictionariesFolder = dictionariesFolder;
	}

	public IterableDictionary getDictionary(Locale locale) throws DictionaryNotFoundException {
		IterableDictionary dictionary = dictionaryMap.get(locale);
		if (dictionary == null) {
			File df = getDictionaryFile(locale);
			if (!df.exists()) {
				if (createEmptyDictionary) {
					createDictionary(df);
				} else {
					throw new DictionaryNotFoundException("Dictionary for locale " + locale + " not found. " +
							"File doesn't exist: " + df.getAbsolutePath());
				}
			}
			dictionary = new FileDictionary(locale, df);
			dictionaryMap.put(locale, dictionary);
		}
		return dictionary;
	}

	private void createDictionary(File df) throws DictionaryNotFoundException {
		try {
			if (!df.getParentFile().exists() && !df.getParentFile().mkdirs()) {
				throw new DictionaryNotFoundException("Dictionary directories can't be created");
			}
			if (!df.createNewFile()) {
				throw new DictionaryNotFoundException("Dictionary file can't be created by unknown error");
			}
		} catch (IOException e) {
			throw new DictionaryNotFoundException("Empty dictionary can't be created", e);
		}
	}

	private File getDictionaryFile(Locale locale) {
		return new File(dictionariesFolder, filePrefix + "_" + locale.toString() + "." + filePostfix);
	}

	public File getDictionariesFolder() {
		return dictionariesFolder;
	}

	public void setDictionariesFolder(File dictionariesFolder) {
		this.dictionariesFolder = dictionariesFolder;
		validateManager();
	}

	public String getFilePrefix() {
		return filePrefix;
	}

	public void setFilePrefix(String filePrefix) {
		this.filePrefix = filePrefix;
		validateManager();
	}

	public String getFilePostfix() {
		return filePostfix;
	}

	public void setFilePostfix(String filePostfix) {
		this.filePostfix = filePostfix;
		validateManager();
	}

	private void validateManager() {
		dictionaryMap.clear();
	}

	public boolean isCreateEmptyDictionary() {
		return createEmptyDictionary;
	}

	public void setCreateEmptyDictionary(boolean createEmptyDictionary) {
		this.createEmptyDictionary = createEmptyDictionary;
	}
}