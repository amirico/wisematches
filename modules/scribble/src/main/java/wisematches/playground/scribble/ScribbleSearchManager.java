package wisematches.playground.scribble;

import org.hibernate.SessionFactory;
import wisematches.core.Player;
import wisematches.core.search.Orders;
import wisematches.core.search.Range;
import wisematches.core.search.SearchFilter;
import wisematches.playground.BoardSearchManager;

import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleSearchManager implements BoardSearchManager<ScribbleDescription, ScribbleContext> {
	private SessionFactory sessionFactory;

	public ScribbleSearchManager() {
	}

	@Override
	public <Ctx extends ScribbleContext> int getTotalCount(Player person, Ctx context) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public <Ctx extends ScribbleContext, Fl extends SearchFilter.NoFilter> int getFilteredCount(Player person, Ctx context, Fl filter) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public <Ctx extends ScribbleContext, Fl extends SearchFilter.NoFilter> List<ScribbleDescription> searchEntities(Player person, Ctx context, Fl filter, Orders orders, Range range) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}
}
