package wisematches.server.standing.rating;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum RatingBatching {
	DAY("player.rating.day"),

	MONTH("player.rating.month");

	private final String queryName;

	RatingBatching(String queryName) {
		this.queryName = queryName;
	}

	public String getQueryName() {
		return queryName;
	}
}
