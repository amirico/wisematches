package wisematches.server.deprecated.web.modules.app.playboard.memory;

import wisematches.server.gameplaying.scribble.Word;

import java.io.Serializable;

/**
 * Memory word contains number of memory word and a word.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class MemoryWord implements Serializable {
	private int number;
	private Word word;

	/**
	 * This is GWT serialization constructor.
	 */
	private MemoryWord() {
	}

	/**
	 * Creates new word with specified number and word.
	 *
	 * @param number the memory word's number
	 * @param word   the memory word.
	 */
	public MemoryWord(int number, Word word) {
		this.number = number;
		this.word = word;
	}

	public int getNumber() {
		return number;
	}

	public Word getWord() {
		return word;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		MemoryWord word1 = (MemoryWord) o;
		return number == word1.number && word.equals(word1.word);
	}

	@Override
	public int hashCode() {
		int result = number;
		result = 31 * result + word.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "MemoryWord{" +
				"number=" + number +
				", word=" + word +
				'}';
	}
}
