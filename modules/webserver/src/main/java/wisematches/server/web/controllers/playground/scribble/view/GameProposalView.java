package wisematches.server.web.controllers.playground.scribble.view;

import wisematches.playground.criteria.CriterionViolation;
import wisematches.playground.propose.GameProposal;
import wisematches.playground.scribble.ScribbleSettings;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class GameProposalView {
    private final boolean blacklisted;
    private final GameProposal<ScribbleSettings> proposal;
    private final Collection<CriterionViolation> violations;

    public GameProposalView(GameProposal<ScribbleSettings> proposal, Collection<CriterionViolation> violations, boolean blacklisted) {
        this.proposal = proposal;
        this.violations = violations;
        this.blacklisted = blacklisted;
    }

    public boolean isBlacklisted() {
        return blacklisted;
    }

    public GameProposal<ScribbleSettings> getProposal() {
        return proposal;
    }

    public Collection<CriterionViolation> getViolations() {
        return violations;
    }


}
