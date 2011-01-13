package wisematches.server.player;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PlayerEditor {
	private long id = 0;
	private String email;
	private String username;
	private String password;
	private Language language = Language.DEFAULT;
	private Membership membership = Membership.GUEST;
	private int rating = 1200;

	public PlayerEditor() {
	}

	public PlayerEditor(Player player) {
		this.id = player.getId();
		this.username = player.getUsername();
		this.password = player.getPassword();
		this.email = player.getEmail();
		this.language = player.getLanguage();
		this.membership = player.getMembership();
		this.rating = player.getRating();
	}

	public PlayerEditor(String email, String username, String password) {
		this.email = email;
		this.username = username;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public Player createPlayer() {
		if (email == null) {
			throw new IllegalArgumentException("email is not specified");
		}
		if (username == null) {
			throw new IllegalArgumentException("username is not specified");
		}
		return new PlayerDetails(id, email, username, password, language, membership, rating);
	}

	/**
	 * Base implementation of {@code Player} interface.
	 *
	 * @author Sergey Klimenko (smklimenko@gmail.com)
	 */
	private static class PlayerDetails implements Player {
		private final long id;
		private final String email;
		private final String username;
		private final String password;
		private final Language language;
		private final Membership membership;
		private final int rating;

		private PlayerDetails(long id, String email, String username, String password, Language language, Membership membership, int rating) {
			this.id = id;
			this.email = email;
			this.username = username;
			this.password = password;
			this.language = language;
			this.membership = membership;
			this.rating = rating;
		}

		@Override
		public long getId() {
			return id;
		}

		@Override
		public String getEmail() {
			return email;
		}

		@Override
		public String getUsername() {
			return username;
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
		public int getRating() {
			return rating;
		}
	}
}
