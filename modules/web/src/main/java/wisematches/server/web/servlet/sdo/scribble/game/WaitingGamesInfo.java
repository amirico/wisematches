package wisematches.server.web.servlet.sdo.scribble.game;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WaitingGamesInfo {
	private final Collection<ViolationInfo> globalViolations;
	private final Collection<ProposalInfo> proposalViews;

	public WaitingGamesInfo(Collection<ProposalInfo> proposalViews, Collection<ViolationInfo> globalViolations) {
		this.globalViolations = globalViolations;
		this.proposalViews = proposalViews;
	}

	public Collection<ProposalInfo> getProposalViews() {
		return proposalViews;
	}

	public Collection<ViolationInfo> getGlobalViolations() {
		return globalViolations;
	}
}
