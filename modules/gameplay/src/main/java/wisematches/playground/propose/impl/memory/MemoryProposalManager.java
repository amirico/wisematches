package wisematches.playground.propose.impl.memory;

import wisematches.playground.GameSettings;
import wisematches.playground.propose.impl.AbstractGameProposal;
import wisematches.playground.propose.impl.AbstractProposalManager;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MemoryProposalManager<S extends GameSettings> extends AbstractProposalManager<S> {
	private final Collection<AbstractGameProposal<S>> proposals = new ArrayList<>();

	public MemoryProposalManager() {
	}

	@Override
	protected Collection<AbstractGameProposal<S>> loadGameProposals() {
		return proposals;
	}

	@Override
	protected void storeGameProposal(AbstractGameProposal<S> proposal) {
		proposals.add(proposal);
	}

	@Override
	protected void removeGameProposal(AbstractGameProposal<S> proposal) {
		proposals.remove(proposal);
	}
}
