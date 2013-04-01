package wisematches.server.services.award;

import java.util.EnumSet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class AwardContext {
	private AwardDescriptor descriptor;
	private EnumSet<AwardWeight> weights;

	public AwardContext(AwardDescriptor descriptor, EnumSet<AwardWeight> weights) {
		this.descriptor = descriptor;
		this.weights = weights;
	}

	public AwardDescriptor getDescriptor() {
		return descriptor;
	}

	public EnumSet<AwardWeight> getWeights() {
		return weights;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("AwardContext{");
		sb.append("descriptor=").append(descriptor);
		sb.append(", weights=").append(weights);
		sb.append('}');
		return sb.toString();
	}
}
