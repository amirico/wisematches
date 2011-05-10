package wisematches.server.personality.player;

import wisematches.server.personality.Personality;
import wisematches.server.personality.account.Language;
import wisematches.server.personality.account.Membership;
import wisematches.server.standing.profile.PlayerProfile;
import wisematches.server.standing.rating.RatingCurve;
import wisematches.server.standing.statistic.PlayerStatistic;

import java.util.Date;
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
	 * Returns this membership for the player.
	 * <p/>
	 * For system users this method returns {@code null}. It means that such user can't be modified,
	 * his rating is not updated and so on.
	 *
	 * @return the membership for the player or {@code null} if this is a system user
	 *         and has some limitations.
	 */
	public abstract Membership getMembership();


	/**
	 * Returns current player's rating.
	 * <p/>
	 * For computer players always returns predefined constant.
	 *
	 * @return the current player's rating.
	 */
	public abstract short getRating();

	/**
	 * Returns current player's position.
	 * <p/>
	 * For computer players always returns zero.
	 *
	 * @return the current player's position.
	 */
	public abstract long getPosition();

	/**
	 * Returns player's profile.
	 * <p/>
	 * For computer players always returns null.
	 *
	 * @return he player's profile or {@code null} if player is {@code ComputerPlayer}.
	 */
	public abstract PlayerProfile getPlayerProfile();

	/**
	 * Returns player's statistic.
	 * <p/>
	 * For computer players always returns null.
	 *
	 * @return the player's statistic or {@code null} if player is {@code ComputerPlayer}.
	 */
	public abstract PlayerStatistic getPlayerStatistic();

	/**
	 * Returns rating curve container that contains information about all rating changes for specified player
	 * for specified dates with specified resolution.
	 * <p/>
	 * For computer players always returns null.
	 *
	 * @param resolution the curve resolution. Indicates how many days must be grouped for one point. It's not possible
	 *                   to get curve with resolution less that one day at this moment.
	 * @param startDate  start date. If null all range will be used.
	 * @param endDate	end date. If null today will be used
	 * @return the rating curve.
	 * @throws IllegalArgumentException if resolution if zero or negative.
	 * @throws NullPointerException	 if {@code player} is null
	 * @see wisematches.server.standing.rating.PlayerRatingManager#getRatingCurve(wisematches.server.personality.Personality, int, java.util.Date, java.util.Date)
	 */
	public abstract RatingCurve getRatingCurve(int resolution, Date startDate, Date endDate);
}