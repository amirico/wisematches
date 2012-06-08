package wisematches.playground.tournament;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WrongAnnouncementException extends WrongSubscriptionException {
	private final int currentNumber;
	private final int expectedNumber;

	public WrongAnnouncementException(int currentNumber, int expectedNumber) {
		this(currentNumber, expectedNumber, null);
	}

	public WrongAnnouncementException(int currentNumber, int expectedNumber, String message) {
		super(message);
		this.currentNumber = currentNumber;
		this.expectedNumber = expectedNumber;
	}

	public int getCurrentNumber() {
		return currentNumber;
	}

	public int getExpectedNumber() {
		return expectedNumber;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("WrongAnnouncementException");
		sb.append("{currentNumber=").append(currentNumber);
		sb.append(", expectedNumber=").append(expectedNumber);
		sb.append('}');
		return sb.toString();
	}
}
