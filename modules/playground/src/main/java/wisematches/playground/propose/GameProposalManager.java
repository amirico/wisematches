package wisematches.playground.propose;

import wisematches.personality.player.Player;
import wisematches.playground.GameSettings;
import wisematches.playground.criteria.CriterionViolation;
import wisematches.playground.criteria.PlayerCriterion;
import wisematches.playground.criteria.ViolatedCriteriaException;
import wisematches.playground.search.SearchManager;

import java.util.Collection;

/**
 * {@code GameProposalManager} provide access to waiting games information.
 * <p/>
 * Waiting games don't have a board and can be managed via this manager only.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GameProposalManager<S extends GameSettings> extends SearchManager<GameProposal<S>, Void> {
	void addGameProposalListener(GameProposalListener l);

	void removeGameProposalListener(GameProposalListener l);


	/**
	 * Initializes new challenge proposal.
	 *
	 * @param initiator the proposal initiator.
	 * @param settings  the game settings
	 * @param comment   the comment for the challenge
	 * @param opponents the other opponents   @return created game proposal.
	 * @return the initiated challenge
	 */
	GameProposal<S> initiateProposal(Player initiator, S settings, String comment, Collection<Player> opponents);

	/**
	 * Opens new waiting game in this manager.
	 *
	 * @param initiator	a player who has initiated that proposal.
	 * @param settings	 the original game settings
	 * @param playersCount the max players count
	 * @param criteria	 the array of proposal criteria. Can be null if there is no any.
	 * @return created game proposal.
	 * @throws IllegalArgumentException if {@code settings} or {@code players} are null or if {@code playersCount}
	 *                                  is less than two or if {@code players} collection size more than
	 *                                  {@code playersCount} or contains null.
	 */
	GameProposal<S> initiateProposal(Player initiator, S settings, int playersCount, PlayerCriterion... criteria);


	/**
	 * Returns proposal with specified id or {@code null} if there is no such proposal.
	 *
	 * @param proposalId the proposal id.
	 * @return proposal with specified id or {@code null} if there is no such proposal
	 */
	GameProposal<S> getProposal(long proposalId);

	/**
	 * Cancels a game proposal with specified {@code proposalId}. A proposal can be cancelled only
	 * by person who initialised a waiting proposal of by anyone who in a challenge proposal.
	 *
	 * @param proposalId the proposal id
	 * @param player	 a player who wants cancel the proposal.
	 * @return cancelled game proposal or {@code null} if there is no proposals with specified id.
	 * @throws ViolatedCriteriaException if specified player can't cancel the proposal.
	 */
	GameProposal<S> cancel(long proposalId, Player player) throws ViolatedCriteriaException;


	/**
	 * Attaches specified player to the waiting game.
	 * <p/>
	 * If game is marked as a ready after attachment the waiting info is removed from the manager so the returned
	 * waiting info must be checked after this method execution.
	 *
	 * @param proposalId the waiting game id
	 * @param player	 the player to be added.
	 * @return the modified waiting game info or {@code null} if no proposal with specified id.
	 * @throws IllegalArgumentException  if specified player is null.
	 * @throws ViolatedCriteriaException if player can't be attached because the player is not suitable
	 *                                   (@link #checkViolation(Player, GameProposal))
	 */
	GameProposal<S> attachPlayer(long proposalId, Player player) throws ViolatedCriteriaException;

	/**
	 * Detaches specified player from the waiting game. If player is not in the proposal nothing is happen.
	 *
	 * @param proposalId the waiting game id.
	 * @param player	 the player to be attached.
	 * @return the modified waiting game info or {@code null} if no proposal with specified id.
	 * @throws IllegalArgumentException  if specified player is null.
	 * @throws ViolatedCriteriaException if player can't be detached from the proposal.
	 */
	GameProposal<S> detachPlayer(long proposalId, Player player) throws ViolatedCriteriaException;


	/**
	 * Checks that the player doesn't violate criteria of the proposal.
	 *
	 * @param player   the player to be checked
	 * @param proposal the proposal to be checked
	 * @return unmodifiable collection of violated criteria or {@code null} if there no such.
	 */
	Collection<CriterionViolation> checkViolation(Player player, GameProposal<S> proposal);
}