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


	void rejectRequests(long... ids);

	void approveRequests(long... ids);


	ChangeSuggestion addWord(String word, String definition, EnumSet<WordAttribute> attributes, Language language, Personality person);

	ChangeSuggestion updateWord(String word, String definition, EnumSet<WordAttribute> attributes, Language language, Personality person);

	ChangeSuggestion removeWord(String word, Language language, Personality person);
}
