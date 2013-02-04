package wisematches.server.web.controllers.playground.scribble.view;

import wisematches.playground.propose.CriterionViolation;
import wisematches.playground.propose.GameProposal;
import wisematches.playground.scribble.ScribbleSettings;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class GameProposalView {
	private final GameProposal<ScribbleSettings> proposal;
	private final Collection<CriterionViolation> violations;

	public GameProposalView(GameProposal<ScribbleSettings> proposal, Collection<CriterionViolation> violations) {
		this.proposal = proposal;
		this.violations = violations;
	}

	public GameProposal<ScribbleSettings> getProposal() {
		return proposal;
	}

	public Collection<CriterionViolation> getViolations() {
		return violations;
	}
}
