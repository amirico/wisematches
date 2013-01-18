package wisematches.core.personality;

import wisematches.core.Language;
import wisematches.core.Personality;

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
public abstract class Player extends Personality {
	protected Player(long id) {
		super(id);
	}

	/**
	 * Returns creation date
	 *
	 * @return the creation date
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
	 * Returns type of this player.
	 *
	 * @return the type of this player.
	 */
	public abstract PlayerType getPlayerType();
}