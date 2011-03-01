package wisematches.server.gameplaying.board;

import wisematches.server.player.Player;

import javax.persistence.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class contains information about game - game settings, all moves and so on.
 * <p/>
 * {@code AbstractGameBoard} is database entity but has {@code @MappedSuperclass} annotation and subclasses
 * shoud specify {@code Entity} annotation.
 * <p/>
 * This implementation persists the following fields: boardId, players, passesCount,
 * lastMoveTile, gamesState and gameSettings. This implementation does not persist moves and any listeners.
 * Each subclass must do it by itself.
 *
 * @param <S> the type of game settings.
 * @param <P> the type of player hands.
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@MappedSuperclass
public abstract class AbstractGameBoard<S extends GameSettings, P extends GamePlayerHand> implements GameBoard<S, P> {
	/**
	 * The board id. This is primary key for each board.
	 * <p/>
	 * Field is accessible by field in Hibernate.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "boardId")
	private long boardId;

	/**
	 * Collections of all maden turns.
	 * <p/>
	 * This is transient field for Hibernate.
	 */
	@Transient
	protected final List<GameMove> moves = new ArrayList<GameMove>(0);

	@Column(name = "startedDate")
	private Date startedDate;

	@Column(name = "finishedDate")
	private Date finishedDate;

	/**
	 * List of players for this board.
	 * <p/>
	 * This is embedded Hibernate object.
	 */
	@Embedded
	private PlayersIterator<P> playersIterator;

	/**
	 * Count of passed turns. If passed turns count grate three game is draw.
	 */
	@Column(name = "passesCount")
	private int passesCount;

	/**
	 * Time when last move was done.
	 */
	@Column(name = "lastMoveTime")
	private Date lastMoveTime;

	/**
	 * The state of this game.
	 */
	@Column(name = "gameState")
	private GameState gameState = GameState.ACTIVE;

	@Column(name = "rated")
	private boolean rated = true;

	/**
	 * Settings of this game
	 */
	@Embedded
	private S gameSettings;

	@Transient
	private final Collection<GameBoardListener> boardListeners = new CopyOnWriteArrayList<GameBoardListener>();

	private static final int MAX_PASSED_TURNS = 2;

	/**
	 * This is Hibernate constructor that is required for serialization. Visibility of this constructor MUST BE
	 * changed to package visible in superclass.
	 */
	@Deprecated
	protected AbstractGameBoard() {
	}

	/**
	 * Creates new board with specified settings. Game created with this constructor is rated.
	 *
	 * @param gameSettings the game settings.
	 * @param players	  the collection of all players.
	 * @throws NullPointerException if setting is {@code null}
	 */
	@SuppressWarnings("unchecked")
	protected AbstractGameBoard(S gameSettings, Collection<Player> players) {
		if (gameSettings == null) {
			throw new IllegalArgumentException("Settings can't be null");
		}
		if (players == null) {
			throw new IllegalArgumentException("Players can't be null");
		}
		if (players.size() < 2) {
			throw new IllegalArgumentException("Game can't have less than 2 players");
		}

		this.gameSettings = gameSettings;
		this.rated = gameSettings.isRatedGame();

		startedDate = lastMoveTime = new Date();

		int index = 0;
		final List<P> hands = new ArrayList<P>(players.size());
		for (Player player : players) {
			if (player == null) {
				throw new IllegalArgumentException("Players list can't contain null players");
			}
			final P hand = createPlayerHand(player);
			hand.setPlayerIndex(index++);
			hand.setGameBoard(this);
			hands.add(hand);
		}
		playersIterator = new PlayersIterator<P>(hands, selectFirstPlayer(gameSettings, hands));
	}

	/**
	 * {@inheritDoc}
	 */
	public long getBoardId() {
		return boardId;
	}

	public Date getStartedTime() {
		return startedDate;
	}

	public Date getFinishedTime() {
		return finishedDate;
	}

	public void addGameBoardListener(GameBoardListener listener) {
		if (!boardListeners.contains(listener)) {
			boardListeners.add(listener);
		}
	}

	public void removeGameBoardListener(GameBoardListener listener) {
		boardListeners.remove(listener);
	}

	protected void firePlayerMoved(GameMove move) {
		for (GameBoardListener boardListener : boardListeners) {
			boardListener.gameMoveMade(this, move);
		}
	}

	protected void fireGameFinished(P wonPlayer) {
		gameState = GameState.FINISHED;
		for (GameBoardListener boardListener : boardListeners) {
			boardListener.gameFinished(this, wonPlayer);
		}
	}

	protected void fireGameDraw() {
		gameState = GameState.DREW;
		for (GameBoardListener boardListener : boardListeners) {
			boardListener.gameDrew(this);
		}
	}

	protected void fireGameInterrupted(P interrupterPlayer, boolean byTimeout) {
		gameState = GameState.INTERRUPTED;
		for (GameBoardListener boardListener : boardListeners) {
			boardListener.gameInterrupted(this, interrupterPlayer, byTimeout);
		}
	}
	/* ========== End Listeners and Fires ================ */

	/**
	 * {@inheritDoc}
	 */
	public int makeMove(PlayerMove move) throws GameMoveException {
		checkState();

		final P player = getPlayerHand(move.getPlayerId());
		if (player == null) {
			throw new UnsuitablePlayerException("make turn", 0);
		}

		if (player != getPlayerTurn()) {
			throw new UnsuitablePlayerException("make turn", getPlayerTurn().getPlayerId(), player.getPlayerId());
		}

		checkMove(move);

		final int points = calculateMovePoints(move);
		playersIterator.getPlayerTurn().increasePoints(points);
		lastMoveTime = new Date();


		if (move instanceof PassTurnMove) {
			passesCount++;
		} else {
			passesCount = 0;
		}

		final GameMove gameMove = new GameMove(move, points, moves.size(), lastMoveTime);
		moves.add(gameMove);

		processMoveFinished(player, gameMove);

		if (checkGameFinished() || checkGamePassed()) {
			finalizeGame();
			firePlayerMoved(gameMove);

			final P won = getWonPlayer(playersIterator.getPlayerHands());
			if (won == null) {
				fireGameDraw();
			} else {
				fireGameFinished(won);
			}
		} else {
			firePlayerMoved(gameMove);
		}
		return points;
	}

	/**
	 * This method process game finished and increases players points.
	 *
	 * @see #processGameFinished()
	 */
	private void finalizeGame() {
		finishedDate = new Date();

		final int[] ints = processGameFinished();
		final List<P> list = playersIterator.getPlayerHands();
		for (int i = 0; i < ints.length; i++) {
			list.get(i).increasePoints(ints[i]);
		}

		playersIterator.setPlayerTurn(null);
	}

	/**
	 * Checks that board is ready for game and player can make a move.
	 *
	 * @throws GameStateException if game state is not <code>ACTIVE</code>.
	 */
	protected void checkState() throws GameStateException {
		switch (gameState) {
			case DREW:
			case FINISHED:
			case INTERRUPTED:
				throw new GameFinishedException(gameState);
			default:
				;
		}

		if (System.currentTimeMillis() - lastMoveTime.getTime() > gameSettings.getDaysPerMove() * 86400000) {
			throw new GameExpiredException();
		}
	}

	/**
	 * Selects first player for the game.
	 * <p/>
	 * Implementation of this method selects random player from all players in specified list.
	 *
	 * @param gameSettings the game settings.
	 * @param players	  the list of all players to select first.
	 * @return the player who should be first.
	 */
	protected P selectFirstPlayer(S gameSettings, List<P> players) {
		int index = (int) (Math.random() * (players.size() - 1));
		return players.get(index);
	}

	/**
	 * Returns won player for this board.
	 * <p/>
	 * Player is won if it has more points. If two or more players has the same points no one is one and
	 * method returns {@code null}.
	 * <p/>
	 * This method does not check state of game and can return current leader.
	 *
	 * @param players players
	 * @return won player or {@code null} if no one is won.
	 */
	protected P getWonPlayer(List<P> players) {
		P won;

		final Iterator<P> i = players.iterator();
		won = i.next();
		int points = won.getPoints();
		while (i.hasNext()) {
			final P p = i.next();
			if (p.getPoints() > points) {
				points = p.getPoints();
				won = p;
			} else if (p.getPoints() == points) {
				won = null;
			}
		}
		return won;
	}

	/**
	 * Returns won player for this game. This method throws {@code java.lang.IllegalStateException}
	 * if game is not finished yet.
	 *
	 * @return the won player or {@code null} if game was finished with draw.
	 * @throws IllegalStateException if game is not finished yet (has state {@code GameState.IN_PROGRESS}
	 *                               or {@code GameState.WAITING}.
	 */
	public P getWonPlayer() {
		if (gameState == GameState.ACTIVE) {
			throw new IllegalStateException("Game not finished yet");
		}
		return getWonPlayer(playersIterator.getPlayerHands());
	}

	/**
	 * {@inheritDoc}
	 */
	public GameState getGameState() {
		return gameState;
	}

	/**
	 * {@inheritDoc}
	 */
	public P getPlayerTurn() {
		return playersIterator.getPlayerTurn();
	}

	/**
	 * {@inheritDoc}
	 */
	public List<GameMove> getGameMoves() {
		return Collections.unmodifiableList(moves);
	}

	/**
	 * Return time of last move in millisecods.
	 *
	 * @return the time of last move in milliseconds.
	 */
	public Date getLastMoveTime() {
		return lastMoveTime;
	}

	/**
	 * {@inheritDoc}
	 */
	public P getPlayerHand(long playerId) {
		for (P playerHand : playersIterator.getPlayerHands()) {
			if (playerHand.getPlayerId() == playerId) {
				return playerHand;
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<P> getPlayersHands() {
		return playersIterator.getPlayerHands();
	}

	/**
	 * {@inheritDoc}
	 */
	public S getGameSettings() {
		return gameSettings;
	}

	/**
	 * Game is rated if it's was creted as rated.
	 * <p/>
	 * Game is not rated if any player has made less than two moves.
	 *
	 * @return {@code true} if game is rated; {@code false} - otherwise.
	 */
	public boolean isRatedGame() {
		return rated;
	}

	/**
	 * {@inheritDoc}
	 */
	public void terminate() throws GameMoveException {
		final P p = getPlayerTurn();
		if (p != null) {
			closeImpl(p, true);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void close(P player) throws GameMoveException {
		closeImpl(player, false);
	}

	private void closeImpl(P player, boolean byTimeout) throws GameMoveException {
		final long playerId = player.getPlayerId();
		final P hand = getPlayerHand(playerId);
		if (hand != player) {
			throw new UnsuitablePlayerException("close game", playerId);
		}

		hand.increasePoints(-hand.getPoints()); // Clear player points...

		if (rated && moves.size() + passesCount < playersIterator.size() * 2) {
			rated = false;
		}

		if (gameState == GameState.ACTIVE) {
			gameState = GameState.INTERRUPTED;
			finalizeGame();

			//According to requirements if game was interrupted when terminator should be set as a current player.
			playersIterator.setPlayerTurn(hand);

			fireGameInterrupted(hand, byTimeout);
		}
	}

	/**
	 * Returns number of passed turns.
	 *
	 * @return the number of passed turns.
	 */
	public int getPassesCount() {
		return passesCount;
	}

	/*================== Checks game state =================*/

	/**
	 * Checks that game was passed.
	 *
	 * @return <code>true</code> if game was passed; <code>otherwise</code>
	 */
	protected boolean checkGamePassed() {
		return passesCount / playersIterator.getPlayerHands().size() >= MAX_PASSED_TURNS;
	}

	/**
	 * Creates a player hand for specified player.
	 *
	 * @param player the player for who hand must be crated.
	 * @return the player's hand.
	 */
	protected abstract P createPlayerHand(Player player);

	/**
	 * Checks specified move. If move isn't correct or can't be maden appropriate exception will be thrown.
	 *
	 * @param move the move for checking.
	 * @throws IncorrectMoveException if move is incorrect.
	 */
	protected abstract void checkMove(PlayerMove move) throws IncorrectMoveException;

	/**
	 * Calculate and returns move points.
	 *
	 * @param move the move to be calculated.
	 * @return the move points.
	 */
	protected abstract int calculateMovePoints(PlayerMove move);

	/**
	 * Indicates that move was accepted. When this method is called move already added to game moves list.
	 *
	 * @param player   the player who made move
	 * @param gameMove the game move
	 */
	protected abstract void processMoveFinished(P player, GameMove gameMove);

	/**
	 * Checks that no one valid move is allowed.
	 *
	 * @return <code>true</code> if no any allowed moves; <code>false</code> - otherwise.
	 */
	protected abstract boolean checkGameFinished();

	/**
	 * Indicates that game was finished with specified status.
	 *
	 * @return the points for players in order of <code>#getPlayerHands</code>.
	 * @see #getPlayersHands()
	 */
	protected abstract int[] processGameFinished();
}