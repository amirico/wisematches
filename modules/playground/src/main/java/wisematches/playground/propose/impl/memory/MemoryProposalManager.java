package wisematches.playground.propose.impl.memory;

import wisematches.playground.GameSettings;
import wisematches.playground.propose.impl.AbstractProposalManager;
import wisematches.playground.propose.impl.DefaultGameProposal;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MemoryProposalManager<S extends GameSettings> extends AbstractProposalManager<S> {
	private final Collection<DefaultGameProposal<S>> proposals = new ArrayList<DefaultGameProposal<S>>();

	public MemoryProposalManager() {
	}

	@Override
	protected Collection<DefaultGameProposal<S>> loadGameProposals() {
		return proposals;
	}

	@Override
	protected void storeGameProposal(DefaultGameProposal<S> proposal) {
		proposals.add(proposal);
	}

	@Override
	protected void removeGameProposal(DefaultGameProposal<S> proposal) {
		proposals.remove(proposal);
	}
}
