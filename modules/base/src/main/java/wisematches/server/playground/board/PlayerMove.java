package wisematches.server.playground.board;

import java.io.Serializable;

/**
 * This interface represents one move in game. Each move associated with one player and can have
 * many parameters.
 * <p/>
 * This class can't be extended directly. You can extends from two predefined types of move: {@link MakeTurnMove}
 * indicates player tries make a turn and {@link PassTurnMove} indicates that player tries pass turn.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 * @see MakeTurnMove
 * @see PassTurnMove
 */
public abstract class PlayerMove implements Serializable {
	private final long playerId;

	/**
	 * Creates new move for specified player.
	 *
	 * @param playerId the player than made this move.
	 */
	PlayerMove(long playerId) {
		if (playerId == 0) {
			throw new IllegalArgumentException("Player id can't be 0");
		}
		this.playerId = playerId;
	}

	/**
	 * Returns id of the player who made this move.
	 *
	 * @return the id of the player.
	 */
	public long getPlayerId() {
		return playerId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PlayerMove gameMove = (PlayerMove) o;
		return playerId == gameMove.playerId;

	}

	@Override
	public int hashCode() {
		return (int) (playerId ^ (playerId >>> 32));
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{" +
				"playerId=" + playerId +
				'}';
	}
}
