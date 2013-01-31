package wisematches.server.services.award;

import java.util.Collection;

/**
 * {@code AwardsSummary} provides summary information about all player's awards.
 *
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


	/**
	 * Indicates that summary has awards with specified code.
	 *
	 * @param code the award's code.
	 * @return {@code true} if summary contains awards with specified code; {@code false} - otherwise.
	 */
	boolean hasAwards(String code);

	/**
	 * Returns highest award for specified code.
	 *
	 * @param code the award's code
	 * @return the highest award or {@code null} if there are no any awards with specified code.
	 */
	AwardWeight getHighestWeight(String code);

	/**
	 * Returns collection of all weights for specified code.
	 *
	 * @param code the award's code
	 * @return collection of all weights for specified code.
	 */
	Collection<AwardWeight> getAwardWeights(String code);


	/**
	 * Returns total numbers of award by selected type.
	 *
	 * @param code the award's code
	 * @return total numbers of award by selected type.
	 */
	int getAwardsCount(String code);

	/**
	 * Returns awards count by specified code and specified weight.
	 *
	 * @param code   the award's code
	 * @param weight the award's weight
	 * @return awards count by specified code and specified weight.
	 */
	int getAwardsCount(String code, AwardWeight weight);
}
