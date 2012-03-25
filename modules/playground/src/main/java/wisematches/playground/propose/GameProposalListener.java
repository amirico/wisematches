package wisematches.playground.propose;

import wisematches.personality.Personality;
import wisematches.playground.GameSettings;

/**
 * Listener interface for {@code wisematches.server.playground.propose.GameProposalManager}.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GameProposalListener {
    /**
     * Indicates that new proposal has been initiated.
     *
     * @param proposal the initiated proposal.
     */
    void gameProposalInitiated(GameProposal<? extends GameSettings> proposal);

    /**
     * Indicates that a proposal has been updated (usually, new player has joined)
     *
     * @param proposal  the updated proposal.
     * @param player    the player who changed the proposal
     * @param directive type of change
     */
    void gameProposalUpdated(GameProposal<? extends GameSettings> proposal, Personality player, ProposalDirective directive);

    /**
     * Indicates that proposal has been finalized because it's ready for play or has been cancelled.
     *
     * @param proposal the finalized proposal.
     * @param player   the player who finalized the proposal.In case of ready or terminated proposal contains {@code null}.
     * @param reason   the type of finalization.
     */
    void gameProposalFinalized(GameProposal<? extends GameSettings> proposal, Personality player, ProposalResolution reason);
}