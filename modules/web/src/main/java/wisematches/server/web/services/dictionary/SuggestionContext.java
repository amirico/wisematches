package wisematches.server.web.services.dictionary;

import wisematches.core.Language;

import java.util.EnumSet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class SuggestionContext {
	private final String word;
	private final Language language;
	private final EnumSet<SuggestionType> suggestionTypes;
	private final EnumSet<SuggestionState> suggestionStates;

	public SuggestionContext(Language language, EnumSet<SuggestionType> suggestionTypes, EnumSet<SuggestionState> suggestionStates) {
		this(null, language, suggestionTypes, suggestionStates);
	}

	public SuggestionContext(String word, Language language, EnumSet<SuggestionType> suggestionTypes, EnumSet<SuggestionState> suggestionStates) {
		if (word == null && language == null && suggestionTypes == null && suggestionStates == null) {
			throw new NullPointerException("All parameters can't be null at the same time");
		}
		this.word = word;
		this.language = language;
		this.suggestionTypes = suggestionTypes;
		this.suggestionStates = suggestionStates;
	}

	public String getWord() {
		return word;
	}

	public Language getLanguage() {
		return language;
	}

	public EnumSet<SuggestionType> getSuggestionTypes() {
		return suggestionTypes;
	}

	public EnumSet<SuggestionState> getSuggestionStates() {
		return suggestionStates;
	}
}
