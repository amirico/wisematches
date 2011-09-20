package wisematches.playground.search;

import wisematches.database.Order;
import wisematches.database.Range;
import wisematches.personality.Personality;

import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface EntitySearchManager<T extends DesiredEntityBean<C>, C extends DesiredEntityContext> {
	DesiredEntityDescriptor getDescriptor();

	int getTotalCount(Personality person, C context);

	int getFilteredCount(Personality person, C context, SearchCriteria[] criteria);

	List<T> searchEntities(Personality person, C context, SearchCriteria[] criteria, Order[] order, Range range);
}
