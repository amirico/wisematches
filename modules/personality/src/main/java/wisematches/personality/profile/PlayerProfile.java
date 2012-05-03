package wisematches.personality.profile;

import wisematches.personality.Language;

import java.util.Date;

/**
 * Player profile interface with a set of {@code get} methods. Implementation of this interface
 * must be immutable and profile can be updated only via {@code PlayerProfileManager} object.
 * <p/>
 * The profile can be changes using {@code PlayerProfileEditor} object.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 * @see PlayerProfileEditor
 */
public interface PlayerProfile {
	long getPlayerId();

	/**
	 * Returns date when player was created
	 *
	 * @return date when player was created
	 */
	Date getCreationDate();

	String getRealName();

	String getCountryCode();

	Date getBirthday();

	Gender getGender();

	Language getPrimaryLanguage();

	String getComments();
}
