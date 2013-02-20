package wisematches.core.personality;

import wisematches.core.Language;
import wisematches.core.Member;
import wisematches.core.Membership;
import wisematches.core.personality.player.account.Account;

import java.util.TimeZone;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class DefaultMember extends Member {
	private final String email;
	private final String nickname;
	private final TimeZone timeZone;
	private final Language language;
	private final Membership playerType;

	public DefaultMember(Account account, Membership playerType) {
		super(account.getId());
		this.nickname = account.getNickname();
		this.email = account.getEmail();
		this.timeZone = account.getTimeZone();
		this.language = account.getLanguage();
		this.playerType = playerType;
	}

	public DefaultMember(long id, String nickname, String email, TimeZone timeZone, Membership playerType, Language language) {
		super(id);
		this.email = email;
		this.nickname = nickname;
		this.timeZone = timeZone;
		this.language = language;
		this.playerType = playerType;
	}

	@Override
	public String getNickname() {
		return nickname;
	}

	@Override
	public Language getLanguage() {
		return language;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public TimeZone getTimeZone() {
		return timeZone;
	}

	@Override
	public Membership getMembership() {
		return playerType;
	}
}
