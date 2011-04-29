package wisematches.server.playground.robot;

import wisematches.server.playground.board.GameBoard;
import wisematches.server.playground.room.Room;
import wisematches.server.personality.player.computer.robot.RobotType;

/**
 * The robot's brain. This object can process for a move of robot.
 *
 * @param <B> the game board that support this robot.
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface RobotBrain<B extends GameBoard> {
	/**
	 * Returns room that this brain is supported.
	 *
	 * @return the supported room.
	 */
	Room getRoom();

	/**
	 * Returns array of robot types that this brain is supported.
	 *
	 * @return the array of types.
	 */
	RobotType[] getRobotTypes();

	/**
	 * Activate brains of robot that do a move.
	 *
	 * @param board the board where move should be maiden.
	 * @param type  the type of robot that should made a move.
	 */
	void putInAction(B board, RobotType type);
}
