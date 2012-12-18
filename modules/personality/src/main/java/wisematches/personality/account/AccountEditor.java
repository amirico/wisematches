package wisematches.personality.account;

import wisematches.personality.Language;
import wisematches.personality.Membership;

import java.util.TimeZone;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class AccountEditor {
	private long id = 0;
	private String email;
	private String nickname;
	private String password;
	private TimeZone timeZone = TimeZone.getDefault();
	private Language language = Language.DEFAULT;
	private Membership membership = Membership.BASIC;

	public AccountEditor() {
	}

	public AccountEditor(Account account) {
		this.id = account.getId();
		this.nickname = account.getNickname();
		this.password = account.getPassword();
		this.email = account.getEmail();
		this.language = account.getLanguage();
		this.membership = account.getMembership();
		this.timeZone = account.getTimeZone();
	}

	public AccountEditor(String email, String nickname, String password) {
		this.email = email;
		this.nickname = nickname;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public AccountEditor setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getNickname() {
		return nickname;
	}

	public AccountEditor setNickname(String nickname) {
		this.nickname = nickname;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public AccountEditor setPassword(String password) {
		this.password = password;
		return this;
	}

	public Language getLanguage() {
		return language;
	}

	public AccountEditor setLanguage(Language language) {
		this.language = language;
		return this;
	}

	public TimeZone getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	public Account createAccount() {
		if (email == null) {
			throw new IllegalArgumentException("email is not specified");
		}
		if (nickname == null) {
			throw new IllegalArgumentException("nickname is not specified");
		}
		return new AccountDetails(id, email, nickname, password, language, membership, timeZone);
	}

	/**
	 * Base implementation of {@code Player} interface.
	 *
	 * @author Sergey Klimenko (smklimenko@gmail.com)
	 */
	private static class AccountDetails extends Account {
		private final String email;
		private final String nickname;
		private final String password;
		private final TimeZone timeZone;
		private final Language language;
		private final Membership membership;

		private AccountDetails(long id, String email, String nickname, String password, Language language, Membership membership, TimeZone timeZone) {
			super(id);
			this.email = email;
			this.nickname = nickname;
			this.password = password;
			this.language = language;
			this.membership = membership;
			this.timeZone = timeZone;
		}

		@Override
		public String getEmail() {
			return email;
		}

		@Override
		public String getNickname() {
			return nickname;
		}

		@Override
		public String getPassword() {
			return password;
		}

		@Override
		public Language getLanguage() {
			return language;
		}

		@Override
		public Membership getMembership() {
			return membership;
		}

		@Override
		public TimeZone getTimeZone() {
			return timeZone;
		}
	}
}
