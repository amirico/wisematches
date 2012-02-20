package wisematches.playground.search;

import wisematches.database.Order;
import wisematches.database.Range;
import wisematches.personality.Personality;

import java.util.List;

/**
 * This is wrapper for {@code EntitySearchManager} interface. It's not always possible to return entity search
 * manager with required generics but it can be done using that wrapper.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class EntitySearchManagerWrapper<E extends DesiredEntityBean<C>, C> implements EntitySearchManager<E, C> {
	private final EntitySearchManager<E, C> entitySearchManager;

	@SuppressWarnings("unchecked")
	public EntitySearchManagerWrapper(EntitySearchManager<?, ?> entitySearchManager) {
		this.entitySearchManager = (EntitySearchManager<E, C>) entitySearchManager;
	}

	@Override
	public DesiredEntityDescriptor getDescriptor() {
		return entitySearchManager.getDescriptor();
	}

	@Override
	public int getTotalCount(Personality person, C context) {
		return entitySearchManager.getTotalCount(person, context);
	}

	@Override
	public int getFilteredCount(Personality person, C context, SearchCriteria[] criteria) {
		return entitySearchManager.getFilteredCount(person, context, criteria);
	}

	@Override
	public List<E> searchEntities(Personality person, C context, SearchCriteria[] criteria, Order[] order, Range range) {
		return entitySearchManager.searchEntities(person, context, criteria, order, range);
	}
}

