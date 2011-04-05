package wisematches.server.personality.player;

import wisematches.server.personality.Personality;
import wisematches.server.personality.account.Language;
import wisematches.server.personality.account.Membership;
import wisematches.server.standing.rating.RatingChange;
import wisematches.server.standing.statistic.PlayerStatistic;

import java.util.Collection;

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
	 * Returns player statistic.
	 * <p/>
	 * For computer players always returns null.
	 *
	 * @return the player's statistic or {@code null} if player is {@code ComputerPlayer}.
	 */
	public abstract PlayerStatistic getPlayerStatistic();

	/**
	 * Returns collection of rating changes.
	 * <p/>
	 * For computer players always returns null.
	 *
	 * @return the player's rating changes or {@code null} if player is {@code ComputerPlayer}.
	 */
	public abstract Collection<RatingChange> getRatingChanges();
}