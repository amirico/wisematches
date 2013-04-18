package wisematches.playground.dictionary;

import java.io.Serializable;
import java.util.EnumSet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class WordEntry implements Serializable, Comparable<WordEntry> {
	private final String word;
	private final String definition;
	private final EnumSet<WordAttribute> attributes;

	private static final EnumSet<WordAttribute> EMPTY_ATTRIBUTES = EnumSet.noneOf(WordAttribute.class);

	public WordEntry(String word) {
		this(word, "", EMPTY_ATTRIBUTES);
	}

	public WordEntry(String word, String definition, EnumSet<WordAttribute> attributes) {
		if (word == null) {
			throw new NullPointerException("Word can't be null");
		}
		if (word.length() < 2) {
			throw new IllegalArgumentException("Word length can't be less that 2 letters: " + word);
		}
		if (definition == null) {
			throw new NullPointerException("Text can't be null");
		}
		if (attributes == null) {
			throw new NullPointerException("Attributes can't be null");
		}

		this.word = word.intern();
		this.definition = definition;
		this.attributes = attributes;
	}

	public String getWord() {
		return word;
	}

	public String getDefinition() {
		return definition;
	}

	public EnumSet<WordAttribute> getAttributes() {
		return attributes;
	}

	@Override
	public int compareTo(WordEntry o) {
		return word.compareTo(o.word);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("WordEntry{");
		sb.append("word='").append(word).append('\'');
		sb.append(", definition='").append(definition).append('\'');
		sb.append(", attributes=").append(attributes);
		sb.append('}');
		return sb.toString();
	}
}
