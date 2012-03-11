package wisematches.playground.restrictions;

import wisematches.playground.GameRestriction;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class GameRestrictionJunction {
    private GameRestrictionJunction() {
    }

    public static GameRestriction or(GameRestriction r1, GameRestriction... rn) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public static GameRestriction and(GameRestriction r1, GameRestriction... rn) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
