package wisematches.playground.propose.restrictions;

import wisematches.personality.player.Player;
import wisematches.playground.propose.GameRestriction;
import wisematches.playground.propose.ViolatedRestrictionException;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class GameRestrictionPlayerId implements GameRestriction {
    private final long playerId;

    public GameRestrictionPlayerId(long playerId) {
        this.playerId = playerId;
    }

    @Override
    public void validatePlayer(Player player) throws ViolatedRestrictionException {
        final long actual = player.getId();
        if (actual != playerId) {
            throw new ViolatedRestrictionException("playerId", actual, playerId, this);
        }
    }
}