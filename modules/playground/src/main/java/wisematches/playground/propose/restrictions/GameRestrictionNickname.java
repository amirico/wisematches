package wisematches.playground.propose.restrictions;

import wisematches.personality.player.Player;
import wisematches.playground.propose.GameRestriction;
import wisematches.playground.propose.ViolatedRestrictionException;

/**
 * Validates player's nickname and throws {@code ViolatedRestrictionException} with code {@code nickname}
 * if player has incorrect nickname.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class GameRestrictionNickname implements GameRestriction {
    private final boolean matcher;
    private final String nickname;

    private static final long serialVersionUID = -5862537948675200684L;

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
