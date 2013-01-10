package wisematches.playground.award.impl;

import wisematches.playground.award.AwardWeight;
import wisematches.playground.award.AwardsSummary;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultAwardsSummary implements AwardsSummary {
	private final Map<String, Map<AwardWeight, Integer>> map = new HashMap<>();

	DefaultAwardsSummary(List<Object[]> list) {
		for (Object[] objects : list) {
			final String code = (String) objects[0];
			final AwardWeight weight = (AwardWeight) objects[1];
			final Integer count = ((Number) objects[2]).intValue();

			Map<AwardWeight, Integer> awardWeightIntegerMap = map.get(code);
			if (awardWeightIntegerMap == null) {
				awardWeightIntegerMap = new HashMap<>();
				map.put(code, awardWeightIntegerMap);
			}
			awardWeightIntegerMap.put(weight, count);
		}
	}

	@Override
	public Set<String> getAwardNames() {
		return map.keySet();
	}

	@Override
	public boolean hasAwards(String code) {
		return map.containsKey(code);
	}

	@Override
	public int getAwardsCount(String code) {
		final Map<AwardWeight, Integer> awardWeightIntegerMap = map.get(code);
		if (awardWeightIntegerMap == null) {
			return 0;
		}
		int res = 0;
		for (Integer integer : awardWeightIntegerMap.values()) {
			res += integer;
		}
		return res;
	}


	@Override
	public int getAwardsCount(String code, AwardWeight weight) {
		final Map<AwardWeight, Integer> awardWeightIntegerMap = map.get(code);
		if (awardWeightIntegerMap == null) {
			return 0;
		}
		final Integer integer = awardWeightIntegerMap.get(weight);
		return integer == null ? 0 : integer;
	}

	@Override
	public AwardWeight getHighestWeight(String code) {
		final Map<AwardWeight, Integer> awardWeightIntegerMap = map.get(code);
		if (awardWeightIntegerMap == null) {
			return null;
		}

		AwardWeight res = null;
		for (AwardWeight weight : awardWeightIntegerMap.keySet()) {
			if (res == null || res.ordinal() < weight.ordinal()) {
				res = weight;
			}
		}
		return res;
	}

	@Override
	public Collection<AwardWeight> getAwardWeights(String code) {
		return map.get(code).keySet();
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("DefaultAwardsSummary");
		sb.append("{map=").append(map);
		sb.append('}');
		return sb.toString();
	}
}