package wisematches.core.personality.member.membership;

import wisematches.core.Personality;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface MembershipListener {
	/**
	 * Indicates that person's membership has been changed.
	 *
	 * @param person  the person who's membership has been changed.
	 * @param oldCard the old player's membership.
	 * @param newCard the new player's membership.
	 */
	void membershipCardUpdated(Personality person, MembershipCard oldCard, MembershipCard newCard);
}
