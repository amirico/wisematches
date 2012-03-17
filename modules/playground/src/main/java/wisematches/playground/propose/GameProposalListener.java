package wisematches.playground.propose;

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
    void gameProposalInitiated(GameProposal proposal);

    /**
     * Indicates that a proposal has been updated (usually, new player has joined)
     *
     * @param proposal the updated proposal.
     */
    void gameProposalUpdated(GameProposal proposal);

    /**
     * Indicates that proposal has been finalized because it's ready for play or has been cancelled.
     *
     * @param proposal the finalized proposal.
     * @param reason   the type of finalization.
     */
    void gameProposalFinalized(GameProposal proposal, ProposalResolution reason);
}