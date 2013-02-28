package wisematches.server.web.servlet.sdo.table;

import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DataTableResponse {
	private final int totalCount;
	private final List<?> rows;
	private final DataTableRequest request;

	private static final Object[] EMPTY_DATA = new Object[0];

	public DataTableResponse(int totalCount, List<?> rows, DataTableRequest request) {
		this.totalCount = totalCount;
		this.rows = rows;
		this.request = request;
	}

	public Object getEcho() {
		return request.getEcho();
	}

	public int getTotalRecords() {
		return totalCount;
	}

	public int getTotalDisplayRecords() {
		return totalCount;
	}

	public Object getData() {
		return rows;
	}
}
