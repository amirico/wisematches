package wisematches.playground.propose.criteria;

import wisematches.core.personality.Player;
import wisematches.playground.tracking.Statistics;

import java.io.Serializable;

/**
 * {@code PlayerRestriction}
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface PlayerCriterion extends Serializable {
	CriterionViolation checkViolation(Player player, Statistics statistics);
}
