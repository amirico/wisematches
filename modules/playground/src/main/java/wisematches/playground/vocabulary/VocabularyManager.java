package wisematches.playground.vocabulary;

import wisematches.personality.Language;

import java.util.Collection;

/**
 * {@code VocabularyManager} provides access to vocabularies.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface VocabularyManager {
	/**
	 * Returns vocabulary by it's ID.
	 *
	 * @param id the vocabulary's id.
	 * @return vocabulary by specified id.
	 * @throws IllegalArgumentException if there is no vocabulary with specified id.
	 */
	Vocabulary getVocabulary(String id);

	/**
	 * Returns collection of all known vocabularies for specified language.
	 *
	 * @param language language.
	 * @return collection of all known vocabularies.
	 */
	Collection<Vocabulary> getVocabularies(Language language);
}
