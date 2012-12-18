package wisematches.playground.membership;

import wisematches.personality.Membership;
import wisematches.personality.Personality;
import wisematches.playground.GameRelationship;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface MembershipManager {
	void addMembershipListener(MembershipListener l);

	void removeMembershipListener(MembershipListener l);


	MembershipActivation activateMembership(Personality person, Membership membership, int days, GameRelationship relationship);
}
