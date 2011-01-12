package wisematches.server.games.board;

import javax.persistence.*;

/**
 * <code>GamePlayerHand</code> is a hand of the player. It contains information about player on the board, like
 * it's point, it's items in hand and so on.
 *
 * @param <B> reference to GameBoard that can use this hand. This reference required for Hibernate mapping because
 *            each hand must have link to a board that contains it.
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@MappedSuperclass
public class GamePlayerHand<B extends GameBoard<?, ? extends GamePlayerHand<B>>> {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne
	// No cascade. In other case we try to persist
	@JoinColumn(name = "boardId", updatable = false)
	private B gameBoard;

	@Column(name = "playerId", updatable = false)
	private long playerId;

	@Column(name = "playerIndex", updatable = false)
	private int playerIndex;

	@Column(name = "points")
	private int points;

	@Column(name = "ratingDelta")
	private int ratingDelta;

	@Column(name = "previousRating")
	private int previousRating;

	/**
	 * This is Hibernate constructor. In subclasses in must be declared as package visibile.
	 */
	protected GamePlayerHand() {
	}

	/**
	 * Creates new player hand with specified player id.
	 *
	 * @param playerId the player id.
	 */
	public GamePlayerHand(long playerId) {
		this.playerId = playerId;
	}

	/**
	 * Creates new player hand with specified player id and statr points.
	 *
	 * @param playerId the player id.
	 * @param points   the start points.
	 */
	public GamePlayerHand(long playerId, int points) {
		this(playerId);
		this.points = points;
	}

	public GamePlayerHand(long playerId, int points, int previousRating, int ratingDelta) {
		this.playerId = playerId;
		this.ratingDelta = ratingDelta;
		this.previousRating = previousRating;
		this.points = points;
	}

	public long getId() {
		return id;
	}

	/**
	 * Returns player id who has this hand.
	 *
	 * @return the player id
	 */
	public long getPlayerId() {
		return playerId;
	}

	/**
	 * Returns points of the player on the board.
	 *
	 * @return the player's points
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * Returns permanent unique index of this player on the board.
	 * <p/>
	 * Each player has it own index that doesn't changed during restarting.
	 *
	 * @return the permanent unique player index.
	 */
	public int getPlayerIndex() {
		return playerIndex;
	}

	public int getRatingDelta() {
		return ratingDelta;
	}

	public int getPreviousRating() {
		return previousRating;
	}

	public void updateRating(int previousRating, int ratingDelta) {
		this.previousRating = previousRating;
		this.ratingDelta = ratingDelta;
	}

	public int getRating() {
		return previousRating + ratingDelta;
	}

	/**
	 * Increases points of the player and returns new value.
	 *
	 * @param delta the delta
	 * @return increased points.
	 */
	int increasePoints(int delta) {
		points = points + delta;
		return points;
	}

	/**
	 * Changes this player index.
	 *
	 * @param playerIndex the new player index.
	 */
	void setPlayerIndex(int playerIndex) {
		this.playerIndex = playerIndex;
	}

	B getGameBoard() {
		return gameBoard;
	}

	void setGameBoard(B gameBoard) {
		this.gameBoard = gameBoard;
	}


	@Override
	public String toString() {
		return "playerId=" + playerId +
				", playerIndex=" + playerIndex +
				", points=" + points;
	}
}
