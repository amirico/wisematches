package wisematches.core.personality.member;

import wisematches.core.Language;
import wisematches.core.personality.Player;
import wisematches.core.personality.PlayerType;
import wisematches.core.personality.member.account.Account;

import java.util.TimeZone;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class MemberPlayer extends Player {
	private final Account account;
	private final Membership membership;

	public MemberPlayer(Account account) {
		this(account, Membership.DEFAULT);
	}

	public MemberPlayer(Account account, Membership membership) {
		super(account.getId());
		if (membership == null) {
			throw new NullPointerException("Membership can't be null");
		}
		this.account = account;
		this.membership = membership;
	}

	@Override
	public String getNickname() {
		return account.getNickname();
	}

	@Override
	public String getEmail() {
		return account.getEmail();
	}

	@Override
	public Language getLanguage() {
		return account.getLanguage();
	}

	@Override
	public PlayerType getPlayerType() {
		return PlayerType.MEMBER;
	}

	public Account getAccount() {
		return account;
	}

	public Membership getMembership() {
		return membership;
	}

	@Override
	public TimeZone getTimeZone() {
		return account.getTimeZone();
	}
}
