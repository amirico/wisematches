package wisematches.server.personality.account;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class AccountEditor {
	private long id = 0;
	private String email;
	private String nickname;
	private String password;
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
	}

	public AccountEditor(String email, String nickname, String password) {
		this.email = email;
		this.nickname = nickname;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public Membership getMembership() {
		return membership;
	}

	public void setMembership(Membership membership) {
		this.membership = membership;
	}

	public Account createAccount() {
		if (email == null) {
			throw new IllegalArgumentException("email is not specified");
		}
		if (nickname == null) {
			throw new IllegalArgumentException("nickname is not specified");
		}
		return new AccountDetails(id, email, nickname, password, language, membership);
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
		private final Language language;
		private final Membership membership;

		private AccountDetails(long id, String email, String nickname, String password, Language language, Membership membership) {
			super(id);
			this.email = email;
			this.nickname = nickname;
			this.password = password;
			this.language = language;
			this.membership = membership;
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
	}
}
