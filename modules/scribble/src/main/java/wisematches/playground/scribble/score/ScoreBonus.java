package wisematches.playground.scribble.score;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class ScoreBonus {
	private final Type type;
	private final int row;
	private final int column;

	public ScoreBonus(int row, int column, Type type) {
		this.type = type;
		this.row = row;
		this.column = column;
	}

	public Type getType() {
		return type;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public static enum Type {
		L2,
		L3,
		W2,
		W3
	}
}