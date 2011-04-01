package wisematches.server.standing.old.rating;

import wisematches.server.gameplaying.board.GameBoard;
import wisematches.server.personality.Personality;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerRatingEvent {
	private final Personality player;
	private final GameBoard gameBoard;
	private final int oldRating;
	private final int newRating;

	public PlayerRatingEvent(Personality player, GameBoard gameBoard, int oldRating, int newRating) {
		this.player = player;
		this.gameBoard = gameBoard;
		this.oldRating = oldRating;
		this.newRating = newRating;
	}

	public Personality getPlayer() {
		return player;
	}

	public GameBoard getGameBoard() {
		return gameBoard;
	}

	public int getOldRating() {
		return oldRating;
	}

	public int getNewRating() {
		return newRating;
	}
}
