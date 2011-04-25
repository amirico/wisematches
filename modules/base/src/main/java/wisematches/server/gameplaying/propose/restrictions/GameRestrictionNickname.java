package wisematches.server.gameplaying.propose.restrictions;

import wisematches.server.gameplaying.propose.GameRestriction;
import wisematches.server.gameplaying.propose.ViolatedRestrictionException;
import wisematches.server.personality.player.Player;

/**
 * Validates player's nickname and throws {@code ViolatedRestrictionException} with code {@code nickname}
 * if player has incorrect nickname.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class GameRestrictionNickname implements GameRestriction {
	private final boolean matcher;
	private final String nickname;

	public GameRestrictionNickname(String nickname) {
		this(nickname, false);
	}

	public GameRestrictionNickname(String nickname, boolean matcher) {
		this.nickname = nickname;
		this.matcher = matcher;
	}

	@Override
	public void validatePlayer(Player player) throws ViolatedRestrictionException {
		final String actual = player.getNickname();
		if (matcher) {
			if (!actual.matches(nickname)) {
				throw new ViolatedRestrictionException("nickname", actual, nickname, this);
			}
		} else {
			if (!actual.equals(nickname)) {
				throw new ViolatedRestrictionException("nickname", actual, nickname, this);
			}
		}
	}
}
