package wisematches.playground.propose;

import wisematches.core.Player;
import wisematches.playground.GameSettings;
import wisematches.playground.tracking.Statistics;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface PublicProposal<S extends GameSettings> extends GameProposal<S> {
	/**
	 * Returns collection of all criterion for this waiting proposal.
	 *
	 * @return the collection of all criterion for this waiting proposal.
	 */
	Collection<PlayerCriterion> getPlayerCriterion();

	/**
	 * Validates that specified player with specified statistics can join to this proposals.
	 *
	 * @param player     the player to be checked
	 * @param statistics the statistic for validation.
	 * @return collection of broken criterion if there are any or empty or null collection if no one.
	 */
	Collection<CriterionViolation> checkViolations(Player player, Statistics statistics);
}
