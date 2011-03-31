package wisematches.server.player.computer;

import wisematches.server.player.Language;
import wisematches.server.player.Membership;
import wisematches.server.player.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This is base class for all hardcoded players, like robots or guests. Any instance of this
 * class must have unique id that is stored in static map.
 * <p/>
 * Instance of this class can't be created directly and only subclasses are able to do this.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ComputerPlayer implements Player {
	protected final long id;
	protected final String nickname;
	protected final Membership membership;

	private static final Map<Long, ComputerPlayer> computerPlayerMap = new HashMap<Long, ComputerPlayer>();

	/**
	 * Creates new computer player with specified parameters.
	 *
	 * @param id		 the computer player id. The id must be between 1 and 1000. This ids are
	 *                   reserved for computer players.
	 * @param nickname   the player's nickname.
	 * @param membership the player's membership
	 */
	protected ComputerPlayer(long id, String nickname, Membership membership) {
		this.id = id;
		this.nickname = nickname;
		this.membership = membership;

		synchronized (computerPlayerMap) {
			if (computerPlayerMap.containsKey(id)) {
				throw new IllegalArgumentException("Computer player can't be created. Duplicated player id: " + id);
			}
			computerPlayerMap.put(id, this);
		}
	}

	@Override
	public long getId() {
		return id;
	}

	/**
	 * Always returns null
	 *
	 * @return null value.
	 */
	@Override
	public String getEmail() {
		return null;
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
	public String getPassword() {
		return nickname;
	}

	/**
	 * Always returns null
	 *
	 * @return null value.
	 */
	@Override
	public Language getLanguage() {
		return null;
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
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ComputerPlayer that = (ComputerPlayer) o;
		return id == that.id;
	}

	@Override
	public int hashCode() {
		return (int) (id ^ (id >>> 32));
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("ComputerPlayer");
		sb.append("{id=").append(id);
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
