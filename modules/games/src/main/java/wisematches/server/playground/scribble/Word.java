package wisematches.server.playground.scribble;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;

/**
 * <code>Word</code> is array of tiles that placed on the board at specified position and
 * that has a direction.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class Word implements Iterable<Word.IteratorItem>, Serializable {
	private Tile[] tiles;
	private Position position;
	private Direction direction;

	private String text;

	/**
	 * This is GWT Serialization constructor.
	 */
	Word() {
		tiles = null;
		direction = null;
		position = null;
		text = null;
	}

	/**
	 * Creates new word at specified <code>position</code> with specified <code>direction</code>
	 * and array of tiles.
	 * <p/>
	 * The length of any word should be more that one characters. This constructor doesn't check
	 * maximum length of word.
	 *
	 * @param position  the position of word.
	 * @param direction the direction of word.
	 * @param tiles	 the tiles of word.
	 * @throws IllegalArgumentException if any parameter is <code>null</code> and
	 *                                  tiles length is less than two.
	 */
	public Word(Position position, Direction direction, Tile... tiles) {
		if (tiles == null || tiles.length < 2) {
			throw new IllegalArgumentException("There is no tiles or tiles count less than 2");
		}
		if (direction == null) {
			throw new IllegalArgumentException("Direction can't be null");
		}
		if (position == null) {
			throw new IllegalArgumentException("Position can't be null");
		}
		this.tiles = tiles;
		this.direction = direction;
		this.position = position;

		final StringBuilder sb = new StringBuilder();
		for (Tile tile : tiles) {
			if (tile == null) {
				throw new IllegalArgumentException("One of tile is null");
			}
			sb.append(tile.getLetter());
		}
		text = sb.toString();
	}

	/**
	 * Returns array of tiles, associated with this word.
	 *
	 * @return the array of tiles.
	 */
	public Tile[] getTiles() {
		return tiles;
	}

	/**
	 * Returns direction of this word.
	 *
	 * @return the word's direction.
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * Returns start position of this word.
	 *
	 * @return the word's start position.
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * Returns length of this word.
	 *
	 * @return the word's length.
	 */
	public int length() {
		return tiles.length;
	}

	public String getText() {
		return text;
	}

	/**
	 * Returns iterator for this word. This is read-only iterator and <code>Iterator.remove()</code>
	 * method throws <code>UnsupportedOperationException</code>.
	 * <p/>
	 * This iterator has only one instance of <code>IteratorItem</code> and just changes it's
	 * fields on each <code>next()</code> method invocation. If you want store all <code>IteratorItem</code>
	 * object you must <code>clone</code> its.
	 *
	 * @return the word's iterator.
	 */
	public Iterator<IteratorItem> iterator() {
		return new Iterator<IteratorItem>() {
			private IteratorItem wordItem = new IteratorItem();

			private int index = 0;
			private int row = position.row;
			private int column = position.column;

			public boolean hasNext() {
				return index < tiles.length;
			}

			public IteratorItem next() {
				if (!hasNext()) {
					throw new IllegalStateException("Iterator has no next element");
				}

				wordItem.tile = tiles[index++];
				wordItem.row = row;
				wordItem.column = column;

				if (direction == Direction.VERTICAL) {
					row++;
				} else {
					column++;
				}
				return wordItem;
			}

			public void remove() {
				throw new UnsupportedOperationException("This is read-only iterator");
			}
		};
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Word word = (Word) o;
		return direction == word.direction && position.equals(word.position) && Arrays.equals(tiles, word.tiles);
	}

	@Override
	public int hashCode() {
		int result = Arrays.hashCode(tiles);
		result = 31 * result + position.hashCode();
		result = 31 * result + direction.ordinal();
		return result;
	}

	/*
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Word word = (Word) o;
		return direction == word.direction && position.equals(word.position) && text.equals(word.text);
	}

	@Override
	public int hashCode() {
		int result = direction.ordinal();
		result = 31 * result + position.hashCode();
		result = 31 * result + text.hashCode();
		return result;
	}
*/

	@Override
	public String toString() {
		return "Word{" +
				"tiles=" + (tiles == null ? null : Arrays.asList(tiles)) +
				", direction=" + direction +
				", position=" + position +
				", text='" + text + '\'' +
				'}';
	}

	/**
	 * A item of word's iterator. Each item has a tile, it's row and it's column position.
	 * <p/>
	 * Original iterator item is mutable and changed by iterator. You can
	 * use a <code>clone</code> method to create a clone of item.
	 */
	public static class IteratorItem {
		private Tile tile;
		private int row;
		private int column;

		private IteratorItem() {
		}

		private IteratorItem(Tile tile, int row, int column) {
			this.tile = tile;
			this.row = row;
			this.column = column;
		}

		/**
		 * Returns tile that represented by this item.
		 *
		 * @return the tile.
		 */
		public Tile getTile() {
			return tile;
		}

		/**
		 * Returns row position of the tile.
		 *
		 * @return the row position of the tile.
		 */
		public int getRow() {
			return row;
		}

		/**
		 * Returns column position of the tile.
		 *
		 * @return the column position of the tile.
		 */
		public int getColumn() {
			return column;
		}

		/**
		 * Creates clone of this item. Creates clone contains the same tile object.
		 *
		 * @return the clone of this item.
		 */
		public IteratorItem copy() {
			return new IteratorItem(tile, row, column);
		}
	}
}
