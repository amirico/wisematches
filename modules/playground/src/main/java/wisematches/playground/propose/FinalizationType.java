package wisematches.playground.propose;

/**
 * Contains all possible finalization types.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum FinalizationType {
    /**
     * Indicates that a proposal is ready and game can be started.
     */
    READY,
    /**
     * Indicates that a proposal initiator has cancelled the proposal.
     */
    REPUDIATED,
    /**
     * Indicates that one or more opponents has cancelled the proposal.
     */
    REJECTED,
    /**
     * Indicates that a proposal has been terminated by timeout.
     */
    TERMINATED
}
