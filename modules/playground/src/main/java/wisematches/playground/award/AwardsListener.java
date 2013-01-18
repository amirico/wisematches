package wisematches.playground.award;

import wisematches.core.Personality;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface AwardsListener {
	void playerAwarded(Personality personality, AwardDescriptor descriptor, Award award);
}
