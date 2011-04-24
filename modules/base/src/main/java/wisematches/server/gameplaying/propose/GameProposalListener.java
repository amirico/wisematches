package wisematches.server.gameplaying.propose;

/**
 * Listener interface for {@code wisematches.server.gameplaying.propose.GameProposalManager}.
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
     * Indicates that proposal has been closed because it's ready for play.
     *
     * @param proposal the closed proposal.
     */
    void gameProposalClosed(GameProposal proposal);
}