package wisematches.server.web.servlet.sdo.scribble.game;

import wisematches.playground.propose.CriterionViolation;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WaitingGamesInfo {
	private final Collection<CriterionViolation> globalViolations;
	private final Collection<ProposalInfo> proposalViews;

	public WaitingGamesInfo(Collection<ProposalInfo> proposalViews, Collection<CriterionViolation> globalViolations) {
		this.globalViolations = globalViolations;
		this.proposalViews = proposalViews;
	}

	public Collection<ProposalInfo> getProposalViews() {
		return proposalViews;
	}

	public Collection<CriterionViolation> getGlobalViolations() {
		return globalViolations;
	}
}
