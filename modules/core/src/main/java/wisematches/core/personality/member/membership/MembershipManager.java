package wisematches.core.personality.member.membership;

import wisematches.core.Personality;
import wisematches.core.expiration.ExpirationManager;
import wisematches.core.personality.member.Membership;

import java.util.Date;

/**
 * {@code MembershipManager} tracks current membership status and provides ability to
 * update player's membership.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface MembershipManager extends ExpirationManager<Personality, MembershipExpiration> {
	void addMembershipListener(MembershipListener l);

	void removeMembershipListener(MembershipListener l);


	/**
	 * Returns current player's membership.
	 * <p/>
	 * This method checks membership expiration before
	 * return and fires {@link MembershipListener#membershipCardUpdated(Personality, MembershipCard, MembershipCard)}
	 * event if it's expired. The event can be fire in the same or in separate thread.
	 * <p/>
	 * If person is unknown {@link wisematches.core.personality.member.Membership#DEFAULT} will be returned.
	 *
	 * @param personality the peronality who's membership should be returned.
	 * @return the person's membership.
	 * @see MembershipListener#membershipCardUpdated(Personality, MembershipCard, MembershipCard)
	 */
	Membership getMembership(Personality personality);


	/**
	 * Returns original membership card associated with the person.
	 *
	 * @param person the person who's membership card must be returned.
	 * @return the person's membership card or {@code null} if person doesn't have
	 *         any membership at this moment.
	 */
	MembershipCard getPlayerMembership(Personality person);

	/**
	 * Removes membership from specified player.
	 *
	 * @param person the person who's membership should be removed.
	 * @return old membership card associated with the person.
	 */
	MembershipCard removePlayerMembership(Personality person);

	/**
	 * Changes current the person's membership.
	 *
	 * @param person     the person who's membership must be changed.
	 * @param membership the new person's membership.
	 * @param expiration the expiration date.
	 * @return registered membership card.
	 */
	MembershipCard setPlayerMembership(Personality person, Membership membership, Date expiration);
}
