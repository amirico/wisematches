package wisematches.playground.tracking;

import java.util.Date;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class RatingCurve {
	private final int resolution;
	private final Date startDate;
	private final Date endDate;

	private final int pointsCount;
	private final short minRating;
	private final short maxRating;

	private final short[] points;
	private final short[] ratingsMin;
	private final short[] ratingsAvg;
	private final short[] ratingsMax;

	RatingCurve() {
		throw new UnsupportedOperationException("Not supported");
	}

	public RatingCurve(int resolution, Date startDate, Date endDate, List list) {
		this.resolution = resolution;
		this.startDate = startDate;
		this.endDate = endDate;

		this.points = new short[list.size()];
		this.ratingsMin = new short[list.size()];
		this.ratingsAvg = new short[list.size()];
		this.ratingsMax = new short[list.size()];

		this.pointsCount = ((int) ((endDate.getTime() - startDate.getTime()) / 1000 / 60 / 60 / 24)) / resolution;

		int index = 0;
		short min = 1200;
		short max = 1200;
		for (Object values : list) {
			final Object[] row = (Object[]) values;
			this.points[index] = ((Number) row[0]).shortValue();
			this.ratingsMin[index] = ((Number) row[1]).shortValue();
			this.ratingsAvg[index] = ((Number) row[2]).shortValue();
			this.ratingsMax[index] = ((Number) row[3]).shortValue();

			if (max < this.ratingsMax[index]) {
				max = this.ratingsMax[index];
			}
			if (min > this.ratingsMin[index]) {
				min = this.ratingsMin[index];
			}
			index++;
		}
		minRating = min;
		maxRating = max;
	}

	public int getResolution() {
		return resolution;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public int getPointsCount() {
		return pointsCount;
	}

	public short[] getRatingPoints() {
		return points;
	}

	public short[] getRatingsMin() {
		return ratingsMin;
	}

	public short[] getRatingsAvg() {
		return ratingsAvg;
	}

	public short[] getRatingsMax() {
		return ratingsMax;
	}

	public short getMinRating() {
		return minRating;
	}

	public short getMaxRating() {
		return maxRating;
	}
}