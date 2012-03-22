package wisematches.playground.propose;

import wisematches.personality.Personality;
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
	S getGameSettings();

	/**
	 * Returns date when the proposal was created.
	 *
	 * @return the date when the proposal was created.
	 */
	Date getCreationDate();

	/**
	 * Returns associated with the proposal comment
	 *
	 * @return the associated with the proposal comment
	 */
	String getCommentary();

	/**
	 * Returns a player who has initiated the proposal.
	 *
	 * @return the proposal's initiator.
	 */
	Personality getInitiator();


	/**
	 * Returns total players number for the proposal.
	 * <p/>
	 * If number of joined players should be returned than {@link #getJoinedPlayersCount()} method should be used.
	 *
	 * @return the total number of players.
	 */
	int getPlayersCount();

	/**
	 * Returns list of all (joined and not) players. If a player is not joined some values will be {@code null} in the list.
	 * <p/>
	 * If only joined players list should be returned please use the {@link #getJoinedPlayers()} method.
	 *
	 * @return unmodifiable list with all players in the proposal or null if players are not joined yet.
	 * @see #getJoinedPlayers()
	 */
	List<Personality> getPlayers();


	/**
	 * Returns number of already joined players
	 *
	 * @return the number of already joined players.
	 */
	int getJoinedPlayersCount();

	/**
	 * Returns unmodifiable collection of all joined players.
	 *
	 * @return the unmodifiable collection of all joined players.
	 */
	List<Personality> getJoinedPlayers();


	/**
	 * Checks is specified player waiting but not joined yet.
	 *
	 * @param player the player to be checked
	 * @return {@code true} if player is waited but not joined; {@code false} - otherwise.
	 */
	boolean isPlayerWaiting(Personality player);

	/**
	 * Checks is specified player joined to the game
	 *
	 * @param player the player to be checked.
	 * @return {@code true} if player is joined; {@code false} - otherwise.
	 */
	boolean isPlayerJoined(Personality player);


	/**
	 * Returns {@code true} if proposal is ready.
	 *
	 * @return {@code true} if proposal is ready; {@code false} - otherwise.
	 */
	public boolean isReady();
}
