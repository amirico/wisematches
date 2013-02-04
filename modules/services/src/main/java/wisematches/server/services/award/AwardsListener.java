package wisematches.server.services.award;

import wisematches.core.Player;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface AwardsListener {
	void playerAwarded(Player player, AwardDescriptor descriptor, Award award);
}
