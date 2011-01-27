package wisematches.server.gameplaying.board;

/**
 * {@code GameMove} is a made and accepted {@code PlayerMove} on the board. This move contains
 * original {@code PlayerMove}, points for maden move, number of move and time when move has been made.
 * <p/>
 * Each move contains it's number and can be taken by this number from board using {@code GameBoard.getGameMove().get(NUMBER)}
 * <p/>
 * {@code GameMove} is {@code Comparable} and compares two moves by move number.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 * @see PlayerMove
 */
public final class GameMove implements Comparable<GameMove> {
	private final PlayerMove playerMove;
	private final int points;
	private final int moveNumber;
	private final long moveTime;

	/**
	 * Creates new {@code GameMove} with specified parameter.
	 *
	 * @param playerMove the maden move.
	 * @param points	 the move points.
	 * @param moveNumber the move number starting with zero.
	 * @param moveTime   the move time.
	 * @throws NullPointerException	 if {@code playerMove} is null
	 * @throws IllegalArgumentException if {@code moveNumber} is negarive or {@code moveTime} is zero.
	 */
	public GameMove(PlayerMove playerMove, int points, int moveNumber, long moveTime) {
		if (playerMove == null) {
			throw new NullPointerException("Player's move is null");
		}
		if (moveNumber < 0) {
			throw new IllegalArgumentException("Move number is negative");
		}
		if (moveTime == 0) {
			throw new IllegalArgumentException("Move time is zero");
		}

		this.playerMove = playerMove;
		this.points = points;
		this.moveNumber = moveNumber;
		this.moveTime = moveTime;
	}

	/**
	 * Returns original player's move.
	 *
	 * @return the original player's move.
	 */
	public PlayerMove getPlayerMove() {
		return playerMove;
	}

	/**
	 * Returns move points.
	 *
	 * @return the move points.
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * Returns move number starting with zero.
	 *
	 * @return the move number starting with zero.
	 */
	public int getMoveNumber() {
		return moveNumber;
	}

	/**
	 * Returns move time.
	 *
	 * @return the move time.
	 */
	public long getMoveTime() {
		return moveTime;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final GameMove gameMove = (GameMove) o;
		return moveNumber == gameMove.moveNumber && moveTime == gameMove.moveTime && points == gameMove.points && playerMove.equals(gameMove.playerMove);
	}

	@Override
	public int hashCode() {
		int result = playerMove.hashCode();
		result = 31 * result + points;
		result = 31 * result + moveNumber;
		result = 31 * result + (int) (moveTime ^ (moveTime >>> 32));
		return result;
	}

	public int compareTo(GameMove o) {
		return moveNumber - o.moveNumber;
	}

	@Override
	public String toString() {
		return "GameMove{" +
				"playerMove=" + playerMove +
				", points=" + points +
				", moveNumber=" + moveNumber +
				", moveTime=" + moveTime +
				'}';
	}
}
