package wisematches.personality.player.member;

import wisematches.personality.Language;
import wisematches.personality.Membership;
import wisematches.personality.account.Account;
import wisematches.personality.player.Player;

import java.util.TimeZone;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class MemberPlayer extends Player {
	private final Account account;

	public MemberPlayer(Account account) {
		super(account.getId());
		this.account = account;
	}

	@Override
	public String getNickname() {
		return account.getNickname();
	}

	public Account getAccount() {
		return account;
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
	public Membership getMembership() {
		return account.getMembership();
	}

	@Override
	public TimeZone getTimeZone() {
		return account.getTimeZone();
	}
}
