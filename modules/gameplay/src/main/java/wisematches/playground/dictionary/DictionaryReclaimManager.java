package wisematches.playground.dictionary;

import wisematches.core.Language;
import wisematches.core.Personality;
import wisematches.core.search.SearchManager;

import java.util.EnumSet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface DictionaryReclaimManager extends SearchManager<WordReclaim, WordReclaimContext> {
	void addDictionaryReclaimListener(DictionaryReclaimListener listener);

	void removeDictionaryReclaimListener(DictionaryReclaimListener listener);


	void rejectReclaims(String commentary, Long... ids);

	void approveReclaims(String commentary, Long... ids);


	void updateReclaim(Long id, String definition, EnumSet<WordAttribute> attributes);


	WordReclaim addWord(Personality person, Language language, String word, String definition, EnumSet<WordAttribute> attributes);

	WordReclaim updateWord(Personality person, Language language, String word, String definition, EnumSet<WordAttribute> attributes);

	WordReclaim removeWord(Personality person, Language language, String word);
}
