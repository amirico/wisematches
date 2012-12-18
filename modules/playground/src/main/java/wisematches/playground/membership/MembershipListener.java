package wisematches.playground.membership;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface MembershipListener {
	void membershipActivated(MembershipActivation record);

	void membershipReactivated(MembershipActivation record);

	void membershipDeactivated(MembershipActivation record);
}
