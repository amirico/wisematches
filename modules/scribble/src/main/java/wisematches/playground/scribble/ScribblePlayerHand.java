package wisematches.playground.scribble;

import wisematches.server.playground.board.GamePlayerHand;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Arrays;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Embeddable
public final class ScribblePlayerHand extends GamePlayerHand {
	@Transient
	private Tile[] tiles = EMPTY_TILES;

	private static final Tile[] EMPTY_TILES = new Tile[0];

	/**
	 * Hibernate only constructor
	 */
	@Deprecated
	ScribblePlayerHand() {
	}

	public ScribblePlayerHand(long playerId) {
		this(playerId, null);
	}

	public ScribblePlayerHand(long playerId, Tile[] tiles) {
		super(playerId);
		if (tiles != null) {
			this.tiles = tiles.clone();
		}
	}

	public Tile[] getTiles() {
		if (tiles == EMPTY_TILES) {
			return EMPTY_TILES;
		}
		return tiles.clone();
	}

	public boolean containsTile(Tile tile) {
		for (Tile tile1 : tiles) {
			if (tile1.equals(tile)) {
				return true;
			}
		}
		return false;
	}

	void addTiles(Tile[] tiles) {
		Tile[] res = new Tile[tiles.length + this.tiles.length];
		System.arraycopy(this.tiles, 0, res, 0, this.tiles.length);
		System.arraycopy(tiles, 0, res, this.tiles.length, tiles.length);

		this.tiles = res;
	}

	void removeTiles(Tile[] tiles) {
		Tile[] res = new Tile[this.tiles.length - tiles.length];

		int index = 0;
		for (Tile pt : this.tiles) {
			boolean remove = false;
			for (int i = 0; i < tiles.length && !remove; i++) {
				remove = pt.equals(tiles[i]);
			}

			if (!remove) {
				res[index++] = pt;
			}
		}
		this.tiles = res;
	}

	void setTiles(Tile[] tiles) {
		if (tiles == null) {
			throw new IllegalArgumentException("Letters can't be null");
		}
		this.tiles = tiles.clone();
	}

	@Override
	public String toString() {
		return "ScribblePlayerHand{" +
				super.toString() +
				", tiles=" + (tiles == null ? null : Arrays.asList(tiles)) +
				'}';
	}
}
