package wisematches.playground.award.impl;

import wisematches.playground.award.AwardDescriptor;
import wisematches.playground.award.AwardType;
import wisematches.playground.award.AwardWeight;
import wisematches.playground.award.AwardsSummary;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultAwardsSummary implements AwardsSummary {
	private final Map<String, Map<AwardWeight, Integer>> awardsMap = new HashMap<>();
	private final Map<AwardType, Collection<AwardDescriptor>> descriptorMap = new HashMap<>();
	private static final Comparator<AwardWeight> COMPARATOR = new Comparator<AwardWeight>() {
		@Override
		public int compare(AwardWeight o1, AwardWeight o2) {
			return o2.ordinal() - o1.ordinal();
		}
	};

	DefaultAwardsSummary(List<Object[]> list, Map<String, AwardDescriptor> descriptorMap) {
		for (Object[] objects : list) {
			final AwardDescriptor desc = descriptorMap.get((String) objects[0]);
			final AwardWeight weight = (AwardWeight) objects[1];
			final Integer count = ((Number) objects[2]).intValue();

			final AwardType type = desc.getType();
			Collection<AwardDescriptor> awardDescriptors = this.descriptorMap.get(type);
			if (awardDescriptors == null) {
				awardDescriptors = new ArrayList<>();
				this.descriptorMap.put(type, awardDescriptors);
			}

			if (!awardDescriptors.contains(desc)) {
				awardDescriptors.add(desc);
			}

			final String code = desc.getCode();
			Map<AwardWeight, Integer> awardWeightIntegerMap = awardsMap.get(code);
			if (awardWeightIntegerMap == null) {
				awardWeightIntegerMap = new TreeMap<>(COMPARATOR);
				awardsMap.put(code, awardWeightIntegerMap);
			}
			awardWeightIntegerMap.put(weight, count);
		}
	}

	@Override
	public Collection<AwardType> getAwardTypes() {
		return descriptorMap.keySet();
	}

	@Override
	public Collection<AwardDescriptor> getAwards(AwardType type) {
		return descriptorMap.get(type);
	}

	@Override
	public boolean hasAwards(String code) {
		return awardsMap.containsKey(code);
	}

	@Override
	public int getAwardsCount(String code) {
		final Map<AwardWeight, Integer> awardWeightIntegerMap = awardsMap.get(code);
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
		final Map<AwardWeight, Integer> awardWeightIntegerMap = awardsMap.get(code);
		if (awardWeightIntegerMap == null) {
			return 0;
		}
		final Integer integer = awardWeightIntegerMap.get(weight);
		return integer == null ? 0 : integer;
	}

	@Override
	public AwardWeight getHighestWeight(String code) {
		final Map<AwardWeight, Integer> awardWeightIntegerMap = awardsMap.get(code);
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
		return awardsMap.get(code).keySet();
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("DefaultAwardsSummary");
		sb.append("{awardsMap=").append(awardsMap);
		sb.append('}');
		return sb.toString();
	}
}