package wisematches.playground.propose;

import wisematches.core.Personality;
import wisematches.core.Player;
import wisematches.playground.GameSettings;

import java.util.Date;
import java.util.List;

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
	S getSettings();

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
	Player getInitiator();

	/**
	 * Returns list of all joined or waiting players.
	 *
	 * @return the list of all joined or waiting players.
	 */
	List<Player> getPlayers();

	/**
	 * Returns list of all joined players.
	 *
	 * @return the list of all joined players.
	 */
	List<Player> getJoinedPlayers();


	/**
	 * Checks that specified player can join to this proposal.
	 *
	 * @param player the player to be checked.
	 * @return {@code true} if player can join to the game; {@code false} - otherwise.
	 */
	boolean validatePlayer(Player player);


	/**
	 * Checks if the proposal contains specified player, or not.
	 *
	 * @param player the player to be checked.
	 * @return {@code true} if the proposal contains specified player; {@code false} - otherwise.
	 */
	boolean containsPlayer(Personality player);


	/**
	 * Returns {@code true} if proposal is ready.
	 *
	 * @return {@code true} if proposal is ready; {@code false} - otherwise.
	 */
	public boolean isReady();


	/**
	 * Returns type of the proposal. Depends on the type a proposal can have differ behaviour.
	 *
	 * @return the type of the proposal.
	 */
	ProposalType getProposalType();
}
