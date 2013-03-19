package wisematches.server.services.dictionary;

import wisematches.core.Language;
import wisematches.playground.dictionary.WordAttribute;

import java.util.Date;
import java.util.EnumSet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface ChangeSuggestion {
	long getId();

	String getWord();

	long getRequester();

	Language getLanguage();

	Date getRequestDate();

	String getDefinition();

	EnumSet<WordAttribute> getAttributes();

	String getCommentary();

	Date getResolutionDate();

	SuggestionType getSuggestionType();

	SuggestionState getSuggestionState();
}
