package wisematches.core.personality.proprietary;

import wisematches.core.Language;
import wisematches.core.personality.Player;
import wisematches.core.personality.PlayerType;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * This is base class for all hardcoded players, like robots or guests. Any instance of this
 * class must have unique id that is stored in static map.
 * <p/>
 * Instance of this class can't be created directly and only subclasses are able to do this.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ProprietaryPlayer extends Player {
	protected final String nickname;
	protected final PlayerType playerType;

	static final Map<Long, ProprietaryPlayer> computerPlayerMap = new HashMap<>();

	/**
	 * Creates new computer player with specified parameters.
	 *
	 * @param id         the computer player id. The id must be between 1 and 1000. This ids are
	 *                   reserved for computer players.
	 * @param nickname   the player's nickname.
	 * @param playerType the player's type
	 */
	protected ProprietaryPlayer(long id, String nickname, PlayerType playerType) {
		super(id);
		this.nickname = nickname;
		this.playerType = playerType;

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
	 * Always returns {@code null}
	 *
	 * @return {@code null}
	 */
	@Override
	public String getEmail() {
		return null;
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

	@Override
	public PlayerType getPlayerType() {
		return playerType;
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
}
