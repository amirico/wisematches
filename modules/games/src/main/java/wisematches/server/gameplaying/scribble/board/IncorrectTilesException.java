package wisematches.server.gameplaying.scribble.board;

import wisematches.server.gameplaying.board.IncorrectMoveException;
import wisematches.server.gameplaying.scribble.Position;
import wisematches.server.gameplaying.scribble.Tile;

import static wisematches.server.gameplaying.scribble.board.IncorrectTilesException.Type.*;

/**
 * Indicates that a tile of word are incorrect of can not be placed on board by some
 * reasones.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class IncorrectTilesException extends IncorrectMoveException {
	private final Type type;

	/**
	 * Indicates that word does not contains no tiles from board or from hand.
	 * <p/>
	 * This constructor changes type to {@code NO_BOARD_TILES} or {@code NO_HAND_TILES} expect
	 * of specified argument.
	 *
	 * @param notTilesFromBoard {@code true} to indicate that no one tile from board is used;
	 *                          {@code false} to indicate that no one tile from hand is used.
	 * @see Type#NO_BOARD_TILES
	 * @see Type#NO_HAND_TILES
	 */
	public IncorrectTilesException(boolean notTilesFromBoard) {
		super(notTilesFromBoard ? "No one tile from board is used" : "No one tile from player's hand is used");
		type = notTilesFromBoard ? NO_BOARD_TILES : NO_HAND_TILES;
	}

	/**
	 * Indicates that unknown tile are placed. It means that board and player's hand does not contains
	 * specified tile.
	 * <p/>
	 * This constructor changes type to {@code UNKNOWN_TILE}
	 *
	 * @param unknownTile unknown tile.
	 * @see Type#UNKNOWN_TILE
	 */
	public IncorrectTilesException(Tile unknownTile) {
		super("Word contains unknown tile " + unknownTile);
		type = UNKNOWN_TILE;
	}

	/**
	 * Indicates that specified tile can not be placed at specified position because
	 * already placed in another position.
	 * <p/>
	 * This constructor changes type to {@code TILE_ALREADY_PLACED}
	 *
	 * @param placedTile		the tile that is placed
	 * @param specifiedPosition the position where tile should be placed
	 * @param placedPosition	the position where tile already plcaed.
	 * @see Type#TILE_ALREADY_PLACED
	 */
	public IncorrectTilesException(Tile placedTile, Position specifiedPosition, Position placedPosition) {
		super("Tile " + placedTile + " can not be placed at " + specifiedPosition +
				" because already placed in position " + placedPosition);
		type = TILE_ALREADY_PLACED;
	}

	/**
	 * Indicates that board cell already busy by another tile.
	 * <p/>
	 * This constructor changes type to {@code CELL_ALREADY_BUSY}
	 *
	 * @param placedTile	 the tile that should be placed.
	 * @param boardTile	  the tile that already placed in required position.
	 * @param placedPosition the position where tile should be placed.
	 * @see Type#CELL_ALREADY_BUSY
	 */
	public IncorrectTilesException(Tile placedTile, Tile boardTile, Position placedPosition) {
		super("Tile " + placedTile + " can not be placed at " + placedPosition + " because " +
				"this position already contains another tile " + boardTile);
		type = CELL_ALREADY_BUSY;
	}

	/**
	 * Returns type of problem.
	 *
	 * @return the type of problem.
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Type of problems with tiles.
	 *
	 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
	 */
	public static enum Type {
		/**
		 * Indicates that no one tiles from board is used.
		 */
		NO_BOARD_TILES,
		/**
		 * Indicates that no one tiles from hand is used.
		 */
		NO_HAND_TILES,
		/**
		 * Indicates that specified tile is not present on board and in hand.
		 */
		UNKNOWN_TILE,
		/**
		 * Indicates that tile already placed in another position.
		 */
		TILE_ALREADY_PLACED,
		/**
		 * Indicates that board cell already busy by another tile.
		 */
		CELL_ALREADY_BUSY
	}
}
