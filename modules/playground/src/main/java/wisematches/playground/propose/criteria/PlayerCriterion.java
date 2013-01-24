package wisematches.playground.propose.criteria;

import wisematches.core.Personality;
import wisematches.playground.tracking.Statistics;

import java.io.Serializable;

/**
 * {@code PlayerRestriction}
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface PlayerCriterion extends Serializable {
	CriterionViolation checkViolation(Personality player, Statistics statistics);
}
