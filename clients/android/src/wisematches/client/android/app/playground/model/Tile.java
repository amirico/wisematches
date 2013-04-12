package wisematches.client.android.app.playground.model;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class Tile {
	private final int cost;
	private final int number;
	private final String letter;

	public Tile(int cost, int number, String letter) {
		this.cost = cost;
		this.number = number;
		this.letter = letter;
	}

	public int getCost() {
		return cost;
	}

	public int getNumber() {
		return number;
	}

	public String getLetter() {
		return letter;
	}

	public boolean isWildcard() {
		return cost == 0;
	}
}
