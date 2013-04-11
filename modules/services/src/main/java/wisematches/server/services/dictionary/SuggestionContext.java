package wisematches.server.services.dictionary;

import wisematches.core.Language;

import java.util.Date;
import java.util.Set;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class SuggestionContext {
    private final String word;
    private final Language language;
    private final Set<SuggestionType> suggestionTypes;
    private final Set<SuggestionState> suggestionStates;
    private final Date resolvedAfter;

    public SuggestionContext(Language language, Set<SuggestionType> suggestionTypes, Set<SuggestionState> suggestionStates, Date resolvedAfter) {
        this(null, language, suggestionTypes, suggestionStates, resolvedAfter);
    }

    public SuggestionContext(String word, Language language, Set<SuggestionType> suggestionTypes, Set<SuggestionState> suggestionStates, Date resolvedAfter) {
        if (word == null && language == null && suggestionTypes == null && suggestionStates == null) {
            throw new NullPointerException("All parameters can't be null at the same time");
        }
        this.word = word;
        this.language = language;
        this.suggestionTypes = suggestionTypes;
        this.suggestionStates = suggestionStates;
        this.resolvedAfter = resolvedAfter;
    }

    public String getWord() {
        return word;
    }

    public Language getLanguage() {
        return language;
    }

    public Date getResolvedAfter() {
        return resolvedAfter;
    }

    public Set<SuggestionType> getSuggestionTypes() {
        return suggestionTypes;
    }

    public Set<SuggestionState> getSuggestionStates() {
        return suggestionStates;
    }
}
