package wisematches.server.services.dictionary;

import wisematches.core.Language;

import java.util.Date;
import java.util.EnumSet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class SuggestionContext {
	private final String word;
	private final Language language;
	private final EnumSet<SuggestionType> suggestionTypes;
	private final EnumSet<SuggestionState> suggestionStates;
	private final Date resolvedAfter;

	public SuggestionContext(Language language, EnumSet<SuggestionType> suggestionTypes, EnumSet<SuggestionState> suggestionStates, Date resolvedAfter) {
		this(null, language, suggestionTypes, suggestionStates, resolvedAfter);
	}

	public SuggestionContext(String word, Language language, EnumSet<SuggestionType> suggestionTypes, EnumSet<SuggestionState> suggestionStates, Date resolvedAfter) {
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

	public EnumSet<SuggestionType> getSuggestionTypes() {
		return suggestionTypes;
	}

	public EnumSet<SuggestionState> getSuggestionStates() {
		return suggestionStates;
	}
}
