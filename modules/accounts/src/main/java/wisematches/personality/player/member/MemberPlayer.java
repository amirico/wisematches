package wisematches.personality.player.member;

import wisematches.personality.player.StandingPlayer;
import wisematches.personality.player.StandingPlayerManager;
import wisematches.server.personality.account.Account;
import wisematches.server.personality.account.Language;
import wisematches.server.personality.account.Membership;

import java.util.Date;
import java.util.TimeZone;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class MemberPlayer extends StandingPlayer {
	private final Account account;

	public MemberPlayer(Account account, StandingPlayerManager standingPlayerManager) {
		super(account.getId(), standingPlayerManager);
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
	public Date getCreationDate() {
		return account.getCreationDate();
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
