package wisematches.server.services.dictionary;

import wisematches.core.Language;
import wisematches.core.Personality;
import wisematches.core.search.SearchManager;
import wisematches.playground.dictionary.WordAttribute;

import java.util.EnumSet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface DictionarySuggestionManager extends SearchManager<WordSuggestion, SuggestionContext> {
	void addDictionarySuggestionListener(DictionarySuggestionListener listener);

	void removeDictionarySuggestionListener(DictionarySuggestionListener listener);


	void rejectRequests(String commentary, Long... ids);

	void approveRequests(String commentary, Long... ids);


	void updateRequest(Long id, String definition, EnumSet<WordAttribute> attributes);


	WordSuggestion addWord(Personality person, Language language, String word, String definition, EnumSet<WordAttribute> attributes);

	WordSuggestion updateWord(Personality person, Language language, String word, String definition, EnumSet<WordAttribute> attributes);

	WordSuggestion removeWord(Personality person, Language language, String word);
}
