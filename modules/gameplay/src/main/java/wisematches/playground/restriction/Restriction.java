package wisematches.playground.restriction;

import java.io.Serializable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class Restriction implements Serializable {
	private final String name;
	private final Comparable threshold;
	private final Comparable violation;

	public Restriction(String name, Comparable threshold, Comparable violation) {
		this.name = name;
		this.threshold = threshold;
		this.violation = violation;
	}

	public String getName() {
		return name;
	}

	public Comparable getThreshold() {
		return threshold;
	}

	public Comparable getViolation() {
		return violation;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Restriction");
		sb.append("{name='").append(name).append('\'');
		sb.append(", threshold=").append(threshold);
		sb.append(", violation=").append(violation);
		sb.append('}');
		return sb.toString();
	}
}
