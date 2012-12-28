package wisematches.playground.vocabulary.impl;

import wisematches.personality.Language;
import wisematches.playground.vocabulary.Vocabulary;
import wisematches.playground.vocabulary.VocabularyManager;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FileVocabularyManager implements VocabularyManager {
	private final Map<String, Vocabulary> vocabularyById = new HashMap<>();
	private final Map<Language, Collection<Vocabulary>> vocabularyByLanguage = new HashMap<>();

	public FileVocabularyManager() {
	}

	@Override
	public Vocabulary getVocabulary(String id) {
		return vocabularyById.get(id);
	}

	@Override
	public Collection<Vocabulary> getVocabularies(Language language) {
		final Collection<Vocabulary> vocabularies = vocabularyByLanguage.get(language);
		if (vocabularies == null) {
			return Collections.emptyList();
		}
		return vocabularies;
	}

	public void setSearchFolder(File searchFolder) throws IOException {
		loadVocabularies(searchFolder);
	}

	private void loadVocabularies(File file) throws IOException {
		if (file.isFile()) {
			final FileVocabulary vocabulary = new FileVocabulary(file);
			vocabularyById.put(vocabulary.getId(), vocabulary);

			final Language lang = vocabulary.getLanguage();
			Collection<Vocabulary> vocabularies = vocabularyByLanguage.get(lang);
			if (vocabularies == null) {
				vocabularies = new ArrayList<>();
				vocabularyByLanguage.put(lang, vocabularies);
			}
			vocabularies.add(vocabulary);
		} else {
			for (File folder : file.listFiles()) {
				loadVocabularies(folder);
			}
		}
	}
}
