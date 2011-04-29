package wisematches.server.standing.rating;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@NamedNativeQueries({
		@NamedNativeQuery(
				name = "player.rating.batch",
				query = "SELECT :range-round((UNIX_TIMESTAMP(:date)-UNIX_TIMESTAMP(date(time)))/60/60/24) as position, " +
						"ROUND(avg(newRating)) as ratingAvg, max(newRating) as ratingMax, min(newRating) as ratingMin " +
						"FROM rating_history " +
						"WHERE playerId=:pid AND time>:date-:range AND time<= :date GROUP BY YEAR(time), ROUND(DAYOFYEAR(time)/:radix) " +
						"ORDER BY position ASC",
				resultClass = RatingBatch.class)
})
public class RatingBatch {
	@Id
	private int position;

	private short ratingMin;

	private short ratingMax;

	private short ratingAvg;

	RatingBatch() {
	}

	public RatingBatch(int position, short ratingMin, short ratingMax, short ratingAvg) {
		this.position = position;
		this.ratingMin = ratingMin;
		this.ratingMax = ratingMax;
		this.ratingAvg = ratingAvg;
	}

	public int getPosition() {
		return position;
	}

	public short getRatingMin() {
		return ratingMin;
	}

	public short getRatingMax() {
		return ratingMax;
	}

	public short getRatingAvg() {
		return ratingAvg;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("RatingBatch");
		sb.append("{ratingAvg=").append(ratingAvg);
		sb.append(", ratingMax=").append(ratingMax);
		sb.append(", ratingMin=").append(ratingMin);
		sb.append(", position=").append(position);
		sb.append('}');
		return sb.toString();
	}
}
