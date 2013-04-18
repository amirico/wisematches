package wisematches.playground;

import wisematches.core.Personality;

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
 */
public abstract class GameMove {
	private int moveNumber;
	private int points;
	private Date moveTime;

	private final Personality player;

	protected GameMove(Personality player) {
		this.player = player;
	}

	protected GameMove(Personality player, int points, Date moveTime) {
		this(player);
		this.moveTime = moveTime;
		this.points = points;
	}

	public Personality getPlayer() {
		return player;
	}

	public int getPoints() {
		return points;
	}

	public int getMoveNumber() {
		return moveNumber;
	}

	public Date getMoveTime() {
		return moveTime;
	}

	void finalizeMove(int points, int moveNumber, Date moveTime) {
		this.points = points;
		this.moveNumber = moveNumber;
		this.moveTime = moveTime;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("GameMove");
		sb.append("{player=").append(player);
		sb.append(", points=").append(points);
		sb.append(", moveNumber=").append(moveNumber);
		sb.append(", moveTime=").append(moveTime);
		sb.append('}');
		return sb.toString();
	}
}
