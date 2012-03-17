package wisematches.playground.propose.impl;

import wisematches.personality.player.Player;
import wisematches.playground.propose.a.GameRestriction;
import wisematches.playground.GameSettings;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class DefaultGameWaiting<S extends GameSettings> extends AbstractGameProposal<S> implements GameWaiting<S> {
    private final GameRestriction gameRestriction;

    private static final long serialVersionUID = -7397713360246859828L;

    public DefaultGameWaiting(long id, S gameSettings, Player initiator, int playersCount) {
        this(id, gameSettings, playersCount, initiator, null);
    }

    public DefaultGameWaiting(long id, S gameSettings, int playersCount, Player initiator, GameRestriction gameRestriction) {
        super(id, gameSettings, playersCount, initiator);
        this.gameRestriction = gameRestriction;
    }

    @Override
    protected void validateRestrictions(Player player) throws ViolatedCriterionException {
        if (gameRestriction != null) {
            gameRestriction.validatePlayer(player, null);
        }
    }

    @Override
    public GameRestriction getRestriction() {
        return gameRestriction;
    }
}
