package wisematches.client.android.app.playground.scribble.model;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleLetter {
	private final int cost;
	private final int count;
	private final char letter;

	public ScribbleLetter(char letter, int count, int cost) {
		this.letter = letter;
		this.count = count;
		this.cost = cost;
	}

	public int getCost() {
		return cost;
	}

	public int getCount() {
		return count;
	}

	public char getLetter() {
		return letter;
	}
}
