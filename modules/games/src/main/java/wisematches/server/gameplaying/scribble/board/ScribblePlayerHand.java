package wisematches.server.gameplaying.scribble.board;

import wisematches.server.gameplaying.board.GamePlayerHand;
import wisematches.server.gameplaying.scribble.Tile;

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
		System.out.println("============== new hibernate tiles created");
/*
		try {
			throw new Exception();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
*/
	}

	public ScribblePlayerHand(long playerId) {
		this(playerId, null);
	}

	public ScribblePlayerHand(long playerId, Tile[] tiles) {
		super(playerId);
		System.out.println("============== new tile created " + Thread.currentThread() + ", " + System.identityHashCode(this));
		if (tiles != null) {
			this.tiles = tiles.clone();
		}
	}

	public Tile[] getTiles() {
		if (tiles == EMPTY_TILES) {
			return EMPTY_TILES;
		}
		System.out.println("============== get tiles " + Arrays.toString(this.tiles) + ": " + Thread.currentThread() + ", " + System.identityHashCode(this));
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
		System.out.println("============== add tiles " + Arrays.toString(this.tiles) + ": " + Thread.currentThread() + ", " + System.identityHashCode(this));
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
		System.out.println("============== remove tiles " + Arrays.toString(this.tiles) + ": " + Thread.currentThread() + ", " + System.identityHashCode(this));
		this.tiles = res;
	}

	void setTiles(Tile[] tiles) {
		if (tiles == null) {
			throw new IllegalArgumentException("Letters can't be null");
		}
		this.tiles = tiles.clone();
		System.out.println("============== set tiles " + Arrays.toString(this.tiles) + ": " + Thread.currentThread() + ", " + System.identityHashCode(this));
	}

	@Override
	public String toString() {
		return "ScribblePlayerHand{" +
				super.toString() +
				", tiles=" + (tiles == null ? null : Arrays.asList(tiles)) +
				'}';
	}
}
