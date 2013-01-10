package wisematches.playground.award;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class AwardDescriptor {
	private final String code;
	private final AwardType type;

	public AwardDescriptor(String code, AwardType type) {
		this.type = type;
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public AwardType getType() {
		return type;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("AwardDescriptor");
		sb.append("{code='").append(code).append('\'');
		sb.append(", type=").append(type);
		sb.append('}');
		return sb.toString();
	}
}
