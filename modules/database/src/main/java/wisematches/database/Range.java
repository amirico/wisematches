package wisematches.database;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class Range {
	private final int firstResult;
	private final int maxResults;

	protected Range(int firstResult, int maxResults) {
		this.firstResult = firstResult;
		this.maxResults = maxResults;
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
