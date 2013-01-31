package wisematches.playground.propose.criteria;

import wisematches.playground.propose.CriterionViolation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ViolatedCriteriaException extends Exception {
	private final Collection<CriterionViolation> violations;

	public ViolatedCriteriaException(CriterionViolation violation) {
		this.violations = Collections.singleton(violation);
	}

	public ViolatedCriteriaException(CriterionViolation[] violations) {
		this.violations = Arrays.asList(violations);
	}

	public ViolatedCriteriaException(Collection<CriterionViolation> violations) {
		this.violations = new ArrayList<CriterionViolation>(violations);
	}

	public CriterionViolation getViolatedCriterion() {
		if (violations != null && violations.size() > 0) {
			return violations.iterator().next();
		}
		return null;
	}

	public Collection<CriterionViolation> getViolatedCriteria() {
		return Collections.unmodifiableCollection(violations);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("ViolatedCriteriaException");
		sb.append("{violations=").append(violations);
		sb.append('}');
		return sb.toString();
	}
}
