package wisematches.server.standing.rating.impl;

import javax.persistence.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "account_personality")
@NamedQueries({
		@NamedQuery
				(name = "PlayerPosition", query =
						"SELECT count( a.id ), a.id " +
								"FROM wisematches.server.standing.rating.impl.HibernatePlayerRating a, wisematches.server.standing.rating.impl.HibernatePlayerRating b " +
								"WHERE a.id = ? AND (b.rating > a.rating OR (b.rating = a.rating AND b.id <= a.id)) GROUP BY a.id",
						hints = {@QueryHint(name = "org.hibernate.cacheable", value = "true")}
				)
})
class HibernatePlayerRating {
	@Id
	@Column(name = "id", updatable = false, insertable = false, nullable = false, unique = true)
	private long playerId;

	@Basic
	@Column(name = "rating", updatable = true, insertable = true, nullable = false, unique = false)
	private short rating;

	HibernatePlayerRating() {
	}

	HibernatePlayerRating(long playerId, short rating) {
		this.playerId = playerId;
		this.rating = rating;
	}

	long getPlayerId() {
		return playerId;
	}

	short getRating() {
		return rating;
	}

	void setRating(short rating) {
		this.rating = rating;
	}
}
