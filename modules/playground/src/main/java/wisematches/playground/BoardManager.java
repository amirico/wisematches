package wisematches.playground;

import wisematches.core.Personality;
import wisematches.core.search.SearchFilter;
import wisematches.core.search.SearchManager;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface BoardManager<S extends GameSettings, B extends GameBoard<S, ?>> extends SearchManager<B, GameState, SearchFilter> {
	void addBoardStateListener(BoardStateListener l);

	void removeBoardStateListener(BoardStateListener l);

	/**
	 * Returns game by it's id.
	 *
	 * @param gameId the game id
	 * @return the game by specified id or <code>null</code> if game is unknown.
	 * @throws BoardLoadingException if board can't be loaded by some reasons.
	 */
	B openBoard(long gameId) throws BoardLoadingException;

	/**
	 * Creates new game board with specified settings.
	 *
	 * @param settings the settings for new game
	 * @param players  the list of players.
	 * @return the created game.
	 * @throws BoardCreationException if board can't be created by some reasons.
	 */
	B createBoard(S settings, Collection<? extends Personality> players) throws BoardCreationException;

	/**
	 * Creates new game board with specified settings and relationship.
	 *
	 * @param settings     the settings for new game
	 * @param relationship the relation ship associated with this game.
	 * @param players      the list of players.
	 * @return the created game.
	 * @throws BoardCreationException if board can't be created by some reasons.
	 */
	B createBoard(S settings, GameRelationship relationship, Collection<? extends Personality> players) throws BoardCreationException;
}
