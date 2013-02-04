package wisematches.playground;

import wisematches.core.Personality;
import wisematches.core.RobotType;

import java.util.Collection;
import java.util.Set;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GamePlayManager<S extends GameSettings, B extends GameBoard<S, ?>> extends BoardManager<S, B> {
	/**
	 * Returns set of supported robots by this player.
	 *
	 * @return the unmodifiable set of supported robots by this player.
	 */
	Set<RobotType> getSupportedRobots();

	/**
	 * Creates new board with machinery opponent.
	 *
	 * @param settings  the board settings.
	 * @param player    the player who would like play with robot.
	 * @param robotType the type of the robot.
	 * @return created board.
	 * @throws BoardCreationException
	 */
	B createBoard(S settings, Personality player, RobotType robotType) throws BoardCreationException;

	/**
	 * Creates new game board with specified settings and relationship.
	 *
	 * @param settings     the settings for new game
	 * @param players      the list of players.
	 * @param relationship the relation ship associated with this game.
	 * @return the created game.
	 * @throws BoardCreationException if board can't be created by some reasons.
	 */
	B createBoard(S settings, Collection<? extends Personality> players, GameRelationship relationship) throws BoardCreationException;
}
