package wisematches.server.gameplaying.scribble.bank;

import wisematches.server.gameplaying.scribble.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * <code>TilesBank</code> is a bank of tiles in game. When bank is initialized it contains all tiles. When
 * players can request tiles from bank.
 * <p/>
 * Each tile in this bank has it's own number.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class TilesBank {
	private final Tile[] tiles;
	private final TilesInfo[] tilesInfos;
	private final List<Integer> unusedIndexes;

	/**
	 * Initializes bank with specified tiles. Each tiles info contains information about tiles of one type
	 * (tiles of one letter).
	 *
	 * @param letterInfos the bank letters info.
	 */
	public TilesBank(TilesInfo... letterInfos) {
		this.tilesInfos = letterInfos.clone();
		int capacity = calculateCapacity(letterInfos);

		tiles = new Tile[capacity];
		unusedIndexes = new ArrayList<Integer>(capacity);

		int number = 0;
		for (final TilesInfo li : letterInfos) {
			for (int j = 0; j < li.getCount(); j++) {
				tiles[number] = new Tile(number, li.getLetter(), li.getCost());
				unusedIndexes.add(number);
				number++;
			}
		}
	}

	/**
	 * Returns tile by specified number.
	 * <p/>
	 * This method doesn't remove tile from free tiles queue.
	 *
	 * @param number the tile number
	 * @return the tile by specified number
	 * @throws IndexOutOfBoundsException if number is negative of great when bank capacity.
	 * @see #getBankCapacity()
	 */
	public Tile getTile(int number) {
		return tiles[number];
	}

	/**
	 * Returns tiles for specified numbers.
	 *
	 * @param numbers the number of tiles which must be returned.
	 * @return the array of tiles for specified numbers.
	 */
	public Tile[] getTiles(int... numbers) {
		Tile[] res = new Tile[numbers.length];
		for (int i = 0; i < numbers.length; i++) {
			res[i] = tiles[numbers[i]];
		}
		return res;
	}

	/**
	 * Returns number of all tiles in bank, including already used tiles.
	 *
	 * @return the bank capacity.
	 */
	public int getBankCapacity() {
		return tiles.length;
	}

	/**
	 * Checks that tile with specified number already in use.
	 *
	 * @param tileNumber the tile number to check
	 * @return <code>true</code> if tile already in use; <code>false</code> - otherwise.
	 */
	public boolean isTileInUse(int tileNumber) {
		return !unusedIndexes.contains(tileNumber);
	}

	/**
	 * Returns number of unused tiles.
	 *
	 * @return the number of unused tiles.
	 */
	public int getTilesLimit() {
		return unusedIndexes.size();
	}

	/**
	 * Checks that bank doesn't have unused tiles. This method is equalent of <code>getTilesLimit() == 0</code>
	 *
	 * @return <code>true</code> if bank doesn't have more unused tiles; <code>false</code> - otherwise.
	 * @see #getTilesLimit()
	 */
	public boolean isEmpty() {
		return unusedIndexes.size() == 0;
	}

	/**
	 * Returns copy of information about tiles in this bank.
	 * <p/>
	 * TODO: this method should be replaced to {@code TilesBankInfo} class. This class should
	 * contains {@code Locale}, number of tiles, array of {@code TilesInfo}, version of bank and so on.
	 *
	 * @return
	 */
	public TilesInfo[] getTilesInfos() {
		return tilesInfos.clone();
	}

	/**
	 * Requests tiles from bank and mark its as used. If bank doesn't have required tiles, minimum allowed tiles
	 * will be returned.
	 *
	 * @param tilesCount the number of tiles that should be requested.
	 * @return the random tiles
	 * @throws IllegalStateException if bank is empty
	 * @see #isEmpty()
	 */
	public Tile[] requestTiles(int tilesCount) {
		if (getTilesLimit() == 0) {
			throw new IllegalStateException("Bank is empty. Requested tiles count: " + tilesCount);
		}

		final int count = Math.min(tilesCount, unusedIndexes.size());
		final Tile[] res = new Tile[count];
		for (int i = 0; i < res.length; i++) {
			int pos = (int) (unusedIndexes.size() * Math.random());
			if (pos < 0 || pos >= unusedIndexes.size()) {
				throw new IllegalStateException("Bank is empty. Requested tiles count: " + tilesCount);
			}

			final Integer index = unusedIndexes.remove(pos);
			if (index != null) {
				res[i] = tiles[index];
			} else {
				throw new IllegalStateException("Bank is empty. Requested tiles count: " + tilesCount);
			}
		}
		return res;
	}

	/**
	 * Request tile from bank by specified number. Tile is marked as used.
	 *
	 * @param number the number of tile.
	 * @return requested tile
	 * @throws IllegalArgumentException if tile with specified number already requested.
	 * @see #isTileInUse(int)
	 */
	public Tile requestTile(int number) {
		if (unusedIndexes.remove(Integer.valueOf(number))) {
			return tiles[number];
		} else {
			throw new IllegalArgumentException("Requested tile not present in bank: " + number);
		}
	}

	public void redefineTile(int tileNumber, char newLetter) {
		final Tile tile = getTile(tileNumber);
		if (!tile.isWildcard()) {
			throw new IllegalArgumentException("Tile at specified number is not wildcard");
		}

		if (tile.getLetter() != '*') {
			throw new IllegalArgumentException("Tile already redefined");
		}

		tiles[tileNumber] = tile.redefine(newLetter);
	}

	/**
	 * Returns this tile to unused list.
	 *
	 * @param number the tile number to rollback.
	 */
	public void rollbackTile(int number) {
		if (!unusedIndexes.contains(number)) {
			unusedIndexes.add(number);
		}
	}

	private int calculateCapacity(TilesInfo... letterInfos) {
		int capacity = 0;
		for (TilesInfo letterInfo : letterInfos) {
			capacity += letterInfo.getCount();
		}
		return capacity;
	}

	/**
	 * <code>TilesInfo</code> contains information about tiles of the same type (tiles with the same letter).
	 *
	 * @author Sergey Klimenko (smklimenko@gmail.com)
	 */
	public static final class TilesInfo {
		private final char letter;
		private final int cost;
		private final int count;

		/**
		 * Creates new information about tile.
		 *
		 * @param letter the letter of tiles.
		 * @param count  the number of tiles with this letter.
		 * @param cost   the cost of these tiles.
		 */
		public TilesInfo(char letter, int count, int cost) {
			this.letter = letter;
			this.count = count;
			this.cost = cost;
		}

		public char getLetter() {
			return letter;
		}

		public int getCount() {
			return count;
		}

		public int getCost() {
			return cost;
		}
	}
}
