package wisematches.playground.award;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class AwardCondition {
	private final String code;
	private final int threshold;

	public AwardCondition(String code, int threshold) {
		if (code == null) {
			throw new NullPointerException("Code can't be null");
		}
		if (threshold < 1) {
			throw new IllegalArgumentException("Threshold can't be less that 1");
		}

		this.code = code;
		this.threshold = threshold;
	}

	/**
	 * Returns code for this condition.
	 *
	 * @return code for this condition.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Returns threshold for this condition. If it equals to one it means boolean condition.
	 *
	 * @return the threshold for this condition.
	 */
	public int getThreshold() {
		return threshold;
	}

	/**
	 * Indicates that it's boolean condition and should be met only once.
	 *
	 * @return {@code true} if it's boolean condition; {@code false} - otherwise.
	 */
	public boolean isSingular() {
		return threshold == 1;
	}

	/**
	 * Indicates is this condition fulfilled or not.
	 *
	 * @param value current value.
	 * @return {@code true} if this condition is fulfilled; {@code false} - otherwise.
	 */
	public boolean isFulfilled(int value) {
		return value >= threshold;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("AwardCondition");
		sb.append("{code='").append(code).append('\'');
		sb.append(", threshold=").append(threshold);
		sb.append('}');
		return sb.toString();
	}
}
