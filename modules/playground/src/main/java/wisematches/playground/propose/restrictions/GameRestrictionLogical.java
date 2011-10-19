package wisematches.playground.propose.restrictions;

import wisematches.playground.propose.GameRestriction;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class GameRestrictionLogical {
    private GameRestrictionLogical() {
    }

    public static GameRestriction or(GameRestriction r1, GameRestriction... rn) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public static GameRestriction and(GameRestriction r1, GameRestriction... rn) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
