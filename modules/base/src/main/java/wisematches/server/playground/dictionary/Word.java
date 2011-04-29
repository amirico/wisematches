package wisematches.server.playground.dictionary;

import java.io.Serializable;
import java.util.Locale;

/**
 * Each word has a text, a description and type of the word (noun and so on).
 * <p/>
 * Two words are equals if it's text, type and locale are equals.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class Word implements Serializable {
	private final String text;
	private final String description;
	private final Locale locale;

	public Word(String text, Locale locale) {
		this(text, null, locale);
	}

	public Word(String text, String description, Locale locale) {
		if (text == null || text.length() == 0) {
			throw new IllegalArgumentException("Text can't be null or empty");
		}
		if (locale == null) {
			throw new IllegalArgumentException("Locale can't be null");
		}
		this.text = text;
		this.description = description;
		this.locale = locale;
	}

	public String getText() {
		return text;
	}

	public String getDescription() {
		return description;
	}

	public Locale getLocale() {
		return locale;
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Word word = (Word) o;
		return locale.equals(word.locale) && text.equals(word.text);
	}

	public int hashCode() {
		int result;
		result = text.hashCode();
		result = 31 * result + locale.hashCode();
		return result;
	}

	public String toString() {
		return text;
	}
}
