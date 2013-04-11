package wisematches.server.services.dictionary;

import wisematches.core.Language;
import wisematches.playground.dictionary.WordAttribute;

import java.util.Date;
import java.util.EnumSet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface WordSuggestion {
	long getId();

	String getWord();

	long getRequester();

	Language getLanguage();

	Date getRequestDate();

	String getDefinition();

	String getCommentary();

	Date getResolutionDate();

	SuggestionType getSuggestionType();

	SuggestionState getSuggestionState();

	EnumSet<WordAttribute> getAttributes();
}
