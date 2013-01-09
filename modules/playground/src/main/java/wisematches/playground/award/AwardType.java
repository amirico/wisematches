package wisematches.playground.award;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum AwardType {
	MEDAL(false),
	BADGE(true),
	RIBBON(true);

	private final boolean weight;

	private AwardType(boolean weight) {
		this.weight = weight;
	}

	public boolean hasWeight() {
		return weight;
	}

	public boolean isMedal() {
		return this == MEDAL;
	}

	public boolean isBadge() {
		return this == BADGE;
	}

	public boolean isRibbon() {
		return this == RIBBON;
	}

	public Collection<Award> filter(Collection<Award> awards) {
		Collection<Award> res = new ArrayList<>(awards.size());
		for (Award award : awards) {
			if (this == award.getType()) {
				res.add(award);
			}
		}
		return res;
	}
}
