package wisematches.server.web.controllers.gameplaying.form;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import wisematches.server.gameplaying.scribble.Direction;
import wisematches.server.gameplaying.scribble.Position;
import wisematches.server.gameplaying.scribble.Tile;
import wisematches.server.gameplaying.scribble.Word;

import java.util.Arrays;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScribbleWordForm {
	private String direction;
	private PositionEditor position;
	private ScribbleTileForm[] tiles;

	public ScribbleWordForm() {
	}

	public Word createWord() {
		final Tile[] t = new Tile[tiles.length];
		for (int i = 0, tileEditorsLength = tiles.length; i < tileEditorsLength; i++) {
			t[i] = tiles[i].createTile();
		}
		return new Word(position.createPosition(), Direction.valueOf(direction.toUpperCase()), t);
	}


	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public ScribbleTileForm[] getTiles() {
		return tiles;
	}

	public void setTiles(ScribbleTileForm[] tiles) {
		this.tiles = tiles;
	}

	public PositionEditor getPosition() {
		return position;
	}

	public void setPosition(PositionEditor position) {
		this.position = position;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("ScribbleWordForm");
		sb.append("{direction='").append(direction).append('\'');
		sb.append(", position=").append(position);
		sb.append(", tiles=").append(tiles == null ? "null" : Arrays.asList(tiles).toString());
		sb.append('}');
		return sb.toString();
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class PositionEditor {
		private int row;
		private int column;

		public PositionEditor() {
		}

		public int getRow() {
			return row;
		}

		public void setRow(int row) {
			this.row = row;
		}

		public int getColumn() {
			return column;
		}

		public void setColumn(int column) {
			this.column = column;
		}

		public Position createPosition() {
			return new Position(row, column);
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append("PositionEditor");
			sb.append("{row=").append(row);
			sb.append(", column=").append(column);
			sb.append('}');
			return sb.toString();
		}
	}
}
