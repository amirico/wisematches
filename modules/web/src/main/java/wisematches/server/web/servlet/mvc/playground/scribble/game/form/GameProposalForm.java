package wisematches.server.web.servlet.mvc.playground.scribble.game.form;

import wisematches.playground.propose.CriterionViolation;
import wisematches.playground.propose.GameProposal;
import wisematches.playground.scribble.ScribbleSettings;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Deprecated
public class GameProposalForm {
	private final GameProposal<ScribbleSettings> proposal;
	private final Collection<CriterionViolation> violations;

	public GameProposalForm(GameProposal<ScribbleSettings> proposal, Collection<CriterionViolation> violations) {
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
