package wisematches.server.web.services.search.player;

import wisematches.database.Order;
import wisematches.database.Range;
import wisematches.personality.Personality;
import wisematches.server.web.services.search.SearchCriteria;

import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface PlayerSearchManager {
	int getPlayersCount(Personality person, PlayerSearchArea area, SearchCriteria criteria);

	List<PlayerInfoBean> getPlayerBeans(Personality person, PlayerSearchArea area, SearchCriteria criteria, Range range, Order... order);
}
