package wisematches.server.services.award;

import java.util.EnumSet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class AwardContext {
	private String code;
	private EnumSet<AwardWeight> weights;

	public AwardContext(String code, EnumSet<AwardWeight> weights) {
		this.code = code;
		this.weights = weights;
	}

	public String getCode() {
		return code;
	}

	public EnumSet<AwardWeight> getWeights() {
		return weights;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("AwardContext");
		sb.append("{code='").append(code).append('\'');
		sb.append(", weights=").append(weights);
		sb.append('}');
		return sb.toString();
	}
}
