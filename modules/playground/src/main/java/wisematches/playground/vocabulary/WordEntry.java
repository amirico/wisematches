package wisematches.playground.vocabulary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class WordEntry implements Serializable {
	private final String text;
	private final Collection<WordDefinition> explanationCases = new ArrayList<>();

	public WordEntry(String text, Collection<WordDefinition> explanationCases) {
		this.text = text;
		this.explanationCases.addAll(explanationCases);
	}

	public String getWord() {
		return text;
	}

	public Collection<WordDefinition> getDefinitions() {
		return explanationCases;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("DefaultWordExplanation");
		sb.append("{text='").append(text).append('\'');
		sb.append(", explanationCases=").append(explanationCases);
		sb.append('}');
		return sb.toString();
	}
}
