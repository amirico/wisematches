package wisematches.server.gameplaying.room;

import wisematches.server.gameplaying.board.GameBoard;
import wisematches.server.player.Player;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RoomSeatesEvent {
	private final Room room;
	private final GameBoard gameBoard;
	private final Player player;

	public RoomSeatesEvent(Room room, GameBoard gameBoard, Player player) {
		this.room = room;
		this.gameBoard = gameBoard;
		this.player = player;
	}

	public Room getRoom() {
		return room;
	}

	public GameBoard getGameBoard() {
		return gameBoard;
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public String toString() {
		return "RoomSeatesEvent{" +
				"room=" + room +
				", gameBoard=" + gameBoard +
				", player=" + player +
				'}';
	}
}
