package wisematches.playground.propose;

import wisematches.playground.GameRestriction;
import wisematches.playground.GameSettings;

/**
 * Waiting game proposal. Each waiting game proposal has restrictions that indicates which
 * players can be joined.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GameWaiting<S extends GameSettings> extends GameProposal<S> {
    /**
     * Returns unmodifiable collection of all restrictions.
     *
     * @return the unmodifiable collection of all restrictions.
     */
    GameRestriction getRestriction();
}
