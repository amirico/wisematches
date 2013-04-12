package wisematches.client.android.app.playground.model;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class ScoreBonus {
	private final int row;
	private final int column;
	private final Type type;

	public ScoreBonus(int row, int column, Type type) {
		this.row = row;
		this.column = column;
		this.type = type;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public Type getType() {
		return type;
	}

	public static enum Type {
		L2(0xff9cff00),
		L3(0xff17e421),
		W2(0xff00fcff),
		W3(0xfffffc00);

		private final int color;

		private Type(int color) {
			this.color = color;
		}

		public int getColor() {
			return color;
		}
	}
}
