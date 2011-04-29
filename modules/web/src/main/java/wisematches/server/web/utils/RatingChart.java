package wisematches.server.web.utils;

import wisematches.server.standing.rating.RatingBatch;
import wisematches.server.standing.rating.RatingPeriod;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class RatingChart {
	private final int startDate;
	private final int endDate;
	private final int minRating;
	private final int maxRating;
	private final int[] dates;
	private final int[] ratingsMin;
	private final int[] ratingsMax;
	private final int[] ratingsAvg;
	private final int[] monthIndexes = new int[12];

	public RatingChart(Calendar calendar, RatingPeriod period, Collection<RatingBatch> batches) {
		startDate = 0;
		endDate = period.getDaysNumber();

		int index = 0;
		int min = 1200;
		int max = 1200;
		dates = new int[batches.size()];
		ratingsAvg = new int[batches.size()];
		ratingsMax = new int[batches.size()];
		ratingsMin = new int[batches.size()];
		Arrays.fill(ratingsAvg, -1);
		Arrays.fill(ratingsMax, -1);
		Arrays.fill(ratingsMin, -1);
		for (RatingBatch playerBatch : batches) {
			if (max < playerBatch.getRatingMax()) {
				max = playerBatch.getRatingMax();
			}
			if (min > playerBatch.getRatingMin()) {
				min = playerBatch.getRatingMin();
			}
			dates[index] = playerBatch.getPosition();
			ratingsAvg[index] = playerBatch.getRatingAvg();
			ratingsMax[index] = playerBatch.getRatingMax();
			ratingsMin[index] = playerBatch.getRatingMin();
			index++;
		}

		minRating = (int) Math.ceil((min / 100) * 100 - 100);
		maxRating = (int) Math.floor((max / 100) * 100 + 100);

		final int middle = calendar.get(Calendar.MONTH) + 1;
		for (int i = 1; i <= 12; i++) {
			int pos = i > middle ? i - 12 : i;
			monthIndexes[11 - middle + pos] = i;
		}
	}

	public int getStartDate() {
		return startDate;
	}

	public int getEndDate() {
		return endDate;
	}

	public int[] getDates() {
		return dates;
	}

	public int getMaxRating() {
		return maxRating;
	}

	public int getMinRating() {
		return minRating;
	}

	public int[] getRatingsMin() {
		return ratingsMin;
	}

	public int[] getRatingsMax() {
		return ratingsMax;
	}

	public int[] getRatingsAvg() {
		return ratingsAvg;
	}

	public int[] getMonthIndexes() {
		return monthIndexes;
	}

	public String getEncodedDates() {
		return GoogleChartTools.encodeExtended(dates, startDate, endDate);
	}

	public String getEncodedRatingsMin() {
		return GoogleChartTools.encodeExtended(ratingsMax, minRating, maxRating);
	}

	public String getEncodedRatingsAvg() {
		return GoogleChartTools.encodeExtended(ratingsAvg, minRating, maxRating);
	}

	public String getEncodedRatingsMax() {
		return GoogleChartTools.encodeExtended(ratingsMax, minRating, maxRating);
	}
}