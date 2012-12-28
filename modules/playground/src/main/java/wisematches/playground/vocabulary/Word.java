package wisematches.playground.vocabulary;

import java.io.Serializable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class Word implements Serializable {
	private String text;
	private WordGender gender;
	private String description;

	public Word(String text, WordGender gender, String description) {
		this.text = text;
		this.gender = gender;
		this.description = description;
	}

	public String getText() {
		return text;
	}

	public WordGender getGender() {
		return gender;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Word");
		sb.append("{text='").append(text).append('\'');
		sb.append(", gender=").append(gender);
		sb.append(", description='").append(description).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
