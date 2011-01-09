package wisematches.server.games.scribble.bank;

import java.util.Locale;

/**
 * {@code TilesBankingHouse} allows create new {@code TilesBank} by some parameters.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface TilesBankingHouse {
    /**
     * Creates new {@code TilesBank} for specified locale.
     * <p/>
     * Returned tiles bank can contains different number of tiles depending on number of players. For example,
     * implentatin can returns two wildcard tiles (if allowed) for each player in game or it can returns only
     * fixed number of wildcard tiles).
     * <p/>
     * If {@code wildcardAllowed} is disabled returned {@code TilesBank} won't contains wilcard tiles at all.
     *
     * @param locale          the locale of tiles bank.
     * @param playersCount    the number of players in game.
     * @param wildcardAllowed is wild card tiles allowed or not. {@code true} of allow wildcards;
     *                        {@code false} - to deny wildcard.
     * @return creted tiles bank
     * @throws UnsupportedLocaleException if tiles bank for specified locale can't be loaded.
     * @throws NullPointerException       if {@code locale} is null
     * @throws IllegalArgumentException   if number of players is negative
     */
    TilesBank createTilesBank(Locale locale, int playersCount, boolean wildcardAllowed);
}
