package wisematches.playground.propose;

import wisematches.core.Player;
import wisematches.playground.tracking.Statistics;

import java.io.Serializable;

/**
 * {@code PlayerRestriction}
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface Criterion extends Serializable {
	String getCode();

	Comparable getExpected();


	CriterionViolation checkViolation(Player player, Statistics statistics);
}
