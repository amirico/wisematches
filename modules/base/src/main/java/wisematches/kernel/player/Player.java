package wisematches.kernel.player;

import wisematches.kernel.notification.PlayerNotifications;
import wisematches.server.player.Language;

import java.util.Date;

/**
 * {@code Player} is base interface for player. Each player has set of unmodifiable and modifiable
 * attributes like username, password, locale and so on.
 * <p/>
 * Each player also has a profile with additional attributes.
 * <p/>
 * Each player also has set of notifications which can be sent to player.
 *
 * @author Sergey Klimenko
 * @version 1.0
 */
public interface Player {
	/**
	 * Returns unique unmodifiable id of this player.
	 *
	 * @return the player id.
	 */
	long getId();

	/**
	 * Return unique unmodifiable username of player. This is nick name.
	 *
	 * @return unique unmodifiable username.
	 */
	String getUsername();

	String getPassword();

	String getEmail();

	void setPassword(String password);

	void setEmail(String email);

	Language getLanguage();

	void setLanguage(Language locale);

	Date getCreationDate();

	int getRating();

	void setRating(int rating);

	void changeRating(int delta);


	Date getLastSigninDate();

	void setLastSigninDate(Date date);

	/**
	 * Returns player profile.
	 *
	 * @return the profile of this player.
	 */
	PlayerProfile getPlayerProfile();

	/**
	 * Returns interface that allows get information about enabled/disable notification and change
	 * set of such notifications.
	 *
	 * @return the interface for interaction with plyaer notifications info.
	 */
	PlayerNotifications getPlayerNotifications();
}