package wisematches.playground.scribble;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.Type;
import wisematches.personality.Personality;
import wisematches.playground.*;
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.scribble.bank.LettersDistribution;
import wisematches.playground.scribble.bank.TilesBank;
import wisematches.playground.scribble.score.ScoreEngine;
import wisematches.playground.scribble.score.engines.ScribbleScoreEngine;

import javax.persistence.*;
import java.nio.ByteBuffer;
import java.util.*;

import static wisematches.playground.scribble.Direction.HORIZONTAL;
import static wisematches.playground.scribble.Direction.VERTICAL;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@Entity
@Table(name = "scribble_board")
public class ScribbleBoard extends AbstractGameBoard<ScribbleSettings, ScribblePlayerHand> implements TilesPlacement {
	@Transient
	private TilesBank tilesBank;

	@Transient
	private Dictionary dictionary;

	@Transient
	private Position[] positions;

	@Transient
	private final ScoreEngine scoreEngine = new ScribbleScoreEngine();

	/**
	 * Arrays encoded tiles on board.
	 * <p/>
	 * This field must be stored into {@code TINYBLOB} SQL type because it required
	 * {@literal 15 * 15 * 1 + 2 * 8 = 241} bytes.
	 * <p/>
	 * Here, {@literal 15 * 15} - board size, {@literal * 1} - required bytes for each cell, {@literal + 2 * 8} -
	 * game maximum has 8 wildcare tiles and each wildcare tile requires additional 2 bytes for letter redifinition.
	 * <p/>
	 * Each tile required only 1 byte that contains number of tile but we have wildcare tiles that requires
	 * 3 bytes (tile number and new letter).
	 * <p/>
	 * Each move encoded by following rule:
	 * <pre>
	 *  |-----------------|
	 *  |   1 byte        |
	 *  |-----------------|
	 *  | tile number + 1 |
	 *  |-----------------|
	 * </pre>
	 */
	private final byte[] boardTiles = new byte[255];

	/**
	 * Arrays of tiles in player's hands. This field must be stored into {@code TINYBLOB} SQL type because
	 * required {@literal 7 * 4 = 42} bytes.
	 * <p/>
	 * Here 7 - number of tiles in each hand, 4 - maximum number of players.
	 * <p/>
	 * In this array bytes from 0 to 6 contains all tiles for player with index 0, bytes from 7 to 13 - tiles
	 * for player with index 1 and so on. If player player has less when 7 tiles according byte
	 * must contains {@code Byte.MAX_VALUE}.
	 * <p/>
	 * This buffer contains tile number incremented by 1. Zero value means no tile.
	 */
	private final byte[] handTiles = new byte[42];

	/**
	 * Arrays of tiles redifinitions. This field must be stored into {@code TINYBLOB} SQL type because
	 * required {@literal 8*3 = 24} bytes.
	 * <p/>
	 * Here 8 - maximum number of wildcard tiles (2 wildcard tiles for each player and maximum 4 players),
	 * 3 - number of bytes for each redifinition.
	 * <p/>
	 * Each move redifinition encoded by following rule:
	 * <pre>
	 *  |--------------------------------|
	 *  |  2 byte        |  1-0 bytes    |
	 *  |--------------------------------|
	 *  | tile number    | new tile char |
	 *  |--------------------------------|
	 * </pre>
	 */
	private final ByteBuffer tilesRedefinitions = ByteBuffer.allocate(255);

	/**
	 * Arrays of maden moves. This filed must be stored info {@code BLOB} field because required
	 * {@literal 224 * 13 = 2912} bytes: if each player puts only one tile from hand, when only 225 (15*15) - 1
	 * moves can be maden (-1 because first move required at least 2 tiles). This size doesn't take into account passed
	 * turns but takes into account that number of moves will be less that 224 because each move will use
	 * more that one tile.
	 * <p/>
	 * Each move encoded by following rule:
	 * /* <pre>
	 *  |------------|-----------|---------------------------|-------------------------|------------------------------|
	 *  |   12-5     | 3-4 bytes |         2 byte            |         1 byte          |       0 bytes                |
	 *  |------------|-----------|---------------------------|-------------------------|------------------------------|
	 *  |   8 bytes  | 2 bytes   |  7-4 bits |  3-0 bits     | 7-4 bits  |    0bit     | 7-4bit |   3-1 bits  |  0bit |
	 *  |------------|-----------|---------------------------|-----------|-------------|--------|-------------|-------|
	 *  | move time  | points    | start row  | start column | length    | direction   |  type  | playerIndex |   1   |
	 *  |-------------------------------------------------------------------------------------------------------------|
	 * </pre>
	 */
	private final ByteBuffer boardMoves = ByteBuffer.allocate(2912);

	public static final int CELLS_NUMBER = 15;
	public static final int LETTERS_IN_HAND = 7;
	public static final int CENTER_CELL = (CELLS_NUMBER - 1) / 2;

	private static final int[] EMPTY_TILES_IDS = new int[0];

	private static final Log log = LogFactory.getLog("wisematches.games.scribble");

	/**
	 * This is Hibernate constructor. It must not be used directly. It should be default visible for
	 * Hibernate optimization.
	 */
	ScribbleBoard() {
	}

	public ScribbleBoard(ScribbleSettings settings, Collection<? extends Personality> players, TilesBank tilesBank, Dictionary dictionary) {
		this(settings, null, players, tilesBank, dictionary);
	}

	public ScribbleBoard(ScribbleSettings settings, GameRelationship relationship, Collection<? extends Personality> players, TilesBank tilesBank, Dictionary dictionary) {
		super(settings, relationship, players);
		if (log.isDebugEnabled()) {
			log.debug("Game started: " + getBoardId());
		}

		this.tilesBank = tilesBank;
		this.dictionary = dictionary;
		positions = new Position[tilesBank.getBankCapacity()];

		final List<ScribblePlayerHand> playerHands = getPlayersHands();
		for (ScribblePlayerHand hand : playerHands) {
			hand.addTiles(tilesBank.requestTiles(LETTERS_IN_HAND));
			if (log.isDebugEnabled()) {
				log.debug("Initialize player " + hand + " hand with tiles: " + Arrays.toString(hand.getTiles()));
			}
			updateHandTilesBuffer(hand);
		}
	}

	void initGameAfterLoading(TilesBank tilesBank, Dictionary vocabulary) {
		this.dictionary = vocabulary;

		this.tilesBank = tilesBank;
		positions = new Position[tilesBank.getBankCapacity()];
		restoreBoardState();
	}

	void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public Dictionary getDictionary() {
		return dictionary;
	}

	/**
	 * Creates new {@code ScrapplePlayerHand} for specified player.
	 *
	 * @param player the player for who hand must be crated.
	 * @return the created player hand.
	 * @see ScribblePlayerHand
	 */
	protected ScribblePlayerHand createPlayerHand(Personality player) {
		return new ScribblePlayerHand(player.getId());
	}

	private void restoreBoardState() {
		int b;
		while ((b = byteToInt(tilesRedefinitions.get())) != 0) {
			tilesBank.redefineTile(b - 1, tilesRedefinitions.getChar());
		}
		tilesRedefinitions.position(tilesRedefinitions.position() - 1);

		restorePlayerHands();
		restoreBoardTiles();
		restoreMoves();
	}

	private void restorePlayerHands() {
		// Process player hands
		int playerIndex = 0;
		final List<ScribblePlayerHand> playersHands = getPlayersHands();
		for (ScribblePlayerHand playersHand : playersHands) {
			for (int i = 0; i < LETTERS_IN_HAND; i++) {
				final int index = playerIndex * LETTERS_IN_HAND + i;
				final int tileNumber = byteToInt(handTiles[index]);
				if (tileNumber != 0) {
					playersHand.addTiles(new Tile[]{tilesBank.requestTile(tileNumber - 1)});
				}
			}
			playerIndex++;
		}
	}

	private void restoreBoardTiles() {
		//Process board tiles
		for (int i = 0; i < boardTiles.length; i++) {
			int tileNumber = byteToInt(boardTiles[i]);
			if (tileNumber != 0) {
				int row = i / CELLS_NUMBER;
				int column = i - row * CELLS_NUMBER;

				final Tile tile = tilesBank.requestTile(tileNumber - 1);
				positions[tile.getNumber()] = new Position(row, column);
			}
		}
	}

	private void restoreMoves() {
		while (true) {
			final int byte1 = byteToInt(boardMoves.get());
			if (byte1 == 0) {
				boardMoves.position(boardMoves.position() - 1); // roll position back
				break;
			}
			final byte code = (byte) ((byte1 >> 1) & 0x7); // 0111
			final int type = (byte1 >> 4) & 0xF; // 1111

			final int byte2 = byteToInt(boardMoves.get());
			final int direction = byte2 & 0xF;
			final int length = (byte2 >> 4) & 0xF;

			final int byte3 = byteToInt(boardMoves.get());
			int row = byte3 & 0xF;
			int column = (byte3 >> 4) & 0xF;

			final long playerId = getPlayerByCode(code).getPlayerId();
			final int points = boardMoves.getShort();
			final long time = boardMoves.getLong();

			final PlayerMove playerMove;
			if (type == 0) {
				final Position position = new Position(row, column);
				final Direction direct = (direction == 0 ? Direction.VERTICAL : Direction.HORIZONTAL);

				final Tile[] tiles = new Tile[length];
				for (int i = 0; i < tiles.length; i++) {
					final int idx = row * CELLS_NUMBER + column;
					final int tileNumber = byteToInt(boardTiles[idx]);
					tiles[i] = tilesBank.getTile(tileNumber - 1);

					if (direct == Direction.HORIZONTAL) {
						column++;
					} else {
						row++;
					}
				}
				playerMove = new MakeWordMove(playerId, new Word(position, direct, tiles));
			} else if (type == 1) {
				playerMove = new PassTurnMove(playerId);
			} else if (type == 2) {
				playerMove = new ExchangeTilesMove(playerId, EMPTY_TILES_IDS);
			} else {
				throw new IllegalStateException("Board state can't be restored. Unknown move type: " + type);
			}
			moves.add(new GameMove(playerMove, points, moves.size(), new Date(time)));
		}
	}

	private void registerMoveInBusyCells(GameMove gameMove) {
		final PlayerMove move = gameMove.getPlayerMove();

		int moveType;
		if (move instanceof MakeWordMove) {
			moveType = 0; // maden
		} else if (move instanceof PassTurnMove) {
			moveType = 1; // passed
		} else {
			moveType = 2; // exchanged
		}

		int moveInfo = 1; //indicates that it's move
		moveInfo |= getPlayerCode(getPlayerHand(move.getPlayerId())) << 1;
		moveInfo |= moveType << 4;
		boardMoves.put((byte) moveInfo);

		if (move instanceof MakeWordMove) {
			final MakeWordMove wordMove = (MakeWordMove) move;
			final Word word = wordMove.getWord();
			final Position position = word.getPosition();

			for (Word.IteratorItem item : word) {
				final Tile tile = item.getTile();
				final int number = tile.getNumber();
				if (positions[number] == null) {
					positions[number] = new Position(item.getRow(), item.getColumn());

					boardTiles[item.getRow() * CELLS_NUMBER + item.getColumn()] = (byte) (number + 1);
					if (tile.isWildcard()) {
						tilesRedefinitions.put((byte) (number + 1));
						tilesRedefinitions.putChar(tile.getLetter());
						tilesBank.redefineTile(number, tile.getLetter());
					}
				}
			}

			int wordInfo = (word.getDirection() == Direction.HORIZONTAL ? 1 : 0);
			wordInfo |= word.getTiles().length << 4;
			boardMoves.put((byte) wordInfo);

			int wordPosition = position.getRow();
			wordPosition |= position.getColumn() << 4;
			boardMoves.put((byte) wordPosition);
		} else {
			boardMoves.put((byte) 0);
			boardMoves.put((byte) 0);
		}
		boardMoves.putShort((short) gameMove.getPoints());
		boardMoves.putLong(gameMove.getMoveTime().getTime());
	}

	private int byteToInt(byte b) {
		return b & 0xFF;
	}

	/**
	 * Checks that <code>dictionary</code> and <code>tilesBank</code> is setted.
	 * <p/>
	 * This method also invokes <code>super</code> method to check that game is right state.
	 *
	 * @throws GameStateException if there is no dictionary or tiles bank.
	 * @see #setDictionary(wisematches.playground.dictionary.Dictionary)
	 */
	protected void checkState() throws GameStateException {
		if (dictionary == null || tilesBank == null) {
			throw new GameStateException("Dictionary or TilesBank isn't initialized");
		}
		super.checkState();
	}

	protected void checkMove(PlayerMove move) throws IncorrectMoveException {
		if (move instanceof MakeWordMove) {
			checkMakeWordMove((MakeWordMove) move);
		} else if (move instanceof ExchangeTilesMove) {
			checkExchangeTilesMove((ExchangeTilesMove) move);
		} else if (move instanceof PassTurnMove) {
			; //always valid
		} else {
			throw new IncorrectMoveException("Unsupported move type");
		}
	}

	@Override
	protected ScribbleMoveScore calculateMoveScores(PlayerMove move) {
		if (move instanceof MakeWordMove) {
			final MakeWordMove makeWordMove = (MakeWordMove) move;
			return scoreEngine.calculateWordScore(makeWordMove.getWord(), this);
		}
		return null;
	}

	@Override
	protected void processMoveFinished(ScribblePlayerHand player, GameMove gameMove) {
		if (log.isDebugEnabled()) {
			log.debug("Process move finished for player " + player + ": " + gameMove);
		}

		final PlayerMove playerMove = gameMove.getPlayerMove();
		if (playerMove instanceof MakeWordMove) {
			processMakeWordMoveFinished(player, (MakeWordMove) playerMove);
		} else if (playerMove instanceof ExchangeTilesMove) {
			processExchangeTilesMove(player, (ExchangeTilesMove) playerMove);
		} else if (playerMove instanceof PassTurnMove) {
			;
		} else {
			throw new IllegalArgumentException("Unsupported move type: " + playerMove);
		}
		registerMoveInBusyCells(gameMove);
	}

	private void checkMakeWordMove(MakeWordMove move) throws IncorrectMoveException {
		final Word word = move.getWord();

		final Position position = word.getPosition();
		final Direction direction = word.getDirection();
		final int length = word.length();

		if (position.row < 0 || position.column < 0 ||
				(direction == VERTICAL && position.row + length > CELLS_NUMBER) ||
				(direction == HORIZONTAL && position.column + length > CELLS_NUMBER)) {
			throw new IncorrectPositionException(position, direction, length, false);
		}

		if (getBoardTile(CENTER_CELL, CENTER_CELL) == null) { // Is first move...
			if (direction == VERTICAL) {
				if (!(position.row <= CENTER_CELL &&
						position.row + length >= CENTER_CELL && position.column == CENTER_CELL)) {
					throw new IncorrectPositionException(position, direction, length, true);
				}
			} else {
				if (!(position.column <= CENTER_CELL &&
						position.column + length >= CENTER_CELL && position.row == CENTER_CELL)) {
					throw new IncorrectPositionException(position, direction, length, true);
				}
			}
		} else {
			boolean hasTileOnBoard = false;
			boolean hasTileInHand = false;
			final ScribblePlayerHand hand = getPlayerHand(move.getPlayerId());
			for (Word.IteratorItem item : word) {
				final Tile tile = item.getTile();

				final Tile boardTile = getBoardTile(item.getRow(), item.getColumn());
				if (boardTile != null && boardTile.getNumber() != tile.getNumber()) {
					throw new IncorrectTilesException(tile, boardTile, new Position(item.getRow(), item.getColumn()));
				}


				final Position pos = positions[tile.getNumber()];
				if (pos == null) {
					if (!hand.containsTile(tile)) {
						throw new IncorrectTilesException(tile);
					}
					hasTileInHand = true;
				} else if (pos.row != item.getRow() || pos.column != item.getColumn()) {
					throw new IncorrectTilesException(tile, new Position(item.getRow(), item.getColumn()), pos);
				} else {
					hasTileOnBoard = true;
				}
			}

			if (!hasTileOnBoard) {
				throw new IncorrectTilesException(true);
			} else if (!hasTileInHand) {
				throw new IncorrectTilesException(false);
			}
		}

		final String w = move.getWord().getText();
		if (!dictionary.contains(w)) {
			throw new UnknownWordException(w);
		}
	}

	private void processMakeWordMoveFinished(ScribblePlayerHand player, MakeWordMove move) {
		final Tile[] moveTiles = move.getWord().getTiles();
		final Tile[] handTiles = player.getTiles();

		final List<Tile> tilesToRemove = new ArrayList<Tile>(LETTERS_IN_HAND);
		for (Tile handTile : handTiles) {
			for (Tile moveTile : moveTiles) {
				if (moveTile.equals(handTile)) {
					tilesToRemove.add(moveTile);
				}
			}
		}

		if (log.isDebugEnabled()) {
			log.debug("Finish player " + player + " make word move. Move tiles: " +
					Arrays.toString(moveTiles) + ", hand tiles: " +
					Arrays.toString(handTiles) + ", tiles to remove from hand: " + tilesToRemove);
		}
		player.removeTiles(tilesToRemove.toArray(new Tile[tilesToRemove.size()]));
		if (!tilesBank.isEmpty()) {
			final Tile[] tiles = tilesBank.requestTiles(LETTERS_IN_HAND - player.getTiles().length);
			if (log.isDebugEnabled()) {
				log.debug("Add new tiles to player's hand " + player + ": " + Arrays.toString(tiles));
			}
			player.addTiles(tiles);
		} else {
			if (log.isDebugEnabled()) {
				log.debug("Bank is empty. No tiles added to player's hand.");
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("New player " + player + " hand: " + Arrays.toString(player.getTiles()));
		}
		updateHandTilesBuffer(player);
	}


	private void checkExchangeTilesMove(ExchangeTilesMove move) throws IncorrectMoveException {
		final int[] ints = move.getTilesIds();
		if (ints.length == 0) {
			throw new IncorrectExchangeException("No tiles for exchange", IncorrectExchangeException.Type.EMPTY_TILES);
		}
		if (ints.length > tilesBank.getTilesLimit()) {
			throw new IncorrectExchangeException("Not required tiles to in bank", IncorrectExchangeException.Type.EMPTY_BANK);
		}

		final Tile[] tiles = getPlayerTurn().getTiles();

		for (int tileNumber : ints) {
			boolean found = false;
			for (Tile tile : tiles) {
				if (tileNumber == tile.getNumber()) {
					found = true;
					break;
				}
			}

			if (!found) {
				throw new IncorrectExchangeException("Player does not contains tiles to exchange",
						IncorrectExchangeException.Type.UNKNOWN_TILES);
			}
		}
	}

	private void processExchangeTilesMove(ScribblePlayerHand player, ExchangeTilesMove move) {
		final int[] ints = move.getTilesIds();

		if (log.isDebugEnabled()) {
			log.debug("Exchange tiles for player " + player + " with ids: " + Arrays.toString(ints));
		}

		final Tile[] tiles = tilesBank.requestTiles(ints.length);
		if (log.isDebugEnabled()) {
			log.debug("New tiles requested from bank: " + Arrays.toString(tiles));
		}

		int index = 0;
		final Tile[] handTiles = player.getTiles();
		for (int i = 0; i < handTiles.length; i++) {
			final Tile handTile = handTiles[i];
			for (int tileNumber : ints) {
				if (tileNumber == handTile.getNumber()) { //if hand tile in list to be replaced
					tilesBank.rollbackTile(tileNumber); // rollback tile
					handTiles[i] = tiles[index++]; // and replace it in hand
					break;
				}
			}
		}
		player.setTiles(handTiles);
		if (log.isDebugEnabled()) {
			log.debug("New player " + player + " hand: " + Arrays.toString(player.getTiles()));
		}
		updateHandTilesBuffer(player); // update player hand buffer
	}

	/**
	 * Updates hand tiles ByteBuffer for Hibernate.
	 *
	 * @param player the player who hand tiles buffer should be updated.
	 */
	private void updateHandTilesBuffer(ScribblePlayerHand player) {
		final int playerIndex = getPlayerCode(player);
		final Tile[] tiles = player.getTiles();
		final byte[] bytes = this.handTiles;

		for (int i = 0; i < tiles.length; i++) {
			final Tile tile = tiles[i];
			bytes[playerIndex * LETTERS_IN_HAND + i] = (byte) (tile.getNumber() + 1);
		}

		for (int i = tiles.length; i < LETTERS_IN_HAND; i++) {
			bytes[playerIndex * LETTERS_IN_HAND + i] = 0;
		}
	}

	protected boolean checkGameFinished() {
		if (tilesBank.isEmpty()) {
			final List<ScribblePlayerHand> playerHands = getPlayersHands();
			for (ScribblePlayerHand hand : playerHands) {
				if (hand.getTiles().length == 0) {
					return true;
				}
			}
		}
		return false;
	}

	protected short[] processGameFinished() {
		final List<ScribblePlayerHand> hands = getPlayersHands();
		final short[] res = new short[hands.size()];

		int index = 0;
		int wonIndex = -1;
		short wonPoints = 0;
		for (ScribblePlayerHand hand : hands) {
			short losePoints = 0;
			final Tile[] tiles = hand.getTiles();
			if (tiles.length == 0) {
				wonIndex = index;
			} else {
				for (Tile tile : tiles) {
					losePoints += tile.getCost();
				}
			}
			wonPoints += losePoints;
			res[index++] = (short) -losePoints;
		}

		if (wonIndex != -1) {
			res[wonIndex] = wonPoints;
		}
		return res;
	}

	public boolean isBoardTile(int tileNumber) {
		return positions[tileNumber] != null;
	}

	public Tile getBoardTile(int row, int column) {
		int index = row * CELLS_NUMBER + column;
		final int tileNumber = byteToInt(boardTiles[index]);
		if (tileNumber != 0) {
			return tilesBank.getTile(tileNumber - 1);
		}
		return null;
	}


	public int getBankCapacity() {
		return tilesBank.getBankCapacity();
	}

	public int getBankRemained() {
		return tilesBank.getTilesLimit();
	}

	/**
	 * Returns collection of {@code TilesBank.TilesInfo} that describes using tiles bank.
	 * <p/>
	 * Board does not returns {@code TilesBank} directly because user can modify it.
	 *
	 * @return the copy of bank info.
	 */
	public LettersDistribution getLettersDistribution() {
		return tilesBank.getLettersDistribution();
	}

	/**
	 * Returns score engine for this board.
	 *
	 * @return the score engine.
	 */
	public ScoreEngine getScoreEngine() {
		return scoreEngine;
	}

	/* ========= Hibernate helper methods ================== */

	/**
	 * This is Hibernate method for storing board tiles.
	 *
	 * @return the encoded board tiles that should be stored into database.
	 */
	@Lob
	@Type(type = "[B")
	@Column(name = "boardTiles")
	@Access(AccessType.PROPERTY)
	byte[] getBoardTiles() {
		return boardTiles;
	}

	void setBoardTiles(byte[] boardTiles) {
		System.arraycopy(boardTiles, 0, this.boardTiles, 0, boardTiles.length);
	}

	@Lob
	@Type(type = "[B")
	@Column(name = "handTiles")
	@Access(AccessType.PROPERTY)
	byte[] getHandTiles() {
		return handTiles;
	}

	void setHandTiles(byte[] handTiles) {
		System.arraycopy(handTiles, 0, this.handTiles, 0, handTiles.length);
	}

	@Lob
	@Type(type = "[B")
	@Column(name = "redefinitions")
	@Access(AccessType.PROPERTY)
	byte[] getTilesRedefinitions() {
		return tilesRedefinitions.array();
	}

	void setTilesRedefinitions(byte[] tilesRedefinitions) {
		this.tilesRedefinitions.clear();
		this.tilesRedefinitions.put(tilesRedefinitions);
		this.tilesRedefinitions.rewind();
	}

	@Lob
	@Type(type = "[B")
	@Column(name = "moves")
	@Access(AccessType.PROPERTY)
	byte[] getBoardMoves() {
		return boardMoves.array();
	}

	void setBoardMoves(byte[] boardMoves) {
		this.boardMoves.clear();
		this.boardMoves.put(boardMoves);
		this.boardMoves.rewind();
	}
}
