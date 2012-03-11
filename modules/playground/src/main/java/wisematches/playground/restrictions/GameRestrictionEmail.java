package wisematches.playground.restrictions;

import wisematches.personality.player.Player;
import wisematches.playground.GameRestriction;
import wisematches.playground.ViolatedRestrictionException;
import wisematches.playground.tracking.Statistics;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class GameRestrictionEmail implements GameRestriction {
    private final String email;

    private static final long serialVersionUID = -8668714084495105235L;

    public GameRestrictionEmail(String email) {
        this.email = email;
    }

    @Override
    public void validatePlayer(Player player, Statistics statistics) throws ViolatedRestrictionException {
        final String actual = player.getEmail();
        if (!actual.equalsIgnoreCase(email)) {
            throw new ViolatedRestrictionException("email", actual, email, this);
        }
    }
}
