package wisematches.playground.propose;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class CriterionViolationException extends Exception {
	private final Collection<CriterionViolation> violations;

	public CriterionViolationException(CriterionViolation violation) {
		this.violations = Collections.singleton(violation);
	}

	public CriterionViolationException(CriterionViolation[] violations) {
		this.violations = Arrays.asList(violations);
	}

	public CriterionViolationException(Collection<CriterionViolation> violations) {
		this.violations = new ArrayList<CriterionViolation>(violations);
	}

	public CriterionViolation getCriterion() {
		if (violations != null && violations.size() > 0) {
			return violations.iterator().next();
		}
		return null;
	}

	public Collection<CriterionViolation> getCriteria() {
		return Collections.unmodifiableCollection(violations);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("CriterionViolationException");
		sb.append("{criteria=").append(violations);
		sb.append('}');
		return sb.toString();
	}
}
