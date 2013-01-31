package wisematches.playground.dictionary;

import java.io.Serializable;
import java.util.EnumSet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class WordDefinition implements Serializable {
	private String text;
	private EnumSet<WordAttribute> attributes;

	public WordDefinition(String text, EnumSet<WordAttribute> attributes) {
		if (text == null) {
			throw new NullPointerException("Text can't be null");
		}
		if (attributes == null) {
			throw new NullPointerException("Attributes can't be null");
		}
		this.text = text;
		this.attributes = attributes;
	}

	public String getText() {
		return text;
	}

	public EnumSet<WordAttribute> getAttributes() {
		return attributes;
	}

	@Override
	public String toString() {
		return "WordDefinition{" +
				"text='" + text + '\'' +
				", attributes=" + attributes +
				'}';
	}
}
