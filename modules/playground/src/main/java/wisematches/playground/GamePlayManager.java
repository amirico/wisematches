package wisematches.playground;

import wisematches.core.personality.Player;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GamePlayManager<S extends GameSettings, B extends GameBoard<S, ?>> {
	void addGamePlayListener(GamePlayListener l);

	void removeGamePlayListener(GamePlayListener l);


	/**
	 * Returns game by it's id.
	 *
	 * @param boardId the game id
	 * @return the game by specified id or <code>null</code> if game is unknown.
	 * @throws BoardLoadingException if board can't be loaded by some reasons.
	 */
	B getBoard(long boardId) throws BoardLoadingException;

	/**
	 * Creates new game board with specified settings.
	 *
	 * @param settings the settings for new game
	 * @param players  the list of players.
	 * @return the created game.
	 * @throws BoardCreationException if board can't be created by some reasons.
	 */
	B createBoard(S settings, Collection<Player> players) throws BoardCreationException;

	/**
	 * Creates new game board with specified settings and relationship.
	 *
	 * @param settings     the settings for new game
	 * @param players      the list of players.
	 * @param relationship the relation ship associated with this game.
	 * @return the created game.
	 * @throws BoardCreationException if board can't be created by some reasons.
	 */
	B createBoard(S settings, Collection<Player> players, GameRelationship relationship) throws BoardCreationException;
}
