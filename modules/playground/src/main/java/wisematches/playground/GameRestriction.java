package wisematches.playground;

import wisematches.personality.player.Player;
import wisematches.playground.tracking.Statistics;

import java.io.Serializable;

/**
 * {@code GameRestriction} checks player and he/she's statistic according to some internal rules and
 * throws {@code ViolatedRestrictionException} in case of any discrepancies.
 * <p/>
 * Each restriction has a name that can be used to get localized message.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GameRestriction extends Serializable {
/*
    */
/**
 * Returns restriction name.
 *
 * @return the restriction name.
 *//*

    String getName();

    */
/**
 * Returns expected value for the restriction. The returned object must redefine {@code toString} method
 * because can be used in localization.
 *
 * @return the expected value.
 *//*

    Object getExpectedValue();
*/

    /**
     * @param player
     * @param statistics
     * @throws ViolatedRestrictionException
     */
    void validatePlayer(Player player, Statistics statistics) throws ViolatedRestrictionException;
}
