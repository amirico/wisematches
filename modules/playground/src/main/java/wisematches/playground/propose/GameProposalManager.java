package wisematches.playground.propose;

import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.playground.GameSettings;

import java.util.Collection;

/**
 * {@code GameProposalManager} provide access to waiting games information.
 * <p/>
 * Waiting games don't have a board and can be managed via this manager only.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GameProposalManager<S extends GameSettings> {
	void addGameProposalListener(GameProposalListener l);

	void removeGameProposalListener(GameProposalListener l);

	/**
	 * Opens new waiting game in this manager.
	 *
	 * @param settings	 the original game settings
	 * @param initiator	a player who has initiated that proposal.
	 * @param playersCount the max players count
	 * @param restriction  the restriction for the proposal.  @return initiated proposal.
	 * @return created game proposal.
	 * @throws IllegalArgumentException if {@code settings} or {@code players} are null or if {@code playersCount}
	 *                                  is less than two or if {@code players} collection size more than
	 *                                  {@code playersCount} or contains null.
	 */
	GameProposal<S> initiateWaitingProposal(S settings, Player initiator, int playersCount, GameRestriction restriction);

	/**
	 * Initializes new challenge proposal.
	 *
	 * @param settings  the game settings
	 * @param comment   the comment for the challenge
	 * @param initiator the proposal initiator.
	 * @param opponents the other opponents   @return created game proposal.
	 * @return the initiated challenge
	 */
	GameProposal<S> initiateChallengeProposal(S settings, String comment, Player initiator, Collection<Player> opponents);

	/**
	 * Attaches specified player to the waiting game.
	 * <p/>
	 * If game is marked as a ready after attachment the waiting info is removed from the manager so the returned
	 * waiting info must be checked after this method execution.
	 *
	 * @param proposalId the waiting game id
	 * @param player	 the player to be added.
	 * @return the modified waiting game info or {@code null} if no proposal with specified id.
	 * @throws IllegalArgumentException	 if specified player is null.
	 * @throws ViolatedRestrictionException if game can't initialized because specified player is not suitable: {@link GameProposal#isSuitablePlayer(Player)}
	 */
	GameProposal<S> attachPlayer(long proposalId, Player player) throws ViolatedRestrictionException;

	/**
	 * Detaches specified player from the waiting game.
	 *
	 * @param proposalId the waiting game id.
	 * @param player	 the player to be attached.
	 * @return the modified waiting game info or {@code null} if no proposal with specified id.
	 * @throws IllegalArgumentException	 if specified player is null.
	 * @throws ViolatedRestrictionException if game can't initialized because specified player is not suitable: {@link GameProposal#isSuitablePlayer(Player)}
	 */
	GameProposal<S> detachPlayer(long proposalId, Player player) throws ViolatedRestrictionException;

	/**
	 * Cancels a game proposal with specified {@code proposalId}. A proposal can be cancelled only
	 * by person who initialised a waiting proposal of by anyone who in a challenge proposal.
	 *
	 * @param proposalId the proposal id
	 * @param player	 a player who wants cancel the proposal.
	 * @return cancelled game proposal or {@code null} if there is no proposals with specified id.
	 * @throws ViolatedRestrictionException if specified player can't cancel the proposal.
	 */
	GameProposal<S> cancel(long proposalId, Player player) throws ViolatedRestrictionException;

	/**
	 * Returns proposal with specified id or {@code null} if there is no such proposal.
	 *
	 * @param proposalId the proposal id.
	 * @return proposal with specified id or {@code null} if there is no such proposal
	 */
	GameProposal<S> getProposal(long proposalId);

	/**
	 * Returns list of all waiting games.
	 *
	 * @return the list of all waiting games.
	 */
	Collection<GameProposal<S>> getActiveProposals();

	/**
	 * Returns list of all active proposals for specified player
	 *
	 * @param player the player who's proposals should be returned
	 * @return unmodifiable list of all active player's proposals.
	 */
	Collection<GameProposal<S>> getPlayerProposals(Personality player);
}