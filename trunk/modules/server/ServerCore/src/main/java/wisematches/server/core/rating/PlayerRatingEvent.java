package wisematches.server.core.rating;

import wisematches.kernel.player.Player;
import wisematches.server.core.board.GameBoard;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerRatingEvent {
    private final Player player;
    private final GameBoard gameBoard;
    private final int oldRating;
    private final int newRating;

    public PlayerRatingEvent(Player player, GameBoard gameBoard, int oldRating, int newRating) {
        this.player = player;
        this.gameBoard = gameBoard;
        this.oldRating = oldRating;
        this.newRating = newRating;
    }

    public Player getPlayer() {
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
