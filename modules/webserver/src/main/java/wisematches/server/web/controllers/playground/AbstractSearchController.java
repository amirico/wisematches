package wisematches.server.web.controllers.playground;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.database.Order;
import wisematches.database.Range;
import wisematches.personality.Personality;
import wisematches.playground.search.DesiredEntityBean;
import wisematches.playground.search.DesiredEntityDescriptor;
import wisematches.playground.search.EntitySearchManager;
import wisematches.server.web.controllers.WisematchesController;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class AbstractSearchController<T extends DesiredEntityBean<C>, C> extends WisematchesController {
	private final String[] columns;

	private EntitySearchManager<T, C> entitySearchManager;

	private static final Object[] EMPTY_DATA = new Object[0];

	private static final Log log = LogFactory.getLog("wisematches.server.web.search");

	public AbstractSearchController(String[] columns) {
		this.columns = columns;
	}

	public String[] getColumns() {
		return columns;
	}

	public DesiredEntityDescriptor getDesiredEntityDescriptor() {
		return entitySearchManager.getDescriptor();
	}

	protected Map<String, Object> loadData(Personality personality, final Map<String, Object> request, final C context, final Locale locale) {
		final int gamesCount = entitySearchManager.getTotalCount(personality, context);
		final int displayGamesCount = gamesCount;

		final Map<String, Object> res = new HashMap<String, Object>();
		res.put("sEcho", request.get("sEcho"));
		res.put("iTotalRecords", gamesCount);
		res.put("iTotalDisplayRecords", displayGamesCount);

		if (gamesCount == 0) {
			res.put("aaData", EMPTY_DATA);
		} else {
			final Order[] orders = new Order[(Integer) request.get("iSortingCols")];
			for (int i = 0; i < orders.length; i++) {
				final String name = columns[(Integer) request.get("iSortCol_" + i)];
				if ("asc".equalsIgnoreCase((String) request.get("sSortDir_" + i))) {
					orders[i] = Order.asc(name);
				} else {
					orders[i] = Order.desc(name);
				}
			}

			int index = 0;
			final Range limit = Range.limit((Integer) request.get("iDisplayStart"), (Integer) request.get("iDisplayLength"));

			final List<T> response = entitySearchManager.searchEntities(personality, context, null, orders, limit);
			final Object[] data = new Object[response.size()];
			for (T entity : response) {
				final Map<String, Object> a = new HashMap<String, Object>();
				data[index++] = a;
				convertEntity(entity, personality, a, locale);
			}
			res.put("aaData", data);
		}
		return res;
	}

	protected abstract void convertEntity(T entity, Personality personality, Map<String, Object> map, Locale locale);


	public <E extends EntitySearchManager<T, C>> void setEntitySearchManager(E entitySearchManager) {
		this.entitySearchManager = entitySearchManager;
	}
}
