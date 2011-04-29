package wisematches.server.standing.rating;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Deprecated
public enum RatingPeriod {
	YEAR(365);

	private final int daysNumber;

	RatingPeriod(int daysNumber) {
		this.daysNumber = daysNumber;
	}

	public int getDaysNumber() {
		return daysNumber;
	}
}
