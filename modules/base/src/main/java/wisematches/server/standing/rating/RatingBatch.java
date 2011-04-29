package wisematches.server.standing.rating;

import javax.persistence.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
/*
@NamedQueries({

		@NamedQuery(name = "player.rating.month",
				query = "SELECT b.batchNumber, avg(b.rating) " +
						"FROM rating_history b " +
						"WHERE playerId=:pid, DATE_SUB(CURDATE(),INTERVAL 1 YEAR) <= time, ",
				hints = {@QueryHint(name = "org.hibernate.cacheable", value = "true")}
		)
})
*/
@NamedNativeQueries({
		@NamedNativeQuery(
				name = "player.rating.day",
				query = "SELECT DAY(time) as batchNumber, avg(newRating) as rating " +
						"FROM rating_history " +
						"WHERE playerId=:pid AND DATE_SUB(CURDATE(),INTERVAL 1 YEAR) < time " +
						"GROUP BY YEAR(time), MONTH(time), DAY(time)",
				resultClass = RatingBatch.class),
		@NamedNativeQuery(
				name = "player.rating.month",
				query = "SELECT MONTH(time) as batchNumber, avg(newRating) as rating " +
						"FROM rating_history " +
						"WHERE playerId=:pid AND DATE_SUB(CURDATE(),INTERVAL 1 YEAR) < time " +
						"GROUP BY YEAR(time), MONTH(time)",
				resultClass = RatingBatch.class)
})
public class RatingBatch {
	@Id
	private int batchNumber;

	private short rating;

	RatingBatch() {
	}

	public RatingBatch(int batchNumber, short rating) {
		this.batchNumber = batchNumber;
		this.rating = rating;
	}

	public int getBatchNumber() {
		return batchNumber;
	}

	public short getRating() {
		return rating;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("RatingBatch");
		sb.append("{batchNumber=").append(batchNumber);
		sb.append(", rating=").append(rating);
		sb.append('}');
		return sb.toString();
	}
}
