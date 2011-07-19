package wisematches.personality.account;

import wisematches.personality.Language;
import wisematches.personality.Membership;
import wisematches.personality.Personality;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.TimeZone;

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
@MappedSuperclass
public abstract class Account extends Personality implements Serializable {
	@Deprecated
	protected Account() {
	}

	protected Account(long id) {
		super(id);
	}

	/**
	 * Returns a email of the player.
	 * <p/>
	 * This value can't be null.
	 *
	 * @return not null email of the player.
	 */
	public abstract String getEmail();

	/**
	 * Returns a username of the player.
	 * <p/>
	 * This value can't be null.
	 *
	 * @return not null username of the player.
	 */
	public abstract String getNickname();

	/**
	 * Returns player's password.
	 *
	 * @return the player's password.
	 */
	public abstract String getPassword();

	/**
	 * Returns default language of the player.
	 * <p/>
	 * This value can't be null.
	 *
	 * @return not null default language of the player.
	 */
	public abstract Language getLanguage();

	/**
	 * Returns player's timezone.
	 *
	 * @return the player's timezone.
	 */
	public abstract TimeZone getTimeZone();

	/**
	 * Returns this membership for the player.
	 * <p/>
	 * For system users this method returns {@code null}. It means that such user can't be modified,
	 * his rating is not updated and so on.
	 *
	 * @return the membership for the player or {@code null} if this is a system user
	 *         and has some limitations.
	 */
	public abstract Membership getMembership();
}