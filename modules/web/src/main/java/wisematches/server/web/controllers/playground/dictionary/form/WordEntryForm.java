package wisematches.server.web.controllers.playground.dictionary.form;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WordEntryForm {
	private String word;
	private String language;
	private String action;
	private String definition;
	private Object attributes;

	public WordEntryForm() {
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public String[] getAttributes() {
		if (attributes == null) {
			return null;
		}

		if (attributes instanceof String) {
			return new String[]{String.valueOf(attributes)};
		}
		@SuppressWarnings("unchecked")
		Collection<String> a = (Collection<String>) attributes;
		String[] res = new String[a.size()];
		a.toArray(res);
		return res;
	}

	public void setAttributes(Object attributes) {
		this.attributes = attributes;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Override
	public String toString() {
		return "WordEntryForm{" +
				"word='" + word + '\'' +
				", definition='" + definition + '\'' +
				", attributes=" + (attributes == null ? null : Arrays.asList(attributes)) +
				'}';
	}
}
