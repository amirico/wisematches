package wisematches.server.services.dictionary;

import wisematches.core.Language;
import wisematches.core.Personality;
import wisematches.core.search.SearchManager;
import wisematches.playground.dictionary.WordAttribute;

import java.util.EnumSet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface DictionarySuggestionManager extends SearchManager<ChangeSuggestion, SuggestionContext> {
	void addDictionaryChangeListener(DictionarySuggestionListener listener);

	void removeDictionaryChangeListener(DictionarySuggestionListener listener);


	void rejectRequests(String commentary, Long... ids);

	void approveRequests(String commentary, Long... ids);


	void updateRequest(Long id, String definition, EnumSet<WordAttribute> attributes);


	ChangeSuggestion addWord(Personality person, Language language, String word, String definition, EnumSet<WordAttribute> attributes);

	ChangeSuggestion updateWord(Personality person, Language language, String word, String definition, EnumSet<WordAttribute> attributes);

	ChangeSuggestion removeWord(Personality person, Language language, String word);
}
