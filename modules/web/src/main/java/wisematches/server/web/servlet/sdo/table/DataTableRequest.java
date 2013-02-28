package wisematches.server.web.servlet.sdo.table;

import wisematches.core.search.Order;
import wisematches.core.search.Orders;
import wisematches.core.search.Range;

import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DataTableRequest {
	private final Range limit;
	private final Object echo;
	private final Orders orders;

	public DataTableRequest(final Map<String, Object> request) {
		echo = request.get("sEcho");

		final Order[] orders = new Order[(Integer) request.get("iSortingCols")];
		for (int i = 0; i < orders.length; i++) {
			final Integer col = (Integer) request.get("iSortCol_" + i);
			final String name = (String) request.get("mDataProp_" + col);
			if ("asc".equalsIgnoreCase((String) request.get("sSortDir_" + i))) {
				orders[i] = Order.asc(name);
			} else {
				orders[i] = Order.desc(name);
			}
		}

		this.limit = Range.limit((Integer) request.get("iDisplayStart"), (Integer) request.get("iDisplayLength"));
		this.orders = Orders.all(orders);
	}

	public Range getLimit() {
		return limit;
	}

	public Object getEcho() {
		return echo;
	}

	public Orders getOrders() {
		return orders;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("DataTableRequest{");
		sb.append("limit=").append(limit);
		sb.append(", orders=").append(orders);
		sb.append('}');
		return sb.toString();
	}
}
