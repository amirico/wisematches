package wisematches.database;

import org.hibernate.Criteria;
import org.hibernate.Query;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class Range {
	private final int firstResult;
	private final int maxResults;

	public static final Range NO = null;

	protected Range(int firstResult, int maxResults) {
		this.firstResult = firstResult;
		this.maxResults = maxResults;
	}

	public static Range no() {
		return NO;
	}

	public static Range from(int start) {
		return limit(start, -1);
	}

	public static Range limit(int count) {
		return limit(-1, count);
	}

	public static Range limit(int start, int count) {
		return new Range(start, count);
	}

	public int getFirstResult() {
		return firstResult;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public void apply(Query query) {
		if (this == NO) {
			return;
		}

		if (firstResult != -1) {
			query.setFirstResult(firstResult);
		}
		if (maxResults != -1) {
			query.setMaxResults(maxResults);
		}
	}

	public void apply(Criteria criteria) {
		if (this == NO) {
			return;
		}

		if (firstResult != -1) {
			criteria.setFirstResult(firstResult);
		}
		if (maxResults != -1) {
			criteria.setMaxResults(maxResults);
		}
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Range");
		sb.append("{firstResult=").append(firstResult);
		sb.append(", maxResults=").append(maxResults);
		sb.append('}');
		return sb.toString();
	}
}
