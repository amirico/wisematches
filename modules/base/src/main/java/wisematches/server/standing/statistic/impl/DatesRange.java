package wisematches.server.standing.statistic.impl;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class DatesRange {
	private final long startTime;
	private final long endTime;

	public DatesRange(long startTime, long endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	private boolean belongsTo(long time) {
		return time >= startTime && time <= endTime;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		DatesRange that = (DatesRange) o;
		return endTime == that.endTime && startTime == that.startTime;
	}

	@Override
	public int hashCode() {
		int result = (int) (startTime ^ (startTime >>> 32));
		result = 31 * result + (int) (endTime ^ (endTime >>> 32));
		return result;
	}

	@Override
	public String toString() {
		return "DatesRange{" +
				"startTime=" + startTime +
				", endTime=" + endTime +
				'}';
	}
}
