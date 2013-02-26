package wisematches.playground.restriction;

import wisematches.core.Membership;
import wisematches.core.Player;

import java.util.Collection;

/**
 * {@code RestrictionManager} provides access to player's restrictions based on predefined descriptions.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface RestrictionManager {
	/**
	 * Checks is the manager has restriction with specified name.
	 *
	 * @param name the restriction name to be checked.
	 * @return {@code true} if manager has specified restriction; {@code false} - otherwise.
	 */
	boolean containsRestriction(String name);

	/**
	 * Returns collection of all supported by this manager restrictions
	 *
	 * @return unmodifiable collection of all supported by this manager restrictions.
	 */
	Collection<String> getRestrictionNames();


	/**
	 * Returns current restriction threshold for specified restriction and player.
	 *
	 * @param name   the name of restriction.
	 * @param player the membership to be checked.
	 * @return current restriction threshold or {@code null} if there are no any restrictions.
	 * @throws IllegalArgumentException if specified restriction name is unknown.
	 * @throws NullPointerException     if {@code name} or {@code membership} is {@code null}
	 */
	Comparable getRestrictionThreshold(String name, Player player);

	/**
	 * Returns current restriction threshold for specified restriction and player.
	 *
	 * @param name   the name of restriction.
	 * @param player the membership to be checked.
	 * @return current restriction threshold or {@code null} if there are no any restrictions.
	 * @throws IllegalArgumentException if specified restriction name is unknown.
	 * @throws NullPointerException     if {@code name} or {@code membership} is {@code null}
	 */
	Comparable getRestrictionThreshold(String name, Membership player);


	/**
	 * Checks and returns restriction description for specified player.
	 * <p/>
	 * Manager checks that current {@code value} is less when value for specified restriction. If so,
	 * method returns {@code null} otherwise it returns {@code Restriction} object.
	 *
	 * @param player
	 * @param name   the restriction name
	 * @param value  current value for this restriction.
	 * @return {@code null} if player doesn't break restriction's threshold; {@code false} if access to specified
	 *         functionality is restricted for specified player.
	 */
	Restriction validateRestriction(Player player, String name, Comparable value);
}
