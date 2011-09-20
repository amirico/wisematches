package wisematches.playground.search;

import wisematches.database.Order;
import wisematches.database.Range;
import wisematches.personality.Personality;

import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface EntitySearchManager<T extends DesiredEntity> {
	int getTotalCount(Personality person);

	int getFilteredCount(Personality person, SearchCriteria[] criteria);

	List<T> searchEntities(Personality person, SearchCriteria[] criteria, Order[] order, Range range);
}
