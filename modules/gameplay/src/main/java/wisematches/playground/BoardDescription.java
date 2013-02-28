package wisematches.playground;

import wisematches.core.Personality;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface BoardDescription<S extends GameSettings, H extends GamePlayerHand> {
	/**
	 * Returns id of this board.
	 *
	 * @return the id of this board.
	 */
	long getBoardId();

	/**
	 * Returns settings of this game.
	 *
	 * @return the settings of this game.
	 */
	S getSettings();


	/**
	 * Indicates should this game be rated after finished.
	 * <p/>
	 * Any game can be created as no rated or this flag can be enabled if game was terminated and should not be rated.
	 *
	 * @return {@code true} is game should be rated; {@code false} - otherwise.
	 */
	boolean isRated();

	/**
	 * Returns {@code true} if game is active and {@code false} if it's finished or expired.
	 *
	 * @return {@code true} if game is active; {@code false} - otherwise.
	 */
	boolean isActive();


	/**
	 * Returns time when game was started.
	 *
	 * @return return time when game was started or {@code null} if game was not started yet.
	 */
	Date getStartedDate();

	/**
	 * Returns time when game was finished.
	 *
	 * @return return time when game was finished or {@code null} if game is not finished yet.
	 */
	Date getFinishedDate();


	/**
	 * Returns number of done moves
	 *
	 * @return the number of done moves
	 */
	int getMovesCount();

	/**
	 * Return time of last move in milliseconds.
	 *
	 * @return the time of last move in milliseconds.
	 */
	Date getLastMoveTime();


	/**
	 * Returns number of player on this board.
	 *
	 * @return the number of player on this board.
	 */
	int getPlayersCount();

	/**
	 * Returns player that has a turn or player who made last turn. For finished stalemate games this method returns
	 * {@code null}.
	 *
	 * @return the player that has a turn or player who made last turn. For finished stalemate games this method returns
	 *         {@code null}.
	 */
	Personality getPlayerTurn();

	/**
	 * Returns unmodifiable collection of all players in this game.
	 *
	 * @return the list of all players on this board.
	 */
	List<Personality> getPlayers();

	/**
	 * Returns player's hand by player's id.
	 *
	 * @param player the player who's hand should be returned.
	 * @return the player's hand.
	 */
	H getPlayerHand(Personality player);


	/**
	 * Returns the game resolution after finish or {@code null} if game is active.
	 *
	 * @return the game resolution if game has been finished or {@code null} if it still active.
	 */
	GameResolution getResolution();

	/**
	 * Returns related relationship object for this game. Can be null if there is no relationships.
	 *
	 * @return the relationship object for this game.
	 */
	GameRelationship getRelationship();


	/**
	 * Returns collection of won players or {@code null} if game is not finished.
	 *
	 * @return the collection of won players or {@code null} if game is not finished.
	 */
	Collection<Personality> getWonPlayers();
}
