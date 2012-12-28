package wisematches.playground.vocabulary;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum WordGender {
	FEMININE('f'),
	MASCULINE('m'),
	NEUTER('n');

	private final char code;

	private WordGender(char code) {
		this.code = code;
	}

	public char encode() {
		return code;
	}

	public static WordGender decode(char ch) {
		if (ch == 'f') {
			return FEMININE;
		} else if (ch == 'm') {
			return MASCULINE;
		} else if (ch == 'n') {
			return NEUTER;
		}
		throw new IllegalArgumentException("Incorrect code: " + ch);
	}
}
