package wisematches.server.standing.rating;

import java.util.Collection;
import java.util.Collections;

/**
 * Contains information about player's rating.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class RatingHistory {
	private long playerId;
	private Collection<RatingChange> ratingChanges;

	RatingHistory() {
	}

	public RatingHistory(long playerId, Collection<RatingChange> ratingChanges) {
		this.playerId = playerId;
		this.ratingChanges = ratingChanges;
	}

	/**
	 * Returns player id for this history.
	 *
	 * @return the player id for this history.
	 */
	public long getPlayerId() {
		return playerId;
	}

	/**
	 * Returns collection of rating changes sorted by date.
	 *
	 * @return the collection of rating changes.
	 */
	public Collection<RatingChange> getRatingChanges() {
		return Collections.unmodifiableCollection(ratingChanges);
	}
}
