package wisematches.personality.membership;

import wisematches.personality.Membership;
import wisematches.personality.Personality;
import wisematches.personality.Relationship;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface MembershipManager {
	void addMembershipListener(MembershipListener l);

	void removeMembershipListener(MembershipListener l);


	MembershipActivation activateMembership(Personality person, Membership membership, int days, Relationship relationship);
}
