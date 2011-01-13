package wisematches.server.core;

import wisematches.server.player.Language;
import wisematches.server.player.Membership;
import wisematches.server.player.Player;

import java.util.Date;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class MockPlayer implements Player {
	private final long id;
	private int rating;

	public MockPlayer(long id) {
		this.id = id;
	}

	public MockPlayer(long id, int rating) {
		this.id = id;
		this.rating = rating;
	}

	public long getId() {
		return id;
	}

	public String getUsername() {
		return "MockPlayer" + id;
	}

	public String getPassword() {
		return null;
	}

	public String getEmail() {
		return null;
	}

	public void setPassword(String password) {
	}

	public void setEmail(String email) {
	}

	public Language getLanguage() {
		return null;
	}

	@Override
	public Membership getMembership() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	public void setLanguage(Language locale) {
	}

	public Date getCreationDate() {
		return null;
	}

	public Date getLastSigninDate() {
		return null;
	}

	public void setLastSigninDate(Date date) {
	}


	@Override
	public int getRating() {
		return rating;
	}


	@Override
	public String toString() {
		return "MockPlayer{" +
				"id=" + id +
				", rating=" + rating +
				'}';
	}
}
