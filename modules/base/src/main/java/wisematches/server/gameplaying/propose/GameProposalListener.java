package wisematches.server.gameplaying.propose;

import wisematches.server.gameplaying.propose.impl.OldGameProposal;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GameProposalListener {
	void gameProposalInitiated(OldGameProposal proposal);

	void gameProposalUpdated(OldGameProposal proposal);

	void gameProposalClosed(OldGameProposal proposal);

	void gameProposalCanceled(OldGameProposal proposal);
}