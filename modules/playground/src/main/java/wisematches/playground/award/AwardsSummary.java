package wisematches.playground.award;

import java.util.Collection;
import java.util.Set;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface AwardsSummary {
	Set<String> getAwardNames();

	// ribbon
	boolean hasAwards(String code);

	// badge
	AwardWeight getHighestWeight(String code);

	// medal
	Collection<AwardWeight> getAwardWeights(String code);


	int getAwardsCount(String code);

	int getAwardsCount(String code, AwardWeight weight);
}
