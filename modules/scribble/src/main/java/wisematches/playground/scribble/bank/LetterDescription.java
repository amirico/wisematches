package wisematches.playground.scribble.bank;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class LetterDescription {
	private final char letter;
	private final int cost;
	private final int count;

	public LetterDescription(char letter, int cost, int count) {
		this.letter = letter;
		this.cost = cost;
		this.count = count;
	}

	public char getLetter() {
		return letter;
	}

	public int getCost() {
		return cost;
	}

	public int getCount() {
		return count;
	}
}
