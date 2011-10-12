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
		DOUBLE_LETTER("2l"),
		TRIPLE_LETTER("3l"),
		DOUBLE_WORD("2w"),
		TRIPLE_WORD("3w");

		private final String displayName;

		Type(String displayName) {
			this.displayName = displayName;
		}

		public String getDisplayName() {
			return displayName;
		}
	}
}