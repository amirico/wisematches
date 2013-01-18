package wisematches.core;

import java.util.SortedSet;

/**
 * TODO: must have link to language or private constructor.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Deprecated
public final class Alphabet {
	private final char[] letters;

	public Alphabet(String letters) {
		this(letters.toCharArray());
	}

	public Alphabet(char[] letters) {
		this.letters = letters;
	}

	public Alphabet(SortedSet<Character> letters) {
		int index = 0;
		this.letters = new char[letters.size()];
		for (Character letter : letters) {
			this.letters[index++] = letter;
		}
	}

	public char[] getLetters() {
		return letters.clone();
	}

	public boolean contains(char letter) {
		for (char aChar : letters) {
			if (aChar == letter) {
				return true;
			}
		}
		return false;
	}

	public boolean validate(String word) {
		for (char c : word.toCharArray()) {
			if (!contains(c)) {
				return false;
			}
		}
		return true;
	}
}
