package wisematches.server.player;

import java.io.Serializable;

/**
 * The {@code Player} interface represents simple user. This interface is read-only
 * and contains only methods that returns common and necessary information about the player,
 * like id, username, nickname, email and language.
 * <p/>
 * Please note that this interface declares only information that is required every time when
 * player info should be shown. Any other player information, like gender, city and so on is defined in
 * {@code PlayerProfile} object.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface Player extends Serializable {
	/**
	 * Returns unique id of the player.
	 * <p/>
	 * This value can't be zero.
	 *
	 * @return unique, non zero id of the player.
	 */
	long getId();

	/**
	 * Returns a email of the player.
	 * <p/>
	 * This value can't be null.
	 *
	 * @return not null email of the player.
	 */
	String getEmail();

	/**
	 * Returns a username of the player.
	 * <p/>
	 * This value can't be null.
	 *
	 * @return not null username of the player.
	 */
	String getNickname();

	/**
	 * Returns player's password.
	 *
	 * @return the player's password.
	 */
	String getPassword();

	/**
	 * Returns default language of the player.
	 * <p/>
	 * This value can't be null.
	 *
	 * @return not null default language of the player.
	 */
	Language getLanguage();

	/**
	 * Returns this membership for the player.
	 * <p/>
	 * For system users this method returns {@code null}. It means that such user can't be modified,
	 * his rating is not updated and so on.
	 *
	 * @return the membership for the player or {@code null} if this is a system user
	 *         and has some limitations.
	 */
	Membership getMembership();

	/**
	 * Returns current rating of the player.
	 *
	 * @return the current rating of the player.
	 */
	int getRating();
}