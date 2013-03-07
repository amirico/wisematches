package wisematches.playground;

import wisematches.core.Personality;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.*;

/**
 * This class contains information about game - game settings, all moves and so on.
 * <p/>
 * {@code AbstractGameBoard} is database entity but has {@code @MappedSuperclass} annotation and subclasses
 * shoud specify {@code Entity} annotation.
 * <p/>
 * This implementation persists the following fields: boardId, players, passesCount,
 * lastMoveTile, gamesState and settings. This implementation does not persist moves and any listeners.
 * Each subclass must do it by itself.
 *
 * @param <S> the type of game settings.
 * @param <H> the type of player hands.
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@MappedSuperclass
public abstract class AbstractGameBoard<S extends GameSettings, H extends AbstractPlayerHand> extends AbstractBoardDescription<S, H> implements GameBoard<S, H> {
	@Transient
	private BoardListener gamePlayListener;

	@Transient
	private final List<GameMove> moves = new ArrayList<>();

	private static final Random FIRST_PLAYER_RANDOM = new Random();

	/**
	 * This is Hibernate constructor that is required for serialization. Visibility of this constructor MUST BE
	 * changed to package visible in superclass.
	 */
	protected AbstractGameBoard() {
	}

	/**
	 * Creates new board with specified settings. Game created with this constructor is rated.
	 *
	 * @param settings the game settings.
	 * @param players  the collection of all players.
	 * @throws NullPointerException if setting is {@code null}
	 */
	@SuppressWarnings("unchecked")
	protected AbstractGameBoard(S settings, Collection<? extends Personality> players, GameRelationship relationship) {
		if (settings == null) {
			throw new IllegalArgumentException("Settings can't be null");
		}
		if (players == null) {
			throw new IllegalArgumentException("Players can't be null");
		}
		if (players.size() < 2) {
			throw new IllegalArgumentException("Game can't have less than 2 players");
		}

		this.settings = settings;
		this.relationship = relationship;
		this.rated = settings.isRatedGame();

		playersCount = players.size();
		startedDate = lastMoveTime = new Date();

		this.hands = new ArrayList<>(playersCount);
		this.players = new ArrayList<>(playersCount);
		for (Personality player : players) {
			if (player == null) {
				throw new IllegalArgumentException("Players list can't contain null players");
			}
			this.players.add(player);
			this.hands.add(createPlayerHand(player));
		}
		currentPlayerIndex = selectFirstPlayer(players);
	}


	@Override
	public List<GameMove> getGameMoves() {
		lock.lock();
		try {
			return moves;
		} finally {
			lock.unlock();
		}
	}

	public void terminate() throws GameMoveException {
		lock.lock();
		try {
			try {
				checkState();
			} catch (GameExpiredException ex) { //terminate if expired
				final Personality player = getPlayerTurn();
				if (player != null) {
					closeImpl(player, true);
				}
			} catch (GameFinishedException ignore) {
			}
		} finally {
			lock.unlock();
		}
	}

	@Override
	public final void resign(Personality player) throws BoardUpdatingException {
		lock.lock();
		try {
			closeImpl(player, false);
		} finally {
			lock.unlock();
		}
	}


	void setBoardListener(BoardListener gamePlayListener) {
		this.gamePlayListener = gamePlayListener;
	}

	/**
	 * Checks that board is ready for game and player can make a move.
	 *
	 * @throws GameStateException if game state is not <code>ACTIVE</code>.
	 */
	protected void checkState() throws GameStateException {
		if (resolution != null) {
			throw new GameFinishedException(resolution);
		}

		if (isGameExpired()) {
			throw new GameExpiredException();
		}
	}

	protected void validatePlayerMove(Personality personality) throws GameMoveException {
		checkState();

		if (!personality.equals(getPlayerTurn())) {
			throw new UnsuitablePlayerException("player.move", personality);
		}
	}


	protected <M extends GameMove> M finalizePlayerMove(M move, GameMoveScore moveScore) throws GameMoveException {
		lock.lock();
		try {
			validatePlayerMove(move.getPlayer());

			final H hand = getPlayerHand(move.getPlayer());

			final short points = moveScore != null ? moveScore.getPoints() : 0;
			hand.increasePoints(points);
			lastMoveTime = new Date();

			registerGameMove(move, points, lastMoveTime);
			processMoveDone(move);

			final boolean finished = isGameFinished();
			if (finished) {
				finalizeGame(GameResolution.FINISHED);
				if (gamePlayListener != null) {
					gamePlayListener.gameMoveDone(this, move, moveScore);
					gamePlayListener.gameFinished(this, resolution, getWonPlayers());
				}
			} else {
				currentPlayerIndex = getNextPlayerIndex();
				if (gamePlayListener != null) {
					gamePlayListener.gameMoveDone(this, move, moveScore);
				}
			}
			return move;
		} finally {
			lock.unlock();
		}
	}

	protected final <M extends GameMove> void registerGameMove(M move, int points, final Date date) {
		move.finalizeMove(points, moves.size(), date);
		moves.add(move);
		movesCount = moves.size();
	}


	protected byte getPlayerCode(Personality player) {
		return (byte) players.indexOf(player);
	}

	protected Personality getPlayerByCode(byte code) {
		return players.get(code);
	}

	/**
	 * Selects first player for the game.
	 * <p/>
	 * Implementation of this method selects random player from all players in specified list.
	 *
	 * @param players the list of all players to select first.
	 * @return the player who should be first.
	 */
	protected byte selectFirstPlayer(Collection<? extends Personality> players) {
		return (byte) FIRST_PLAYER_RANDOM.nextInt(players.size());
	}


	private int getNextPlayerIndex() {
		if (currentPlayerIndex == -1) {
			return -1;
		} else if (currentPlayerIndex + 1 == hands.size()) {
			return 0;
		} else {
			return currentPlayerIndex + 1;
		}
	}


	private void finalizeGame(GameResolution resolution) {
		this.finishedDate = new Date();
		this.resolution = resolution;

		this.currentPlayerIndex = -1;
		this.rated = rated && movesCount >= playersCount * 2;

		int cnt = 0;
		int maxPoints = 0; // select max points
		final short[] ints = processGameFinished();
		for (int i = 0; i < ints.length; i++) {
			final H h = hands.get(i);
			h.increasePoints(ints[i]);

			final short p = h.getPoints();
			if (p > maxPoints) {
				cnt = 0;
				maxPoints = p;
			}

			if (p == maxPoints) {
				cnt++;
			}
		}

		if (cnt != hands.size()) {
			for (H hand : hands) {
				if (hand.getPoints() == maxPoints) {
					hand.markAsWinner();
				}
			}
		}
	}

	private void closeImpl(Personality player, boolean byTimeout) throws GameMoveException {
		if (resolution != null) {
			return;
		}
		final H hand = getPlayerHand(player);
		if (hand == null) {
			throw new UnsuitablePlayerException("Player does not belong to this game and can't resign it", player);
		}

		hand.increasePoints((short) -hand.getPoints()); // Clear player points...

		finalizeGame(byTimeout ? GameResolution.INTERRUPTED : GameResolution.RESIGNED);

		//According to requirements if game was interrupted when terminator should be set as a current player.
		currentPlayerIndex = players.indexOf(player);
		if (gamePlayListener != null) {
			gamePlayListener.gameFinished(this, resolution, getWonPlayers());
		}
	}

	/**
	 * Checks is game already finished or move moves can be done in the game.
	 *
	 * @return {@code true} is game is finished; {@code false} - if can should continue.
	 */
	protected abstract boolean isGameFinished();

	/**
	 * Indicates that game was finished with specified status.
	 *
	 * @return the points for players in order of <code>#getPlayers</code>.
	 * @see #getPlayers()
	 */
	protected abstract short[] processGameFinished();

	protected abstract void processMoveDone(GameMove move);

	/**
	 * Creates a player hand for specified player.
	 *
	 * @param player the player for who hand must be crated.
	 * @return the player's hand.
	 */
	protected abstract H createPlayerHand(Personality player);

	@Override
	public List<GameMove> getGameChanges(Personality player) {
		lock.lock();
		try {
			final ListIterator<GameMove> iterator = moves.listIterator(moves.size());
			if (!iterator.hasPrevious()) {
				return Collections.emptyList();
			}

			GameMove previous = iterator.previous();
			if (previous.getPlayer().equals(player)) { // no new moves
				return Collections.emptyList();
			}

			final List<GameMove> res = new ArrayList<>();
			res.add(previous);
			while (iterator.hasPrevious()) {
				previous = iterator.previous();
				if (previous.getPlayer().equals(player)) { // no new moves
					break;
				}
				res.add(previous);
			}
			Collections.reverse(res);
			return res;
		} finally {
			lock.unlock();
		}
	}
}
