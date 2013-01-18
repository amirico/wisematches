package wisematches.playground.award.impl;

import wisematches.core.Personality;
import wisematches.playground.GameRelationship;
import wisematches.playground.award.AwardWeight;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface AwardMachinery {
	void grantAward(Personality person, String code, AwardWeight weight, GameRelationship relationship);
}
