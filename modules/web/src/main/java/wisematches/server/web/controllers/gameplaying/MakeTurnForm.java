package wisematches.server.web.controllers.gameplaying;

import java.util.Arrays;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MakeTurnForm {
	private String text;
	private Position start;
	private String length;
	private String direction;
	private Tile[] tiles;

	public MakeTurnForm() {
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Position getStart() {
		return start;
	}

	public void setStart(Position start) {
		this.start = start;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public Tile[] getTiles() {
		return tiles;
	}

	public void setTiles(Tile[] tiles) {
		this.tiles = tiles;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("MakeTurnForm");
		sb.append("{text='").append(text).append('\'');
		sb.append(", start=").append(start);
		sb.append(", length='").append(length).append('\'');
		sb.append(", direction='").append(direction).append('\'');
		sb.append(", tiles=").append(tiles == null ? "null" : Arrays.asList(tiles).toString());
		sb.append('}');
		return sb.toString();
	}

	public static class Tile {
		private String number;
		private String letter;
		private String cost;
		private String wildcard;
		private String row;
		private String column;

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}

		public String getLetter() {
			return letter;
		}

		public void setLetter(String letter) {
			this.letter = letter;
		}

		public String getCost() {
			return cost;
		}

		public void setCost(String cost) {
			this.cost = cost;
		}

		public String getWildcard() {
			return wildcard;
		}

		public void setWildcard(String wildcard) {
			this.wildcard = wildcard;
		}

		public String getRow() {
			return row;
		}

		public void setRow(String row) {
			this.row = row;
		}

		public String getColumn() {
			return column;
		}

		public void setColumn(String column) {
			this.column = column;
		}
	}

	public static class Position {
		private String row;
		private String column;

		public String getRow() {
			return row;
		}

		public void setRow(String row) {
			this.row = row;
		}

		public String getColumn() {
			return column;
		}

		public void setColumn(String column) {
			this.column = column;
		}
	}
}
