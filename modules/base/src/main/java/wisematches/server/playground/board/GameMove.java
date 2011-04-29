package wisematches.server.playground.board;

import java.util.Date;

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
	private final Date moveTime;

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
	public GameMove(PlayerMove playerMove, int points, int moveNumber, Date moveTime) {
		if (playerMove == null) {
			throw new NullPointerException("Player's move is null");
		}
		if (moveNumber < 0) {
			throw new IllegalArgumentException("Move number is negative");
		}
		if (moveTime == null) {
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
	public Date getMoveTime() {
		return moveTime;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		GameMove gameMove = (GameMove) o;

		if (moveNumber != gameMove.moveNumber) return false;
		if (points != gameMove.points) return false;
		if (!moveTime.equals(gameMove.moveTime)) return false;
		if (!playerMove.equals(gameMove.playerMove)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = playerMove.hashCode();
		result = 31 * result + points;
		result = 31 * result + moveNumber;
		result = 31 * result + moveTime.hashCode();
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
