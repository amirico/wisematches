package wisematches.server.web.servlet.sdo.dictionary;

import wisematches.core.Language;
import wisematches.core.Personality;
import wisematches.playground.GameMessageSource;
import wisematches.playground.dictionary.WordAttribute;
import wisematches.server.services.dictionary.SuggestionState;
import wisematches.server.services.dictionary.SuggestionType;
import wisematches.server.services.dictionary.WordSuggestion;
import wisematches.server.services.state.PlayerStateManager;
import wisematches.server.web.servlet.sdo.InternationalisedInfo;
import wisematches.server.web.servlet.sdo.person.PersonalityInfo;
import wisematches.server.web.servlet.sdo.time.TimeInfo;

import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WordSuggestionInfo extends InternationalisedInfo {
    private final Personality requestor;
    private final WordSuggestion suggestion;
    private final PlayerStateManager stateManager;

    public WordSuggestionInfo(Personality requestor, WordSuggestion suggestion, PlayerStateManager stateManager, GameMessageSource messageSource, Locale locale) {
        super(messageSource, locale);
        this.requestor = requestor;
        this.suggestion = suggestion;
        this.stateManager = stateManager;
    }

    public String getWord() {
        return suggestion.getWord();
    }

    public PersonalityInfo getRequester() {
        return new PersonalityInfo(messageSource.getPersonalityNick(requestor, locale), requestor, stateManager);
    }

    public Language getLanguage() {
        return suggestion.getLanguage();
    }

    public TimeInfo getRequestDate() {
        return new TimeInfo(suggestion.getRequestDate(), messageSource, locale);
    }

    public String getDefinition() {
        return suggestion.getDefinition();
    }

    public String getCommentary() {
        return suggestion.getCommentary();
    }

    public TimeInfo getResolutionDate() {
        final Date resolutionDate = suggestion.getResolutionDate();
        if (resolutionDate != null) {
            return new TimeInfo(resolutionDate, messageSource, locale);
        }
        return null;
    }

    public SuggestionType getSuggestionType() {
        return suggestion.getSuggestionType();
    }

    public SuggestionState getSuggestionState() {
        return suggestion.getSuggestionState();
    }

    public EnumSet<WordAttribute> getAttributes() {
        return suggestion.getAttributes();
    }
}
