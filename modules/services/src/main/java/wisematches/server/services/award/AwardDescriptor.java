package wisematches.server.services.award;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class AwardDescriptor {
	private final int code;
	private final String name;
	private final AwardType type;

	public AwardDescriptor(int code, String name, AwardType type) {
		this.code = code;
		this.name = name;
		this.type = type;
	}

	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public AwardType getType() {
		return type;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("AwardDescriptor{");
		sb.append("code=").append(code);
		sb.append(", name='").append(name).append('\'');
		sb.append(", type=").append(type);
		sb.append('}');
		return sb.toString();
	}
}
