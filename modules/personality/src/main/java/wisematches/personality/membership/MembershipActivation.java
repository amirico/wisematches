package wisematches.personality.membership;

import wisematches.personality.Membership;
import wisematches.personality.Relationship;

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

	Relationship getRelationship();
}
