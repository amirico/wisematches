package wisematches.playground.membership;

import wisematches.personality.Membership;
import wisematches.playground.GameRelationship;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface MembershipActivation {
	long getPlayer();

	int getTotalDays();

	int getSpentDays();

	Date getRegistered();

	Membership getMembership();

	GameRelationship getRelationship();
}
