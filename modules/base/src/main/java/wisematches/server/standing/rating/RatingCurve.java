package wisematches.server.standing.rating;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@NamedNativeQueries({
		@NamedNativeQuery(
				name = "player.rating.curve",
				query = "SELECT ROUND(((UNIX_TIMESTAMP(date(time))-UNIX_TIMESTAMP(date(:start)))/60/60/24)/:resolution) as position, " +
						"min(newRating) as ratingMin, avg(newRating) as ratingAvg, max(newRating) as ratingMax " +
						"FROM rating_history " +
						"WHERE playerId=:pid AND time>:start AND time<=:end GROUP BY YEAR(time), ROUND(DAYOFYEAR(time)/:resolution) " +
						"ORDER BY position ASC",
				resultSetMapping = "player.rating.curve")
})
@SqlResultSetMapping(name = "player.rating.curve", columns = {
		@ColumnResult(name = "position"),
		@ColumnResult(name = "ratingAvg"),
		@ColumnResult(name = "ratingMax"),
		@ColumnResult(name = "ratingMin")
})
public class RatingCurve {
	@Id
	private final int resolution;
	private final Date startDate;
	private final Date endDate;

	@Transient
	private final int pointsCount;
	@Transient
	private final short[] points;
	@Transient
	private final short[] ratingsMin;
	@Transient
	private final short[] ratingsAvg;
	@Transient
	private final short[] ratingsMax;

	@Transient
	private final short minRating;

	@Transient
	private final short maxRating;

	@Deprecated
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

	/*	private final int startDate;
		   private final int endDate;
		   private final int minRating;
		   private final int maxRating;
		   private final int[] dates;
		   private final int[] ratingsMin;
		   private final int[] ratingsMax;
		   private final int[] ratingsAvg;
		   private final int[] monthIndexes = new int[12];

		   public RatingCurve() {
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
		   }*/
}