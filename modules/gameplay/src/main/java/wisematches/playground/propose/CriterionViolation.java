package wisematches.playground.propose;

import java.io.Serializable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class CriterionViolation implements Serializable {
	private final String code;
	private final Object expected;
	private final Object received;

	private static final long serialVersionUID = 4808346159699224069L;

	public CriterionViolation(String code, Object received) {
		this(code, received, null);
	}

	public CriterionViolation(String code, Object received, Object expected) {
		if (code == null) {
			throw new NullPointerException("Code can't be null");
		}
		this.code = code;
		this.expected = expected;
		this.received = received;
	}

	public String getCode() {
		return code;
	}

	public Object getExpected() {
		return expected;
	}

	public Object getReceived() {
		return received;
	}

	@Override
	public String toString() {
		return "CriterionViolation{" +
				"code='" + code + '\'' +
				", expected=" + expected +
				", received=" + received +
				'}';
	}
}
