package wisematches.playground.propose;

import wisematches.personality.Personality;
import wisematches.playground.GameSettings;

import java.util.Collection;
import java.util.Date;

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
	 * Returns date when the proposal was created.
	 *
	 * @return the date when the proposal was created.
	 */
	Date getCreationDate();

	/**
	 * Returns a player who has initiated the proposal.
	 *
	 * @return the proposal's initiator.
	 */
	Personality getInitiator();


	/**
	 * Returns number of players for the game.
	 *
	 * @return the expected number of players.
	 */
	int getExpectedPlayersCount();

	/**
	 * Returns unmodifiable collection of players who already joined to the game. Initiator always in the
	 * collection.
	 *
	 * @return unmodifiable collection of players who already joined to the game.
	 */
	Collection<Personality> getJoinedPlayers();

	/**
	 * Checks is specified player joined to the game
	 *
	 * @param player the player to be checked.
	 * @return {@code true} if player is joined; {@code false} - otherwise.
	 */
	boolean isPlayerJoined(Personality player);


	/**
	 * Returns type of the proposal.
	 *
	 * @return the type of the proposal.
	 */
	ProposalType getProposalType();
}
