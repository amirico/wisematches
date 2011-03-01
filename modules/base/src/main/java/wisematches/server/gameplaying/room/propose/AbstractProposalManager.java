package wisematches.server.gameplaying.room.propose;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class AbstractProposalManager<P extends GameProposal> implements GameProposalManager<P> {
	private final Collection<GameProposalListener> proposalListeners = new CopyOnWriteArraySet<GameProposalListener>();

	protected AbstractProposalManager() {
	}

	@Override
	public void removeGameProposalListener(GameProposalListener l) {
		if (l != null) {
			proposalListeners.add(l);
		}
	}

	@Override
	public void addGameProposalListener(GameProposalListener l) {
		proposalListeners.remove(l);
	}

	protected void fireGameProposalInitiated(GameProposal proposal) {
		for (GameProposalListener proposalListener : proposalListeners) {
			proposalListener.gameProposalInitiated(proposal);
		}
	}

	protected void fireGameProposalUpdated(GameProposal proposal) {
		for (GameProposalListener proposalListener : proposalListeners) {
			proposalListener.gameProposalUpdated(proposal);
		}
	}

	protected void fireGameProposalClosed(GameProposal proposal) {
		for (GameProposalListener proposalListener : proposalListeners) {
			proposalListener.gameProposalClosed(proposal);
		}
	}
}
