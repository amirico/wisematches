package wisematches.server.web.utils;

import wisematches.playground.tracking.RatingChangesCurve;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class RatingChart {
	private final RatingChangesCurve ratingCurve;
	private final int[] monthIndexes = new int[12];

	public RatingChart(RatingChangesCurve ratingCurve, int endMonth) {
		this.ratingCurve = ratingCurve;

		for (int i = 1; i <= 12; i++) {
			int pos = i > endMonth ? i - 12 : i;
			monthIndexes[11 - endMonth + pos] = i;
		}
	}

	public int getPointsCount() {
		return ratingCurve.getPointsCount();
	}

	public short getMaxRating() {
		return (short) Math.ceil((ratingCurve.getMaxRating() / 100) * 100 + 100);
	}

	public short getMinRating() {
		return (short) Math.ceil((ratingCurve.getMinRating() / 100) * 100 - 100);
	}

	public short[] getRatingsPoint() {
		return ratingCurve.getRatingPoints();
	}

	public short[] getRatingsMin() {
		return ratingCurve.getRatingsMin();
	}

	public short[] getRatingsMax() {
		return ratingCurve.getRatingsMax();
	}

	public short[] getRatingsAvg() {
		return ratingCurve.getRatingsAvg();
	}

	public int[] getMonthIndexes() {
		return monthIndexes;
	}
}