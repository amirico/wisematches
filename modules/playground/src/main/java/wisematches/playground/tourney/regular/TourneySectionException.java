package wisematches.playground.tourney.regular;

import wisematches.playground.tourney.TourneySubscriptionException;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TourneySectionException extends TourneySubscriptionException {
	private final int currentRating;
	private final int expectedRating;

	public TourneySectionException(int currentRating, int expectedRating) {
		this(currentRating, expectedRating, null);
	}

	public TourneySectionException(int currentRating, int expectedRating, String message) {
		super(message);
		this.currentRating = currentRating;
		this.expectedRating = expectedRating;
	}

	public int getCurrentRating() {
		return currentRating;
	}

	public int getExpectedRating() {
		return expectedRating;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("WrongSectionException");
		sb.append("{currentRating=").append(currentRating);
		sb.append(", expectedRating=").append(expectedRating);
		sb.append('}');
		return sb.toString();
	}
}
