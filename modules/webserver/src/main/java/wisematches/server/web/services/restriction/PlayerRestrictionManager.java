package wisematches.server.web.services.restriction;

import wisematches.personality.player.Player;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface PlayerRestrictionManager {
	Comparable getRestriction(Player player, String name);

	boolean hasRestriction(Player player, String name);

	void checkRestriction(Player player, String name, Comparable value) throws RestrictionException;

	boolean isRestricted(Player player, String name, Comparable value);
}
