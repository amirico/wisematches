package wisematches.server.standing.rating;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Deprecated
public enum RatingBatching {
	DAY(3),

	MONTH(10);

	private final int radix;

	RatingBatching(int radix) {
		this.radix = radix;
	}

	public int getRadix() {
		return radix;
	}
}
