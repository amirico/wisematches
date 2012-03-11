package wisematches.playground.restrictions;

import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.playground.GameRestriction;
import wisematches.playground.ViolatedRestrictionException;
import wisematches.playground.tracking.Statistics;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class GameRestrictionPersonality implements GameRestriction {
    private final long playerId;

    private static final long serialVersionUID = -8523418042927916445L;

    public GameRestrictionPersonality(Personality personality) {
        this.playerId = personality.getId();
    }

    @Override
    public void validatePlayer(Player player, Statistics statistics) throws ViolatedRestrictionException {
        final long actual = player.getId();
        if (actual != playerId) {
            throw new ViolatedRestrictionException("playerId", actual, playerId, this);
        }
    }
}