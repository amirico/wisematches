package wisematches.database;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class Range {
	private final int start;
	private final int count;

	protected Range(int start, int count) {
		this.start = start;
		this.count = count;
	}

	public static Range limit(int start, int count) {
		return new Range(start, count);
	}

	public int getStart() {
		return start;
	}

	public int getCount() {
		return count;
	}
}
