package wisematches.server.gameplaying.propose;

import wisematches.server.gameplaying.board.GameSettings;
import wisematches.server.personality.Personality;

import java.util.Collection;

/**
 * The base interface that contains information about game proposal.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GameProposal<S extends GameSettings> {
	/**
	 * Returns proposal id
	 *
	 * @return the id of this proposal.
	 */
	long getId();

	/**
	 * Returns game settings for this proposal.
	 *
	 * @return the game settings
	 */
	S getGameSettings();

	/**
	 * Returns total number of players
	 *
	 * @return the total number of players
	 */
	int getPlayersCount();

	/**
	 * Returns the game owner
	 *
	 * @return the owner
	 */
	Personality getGameOwner();

	/**
	 * Returns collection of player who already joined to the game
	 *
	 * @return the unmodifiable collection of player who already joined to the game
	 */
	Collection<Personality> getPlayers();
}
