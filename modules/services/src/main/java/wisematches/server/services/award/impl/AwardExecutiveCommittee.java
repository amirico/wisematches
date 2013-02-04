package wisematches.server.services.award.impl;

import wisematches.core.Player;
import wisematches.playground.GameRelationship;
import wisematches.server.services.award.AwardWeight;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface AwardExecutiveCommittee {
	void grantAward(Player player, String code, AwardWeight weight, GameRelationship relationship);
}
