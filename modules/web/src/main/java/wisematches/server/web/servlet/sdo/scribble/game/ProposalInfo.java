package wisematches.server.web.servlet.sdo.scribble.game;

import wisematches.playground.propose.CriterionViolation;
import wisematches.playground.propose.GameProposal;
import wisematches.playground.propose.ProposalType;
import wisematches.playground.scribble.ScribbleSettings;
import wisematches.server.web.servlet.sdo.person.PersonalityInfo;
import wisematches.server.web.servlet.sdo.scribble.SettingsInfo;
import wisematches.server.web.servlet.sdo.time.TimeInfo;

import java.util.Collection;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ProposalInfo {
	private final GameProposal<ScribbleSettings> proposal;
	private final Collection<CriterionViolation> violations;

	public ProposalInfo(GameProposal<ScribbleSettings> proposal, Collection<CriterionViolation> violations) {
		this.proposal = proposal;
		this.violations = violations;
	}

	public long getId() {
		return proposal.getId();
	}

	public SettingsInfo getSettings() {
		throw new UnsupportedOperationException("Not implemented");
	}

	public TimeInfo getCreationDate() {
		throw new UnsupportedOperationException("Not implemented");
	}

	public long getInitiator() {
		throw new UnsupportedOperationException("Not implemented");
	}

	public List<PersonalityInfo> getPlayers() {
		throw new UnsupportedOperationException("Not implemented");
	}

	public List<Long> getJoinedPlayers() {
		throw new UnsupportedOperationException("Not implemented");
	}

	public boolean isReady() {
		return proposal.isReady();
	}

	public ProposalType getProposalType() {
		return proposal.getProposalType();
	}

	public Collection<CriterionViolation> getViolations() {
		return violations;
	}
}
