package wisematches.server.core;

import wisematches.kernel.notification.PlayerNotifications;
import wisematches.kernel.player.Player;
import wisematches.kernel.player.PlayerProfile;
import wisematches.server.player.Language;

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
	public PlayerProfile getPlayerProfile() {
		return null;
	}

	@Override
	public PlayerNotifications getPlayerNotifications() {
		return null;
	}

	@Override
	public int getRating() {
		return rating;
	}

	@Override
	public void setRating(int rating) {
		this.rating = rating;
	}

	@Override
	public void changeRating(int delta) {
		rating += delta;
	}

	@Override
	public String toString() {
		return "MockPlayer{" +
				"id=" + id +
				", rating=" + rating +
				'}';
	}
}
