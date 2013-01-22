package wisematches.server.web.controllers.playground.scribble.view;

import wisematches.playground.propose.criteria.CriterionViolation;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WaitingGamesView {
	private final CriterionViolation globalViolation;
	private final Collection<GameProposalView> proposalViews;

	public WaitingGamesView(CriterionViolation globalViolation, Collection<GameProposalView> proposalViews) {
		this.globalViolation = globalViolation;
		this.proposalViews = proposalViews;
	}

	public CriterionViolation getGlobalViolation() {
		return globalViolation;
	}

	public Collection<GameProposalView> getProposalViews() {
		return proposalViews;
	}
}
