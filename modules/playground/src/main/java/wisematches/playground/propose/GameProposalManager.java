package wisematches.playground.propose;

import wisematches.core.Player;
import wisematches.core.search.SearchFilter;
import wisematches.core.search.SearchManager;
import wisematches.playground.GameSettings;
import wisematches.playground.propose.criteria.ViolatedCriteriaException;

import java.util.Collection;

/**
 * {@code GameProposalManager} provide access to waiting games information.
 * <p/>
 * Waiting games don't have a board and can be managed via this manager only.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GameProposalManager<S extends GameSettings> extends SearchManager<GameProposal<S>, ProposalRelation, SearchFilter> {
	void addGameProposalListener(GameProposalListener l);

	void removeGameProposalListener(GameProposalListener l);


	/**
	 * Initiates new waiting proposal.
	 *
	 * @param settings       the original game settings
	 * @param initiator      a player who has initiated that proposal.
	 * @param opponentsCount number of expected players
	 * @param criteria       the array of proposal criteria. Can be null if there is no any.
	 * @return created game proposal.
	 * @throws IllegalArgumentException if {@code settings} or {@code players} are null or if {@code playersCount}
	 *                                  is less than two or if {@code players} collection size more than
	 *                                  {@code playersCount} or contains null.
	 */
	PublicProposal<S> initiate(S settings, Player initiator, int opponentsCount, PlayerCriterion... criteria);

	/**
	 * Initiates new challenge if it's possible.
	 *
	 * @param settings   the game settings
	 * @param commentary the comment for the challenge
	 * @param initiator  the proposal initiator.
	 * @param opponents  the other opponents   @return created game proposal.
	 * @return the initiated challenge
	 * @throws NullPointerException     if any parameter except {@code commentary} is null.
	 * @throws IllegalArgumentException if {@code opponents} collection impossible opponents number.
	 */
	PrivateProposal<S> initiate(S settings, String commentary, Player initiator, Collection<Player> opponents);


	/**
	 * Returns proposal with specified id or {@code null} if there is no such proposal.
	 *
	 * @param proposalId the proposal id.
	 * @return proposal with specified id or {@code null} if there is no such proposal
	 */
	GameProposal<S> getProposal(long proposalId);


	/**
	 * Accept proposal with specified id by specified {@code player}
	 *
	 * @param proposalId the id of proposal
	 * @param player     the player who wants accept the proposal.
	 * @return the accepted proposal or {@code null} if there is no proposal with specified id.
	 * @throws NullPointerException      if {@code player} is null;
	 * @throws ViolatedCriteriaException if player violates one ore more proposal's restrictions.
	 */
	GameProposal<S> accept(long proposalId, Player player) throws ViolatedCriteriaException;

	/**
	 * The player would like reject accepted proposal or received challenge.
	 * <p/>
	 * If player don't accept the proposal when nothing is happened.
	 * <p/>
	 * The proposal can be cancelled if it's rejected by initiator or it's challenge.
	 *
	 * @param proposalId the proposal
	 * @param player     the player who rejected the proposal.
	 * @return rejected proposal or {@code null} if player doesn't belong to the proposal.
	 * @throws NullPointerException      if {@code player} is null;
	 * @throws ViolatedCriteriaException if player violates one ore more proposal's restrictions.
	 */
	GameProposal<S> reject(long proposalId, Player player) throws ViolatedCriteriaException;

	/**
	 * Terminates specified proposal. This is system function and usually is executed by administrator or
	 * a component.
	 *
	 * @param proposalId the proposal to be cancelled.
	 * @return cancelled proposal or {@code null} if there is no proposal with specified id.
	 */
	GameProposal<S> terminate(long proposalId);


	/**
	 * Checks that the player doesn't violate criteria of the proposal.
	 *
	 * @param proposalId the proposal id to be validated.
	 * @param player     the player to be checked
	 * @return unmodifiable collection of violated criteria or {@code null} if there no such.
	 */
	Collection<CriterionViolation> validate(long proposalId, Player player);
}