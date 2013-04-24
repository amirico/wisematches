package wisematches.client.android.app.playground.scribble.model;

import java.util.Arrays;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleWord {
	private int row;
	private int column;
	private WordDirection direction;
	private ScribbleTile[] selectedTiles;

	public ScribbleWord(int row, int column, WordDirection direction, ScribbleTile[] selectedTiles) {
		this.row = row;
		this.column = column;
		this.direction = direction;
		this.selectedTiles = selectedTiles;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public WordDirection getDirection() {
		return direction;
	}

	public ScribbleTile[] getSelectedTiles() {
		return selectedTiles;
	}

	public CharSequence getText() {
		StringBuilder b = new StringBuilder();
		for (ScribbleTile selectedTile : selectedTiles) {
			b.append(selectedTile.getLetter());
		}
		return b;
	}


	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("ScribbleWord{");
		sb.append("row=").append(row);
		sb.append(", column=").append(column);
		sb.append(", direction=").append(direction);
		sb.append(", selectedTiles=").append(Arrays.toString(selectedTiles));
		sb.append('}');
		return sb.toString();
	}
}
