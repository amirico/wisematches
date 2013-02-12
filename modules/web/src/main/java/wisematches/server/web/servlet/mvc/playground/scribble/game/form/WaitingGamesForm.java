package wisematches.server.web.servlet.mvc.playground.scribble.game.form;

import wisematches.playground.propose.CriterionViolation;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WaitingGamesForm {
	private final CriterionViolation globalViolation;
	private final Collection<GameProposalForm> proposalViews;

	public WaitingGamesForm(CriterionViolation globalViolation, Collection<GameProposalForm> proposalViews) {
		this.globalViolation = globalViolation;
		this.proposalViews = proposalViews;
	}

	public CriterionViolation getGlobalViolation() {
		return globalViolation;
	}

	public Collection<GameProposalForm> getProposalViews() {
		return proposalViews;
	}
}
