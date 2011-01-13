package wisematches.server.player;

/**
 * Base implementation of {@code Player} interface.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PlayerDetails implements Player {
	private final long id;
	private final String email;
	private final String username;
	private final String password;
	private final Language language;
	private final Membership membership;
	private final int rating = 1200; // Default rating is 1200

	public PlayerDetails(String email, String username, String password, Language language, Membership membership) {
		this.id = 0;
		this.email = email;
		this.username = username;
		this.password = password;
		this.language = language;
		this.membership = membership;
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
