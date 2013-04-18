package wisematches.client.android.app.playground.scribble.model;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleTile {
	private final int cost;
	private final int number;
	private final String letter;

	public ScribbleTile(int cost, int number, String letter) {
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

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("ScribbleTile{");
		sb.append("cost=").append(cost);
		sb.append(", number=").append(number);
		sb.append(", letter='").append(letter).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
