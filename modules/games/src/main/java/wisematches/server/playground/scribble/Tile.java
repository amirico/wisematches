package wisematches.server.playground.scribble;

import java.io.Serializable;

/**
 * Simple tile. Each tile has unique number in all tiles set, letter and cost.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class Tile implements Serializable {
	private int number;
	private char letter;
	private int cost;

	private Tile() {
	}

	/**
	 * Creates new tile.
	 *
	 * @param number the unique tile number.
	 * @param letter the tile letter.
	 * @param cost   the tile cost.
	 */
	public Tile(int number, char letter, int cost) {
		this.number = number;
		this.letter = letter;
		this.cost = cost;
	}

	public int getNumber() {
		return number;
	}

	public char getLetter() {
		return letter;
	}

	public int getCost() {
		return cost;
	}

	public boolean isWildcard() {
		return cost == 0;
	}

	public Tile redefine(char newLetter) {
		if (!isWildcard()) {
			throw new IllegalArgumentException("Tile is not wildcard.");
		}
		return new Tile(number, newLetter, cost);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Tile tile = (Tile) o;
		return number == tile.number;
	}

	@Override
	public int hashCode() {
		return number;
	}

	@Override
	public String toString() {
		return number + ":" + letter + (isWildcard() ? "*" : "");
	}
}
