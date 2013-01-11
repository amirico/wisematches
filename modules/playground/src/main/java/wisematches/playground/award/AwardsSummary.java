package wisematches.playground.award;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface AwardsSummary {
	/**
	 * Returns collection of all award types in this summary.
	 *
	 * @return the collection of all award types in this summary.
	 */
	Collection<AwardType> getAwardTypes();

	/**
	 * Returns collection of all awards
	 *
	 * @param type the awards type to be returned
	 * @return collection of awards by specified type. If null all awards will be returned.
	 */
	Collection<AwardDescriptor> getAwards(AwardType type);


	boolean hasAwards(String code);

	AwardWeight getHighestWeight(String code);

	Collection<AwardWeight> getAwardWeights(String code);


	int getAwardsCount(String code);

	int getAwardsCount(String code, AwardWeight weight);
}
