package wisematches.server.web.services.dictionary;

import wisematches.personality.Language;
import wisematches.personality.Personality;
import wisematches.playground.dictionary.WordAttribute;
import wisematches.playground.search.SearchFilter;
import wisematches.playground.search.SearchManager;

import java.util.EnumSet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface DictionarySuggestionManager extends SearchManager<ChangeSuggestion, SuggestionContext, SearchFilter.NoFilter> {
    void addDictionaryChangeListener(DictionarySuggestionListener listener);

    void removeDictionaryChangeListener(DictionarySuggestionListener listener);


    void rejectRequests(long... ids);

    void approveRequests(long... ids);


    ChangeSuggestion addWord(String word, String definition, EnumSet<WordAttribute> attributes, Language language, Personality person);

    ChangeSuggestion updateWord(String word, String definition, EnumSet<WordAttribute> attributes, Language language, Personality person);

    ChangeSuggestion removeWord(String word, Language language, Personality person);
}
