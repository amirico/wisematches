package wisematches.server.web.utils;

import wisematches.server.standing.rating.RatingCurve;

import java.util.Calendar;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class RatingChart {
	private final RatingCurve ratingCurve;
	private final int[] monthIndexes = new int[12];

	public RatingChart(Calendar calendar, RatingCurve ratingCurve) {
		this.ratingCurve = ratingCurve;

		final int middle = calendar.get(Calendar.MONTH) + 1;
		for (int i = 1; i <= 12; i++) {
			int pos = i > middle ? i - 12 : i;
			monthIndexes[11 - middle + pos] = i;
		}
	}

	public int getStartPoint() {
		return 0;
	}

	public int getEndPoint() {
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

	public String getEncodedPoints() {
		return GoogleChartTools.encodeExtended(ratingCurve.getRatingPoints(), 0, ratingCurve.getPointsCount());
	}

	public String getEncodedRatingsMin() {
		return GoogleChartTools.encodeExtended(ratingCurve.getRatingsMin(), getMinRating(), getMaxRating());
	}

	public String getEncodedRatingsAvg() {
		return GoogleChartTools.encodeExtended(ratingCurve.getRatingsAvg(), getMinRating(), getMaxRating());
	}

	public String getEncodedRatingsMax() {
		return GoogleChartTools.encodeExtended(ratingCurve.getRatingsMax(), getMinRating(), getMaxRating());
	}
}