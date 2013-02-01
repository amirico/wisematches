package wisematches.playground;

import wisematches.core.Personality;
import wisematches.core.PersonalityManager;

import javax.persistence.*;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
public abstract class AbstractGameBoard<S extends GameSettings, H extends AbstractPlayerHand> implements GameBoard<S, H> {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "boardId")
	private long boardId;

	@Embedded
	private S settings;

	@Column(name = "startedDate")
	private Date startedDate;

	@Column(name = "finishedDate")
	private Date finishedDate;

	@OrderColumn(name = "playerIndex")
	@ElementCollection(fetch = FetchType.EAGER)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.ALL)
	@CollectionTable(name = "scribble_player", joinColumns = @JoinColumn(name = "boardId"))
	private List<H> hands;

	@Transient
	private List<Personality> players;

	@Column(name = "playersCount")
	private int playersCount;

	@Column(name = "movesCount")
	private int movesCount = 0;

	@Column(name = "currentPlayerIndex")
	private int currentPlayerIndex = -1;

	@Column(name = "lastMoveTime")
	private Date lastMoveTime;

	@Column(name = "resolution")
	private GameResolution gameResolution = null;

	@Column(name = "rated")
	private boolean rated = true;

	@Embedded
	private GameRelationship relationship;

	@Transient
	private BoardListener gamePlayListener;

	@Transient
	protected final Lock lock = new ReentrantLock();

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
	protected AbstractGameBoard(S settings, Collection<Personality> players, GameRelationship relationship) {
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

	protected void initializePlayers(PersonalityManager playerManager) {
		players = new ArrayList<>(hands.size()); // create new list. It's transient and not stored
		for (H h : hands) {
			final long playerId = h.getPlayerId();
			final Personality player = playerManager.getPerson(playerId);
			if (player == null) {
				throw new IllegalStateException("Player can't be loaded: " + playerId);
			}
			players.add(player);
		}
	}

	@Override
	public S getSettings() {
		return settings;
	}

	@Override
	public long getBoardId() {
		return boardId;
	}

	@Override
	public Date getStartedTime() {
		return startedDate;
	}

	@Override
	public Date getLastMoveTime() {
		return lastMoveTime;
	}

	@Override
	public Date getFinishedTime() {
		return finishedDate;
	}

	@Override
	public GameRelationship getRelationship() {
		return relationship;
	}

	@Override
	public int getMovesCount() {
		lock.lock();
		try {
			return movesCount;
		} finally {
			lock.unlock();
		}
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

	@Override
	public boolean isRated() {
		return rated;
	}

	@Override
	public boolean isActive() {
		lock.lock();
		try {
			return gameResolution == null && !isGameExpired();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public Personality getPlayerTurn() {
		if (currentPlayerIndex < 0) {
			return null;
		}
		return players.get(currentPlayerIndex);
	}

	@Override
	public int getPlayersCount() {
		return hands.size();
	}

	@Override
	public List<Personality> getPlayers() {
		return players;
	}

	@Override
	public H getPlayerHand(Personality player) {
		final int i = players.indexOf(player);
		if (i < 0) {
			return null;
		}
		return hands.get(i);
	}

	@Override
	public GameResolution getResolution() {
		lock.lock();
		try {
			return gameResolution;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public Collection<Personality> getWonPlayers() {
		if (isActive()) {
			return null;
		}

		final Collection<Personality> res = new ArrayList<>();
		for (int i = 0; i < hands.size(); i++) {
			H hand = hands.get(i);
			if (hand.isWinner()) {
				res.add(players.get(i));
			}
		}
		return res;
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
		if (gameResolution != null) {
			throw new GameFinishedException(gameResolution);
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

	protected final void registerGameMove(GameMove move, int points, Date date) {
		move.finalizeMove(points, movesCount, date);
		moves.add(move);
		movesCount = moves.size();
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

			final boolean finished = isGameFinished();
			if (finished) {
				finalizeGame(GameResolution.FINISHED);
				if (gamePlayListener != null) {
					gamePlayListener.gameMoveDone(this, move, moveScore);
					gamePlayListener.gameFinished(this, gameResolution, getWonPlayers());
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
	protected byte selectFirstPlayer(Collection<Personality> players) {
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

	private boolean isGameExpired() {
		return System.currentTimeMillis() - getLastMoveTime().getTime() > settings.getDaysPerMove() * 86400000;
	}


	private void finalizeGame(GameResolution resolution) {
		finishedDate = new Date();
		gameResolution = resolution;

		currentPlayerIndex = -1;
		rated = rated && movesCount >= playersCount * 2;

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
		if (gameResolution != null) {
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
			gamePlayListener.gameFinished(this, gameResolution, getWonPlayers());
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

	/**
	 * Creates a player hand for specified player.
	 *
	 * @param player the player for who hand must be crated.
	 * @return the player's hand.
	 */
	protected abstract H createPlayerHand(Personality player);

/*
	@Override
	public List<GameMove> getGameChanges(long playerId) {
		lock.lock();
		try {
			final ListIterator<GameMove> iterator = moves.listIterator(moves.size());
			if (!iterator.hasPrevious()) {
				return Collections.emptyList();
			}

			GameMove previous = iterator.previous();
			if (previous.getPlayerMove().getPlayerId() == playerId) { // no new moves
				return Collections.emptyList();
			}

			final List<GameMove> res = new ArrayList<>();
			res.add(previous);
			while (iterator.hasPrevious()) {
				previous = iterator.previous();
				if (previous.getPlayerMove().getPlayerId() == playerId) { // no new moves
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
 */
}