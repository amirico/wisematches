package wisematches.server.playground.propose;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class ViolatedRestrictionException extends Exception {
	private final String code;
	private final Object actualValue;
	private final Object expectedValue;
	private final GameRestriction gameRestriction;

	public ViolatedRestrictionException(String code) {
		this.code = code;
		actualValue = null;
		expectedValue = null;
		gameRestriction = null;
	}

	public ViolatedRestrictionException(String code, Object actualValue, Object expectedValue, GameRestriction gameRestriction) {
		this.code = code;
		this.actualValue = actualValue;
		this.expectedValue = expectedValue;
		this.gameRestriction = gameRestriction;
	}

	public String getCode() {
		return code;
	}

	public Object getActualValue() {
		return actualValue;
	}

	public Object getExpectedValue() {
		return expectedValue;
	}

	public GameRestriction getGameRestriction() {
		return gameRestriction;
	}
}