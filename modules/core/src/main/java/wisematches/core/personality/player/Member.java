package wisematches.core.personality.player;

import wisematches.core.Language;
import wisematches.core.Player;
import wisematches.core.PlayerType;
import wisematches.core.personality.player.account.Account;
import wisematches.core.personality.player.membership.MembershipCard;

import java.util.TimeZone;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class Member extends Player {
	private Account account;
	private MembershipCard membershipCard;

	public Member(Account account) {
		this(account, null);
	}

	public Member(Account account, MembershipCard membershipCard) {
		super(account.getId());
		this.account = account;
		this.membershipCard = membershipCard;
	}

	@Override
	public String getNickname() {
		return account.getNickname();
	}

	@Override
	public Language getLanguage() {
		return account.getLanguage();
	}

	@Override
	public PlayerType getPlayerType() {
		return PlayerType.MEMBER;
	}

	public Membership getMembership() {
		return membershipCard == null ? Membership.BASIC : membershipCard.getValidMembership();
	}

	public String getEmail() {
		return account.getEmail();
	}

	public TimeZone getTimeZone() {
		return account.getTimeZone();
	}

	void setAccount(Account account) {
		this.account = account;
	}

	void setMembershipCard(MembershipCard membershipCard) {
		this.membershipCard = membershipCard;
	}
}
