package wisematches.server.personality.player.computer;

import wisematches.server.personality.account.Language;
import wisematches.server.personality.account.Membership;
import wisematches.server.personality.player.Player;
import wisematches.server.standing.profile.PlayerProfile;
import wisematches.server.standing.rating.RatingBatch;
import wisematches.server.standing.rating.RatingBatching;
import wisematches.server.standing.rating.RatingChange;
import wisematches.server.standing.statistic.PlayerStatistic;

import java.util.*;

/**
 * This is base class for all hardcoded players, like robots or guests. Any instance of this
 * class must have unique id that is stored in static map.
 * <p/>
 * Instance of this class can't be created directly and only subclasses are able to do this.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ComputerPlayer extends Player {
	protected final String nickname;
	protected final Membership membership;
	protected final short rating;

	private static final Map<Long, ComputerPlayer> computerPlayerMap = new HashMap<Long, ComputerPlayer>();

	/**
	 * Creates new computer player with specified parameters.
	 *
	 * @param id		 the computer player id. The id must be between 1 and 1000. This ids are
	 *                   reserved for computer players.
	 * @param nickname   the player's nickname.
	 * @param membership the player's membership
	 * @param rating	 the player's rating
	 */
	protected ComputerPlayer(long id, String nickname, Membership membership, short rating) {
		super(id);
		this.nickname = nickname;
		this.membership = membership;
		this.rating = rating;

		synchronized (computerPlayerMap) {
			if (computerPlayerMap.containsKey(id)) {
				throw new IllegalArgumentException("Computer player can't be created. Duplicated player id: " + id);
			}
			computerPlayerMap.put(id, this);
		}
	}

	@Override
	public String getNickname() {
		return nickname;
	}

	/**
	 * Always returns null
	 *
	 * @return null value.
	 */
	@Override
	public Language getLanguage() {
		return Language.EN;
	}

	/**
	 * Always returns {@code TimeZone.getDefault()}
	 *
	 * @return returns default timezone.
	 */
	@Override
	public TimeZone getTimeZone() {
		return TimeZone.getDefault();
	}

	/**
	 * Always returns null
	 *
	 * @return null value.
	 */
	@Override
	public Membership getMembership() {
		return membership;
	}

	@Override
	public short getRating() {
		return rating;
	}

	@Override
	public long getPosition() {
		return 0;
	}

	@Override
	public PlayerProfile getPlayerProfile() {
		return null;
	}

	@Override
	public PlayerStatistic getPlayerStatistic() {
		return null;
	}

	@Override
	public Collection<RatingBatch> getRatingChanges(RatingBatching batching) {
		return null;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("ComputerPlayer");
		sb.append("{id=").append(getId());
		sb.append(", nickname='").append(nickname).append('\'');
		sb.append('}');
		return sb.toString();
	}

	/**
	 * Returns computer player by specified id.
	 * <p/>
	 * This method can throw {@code ClassCastException} if the computer type can't be
	 * cast to expected type. To safe get computer player please use another
	 * method {@link #getComputerPlayer(long, Class)}.
	 *
	 * @param id  the player id
	 * @param <T> the expected type of computed player
	 * @return the computed player or {@code null} if there is no computer player with such id.
	 * @throws ClassCastException if computer player with required id can't be cast to expected type.
	 * @see #getComputerPlayer(long, Class)
	 */
	@SuppressWarnings("unchecked")
	public static <T extends ComputerPlayer> T getComputerPlayer(long id) {
		synchronized (computerPlayerMap) {
			return (T) computerPlayerMap.get(id);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends ComputerPlayer> T getComputerPlayer(long id, Class<T> type) {
		synchronized (computerPlayerMap) {
			final ComputerPlayer player = computerPlayerMap.get(id);
			if (player != null && type.isAssignableFrom(player.getClass())) {
				return (T) player;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T extends ComputerPlayer> Collection<T> getComputerPlayers(Class<T> playerType) {
		final Collection<T> res = new ArrayList<T>();
		synchronized (computerPlayerMap) {
			for (ComputerPlayer computerPlayer : computerPlayerMap.values()) {
				if (playerType.isAssignableFrom(computerPlayer.getClass())) {
					res.add((T) computerPlayer);
				}
			}
		}
		return res;
	}
}
