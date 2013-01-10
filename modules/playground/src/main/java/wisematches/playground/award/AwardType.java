package wisematches.playground.award;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum AwardType {
	MEDAL(false),
	BADGE(true),
	RIBBON(false);

	private final boolean weight;

	private AwardType(boolean weight) {
		this.weight = weight;
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

	public boolean hasWeight() {
		return weight;
	}

	public Collection<AwardDescriptor> filter(Collection<AwardDescriptor> descriptors) {
		final Collection<AwardDescriptor> res = new ArrayList<>();
		for (AwardDescriptor re : descriptors) {
			if (re.getType() == this) {
				res.add(re);
			}
		}
		return res;
	}
}
