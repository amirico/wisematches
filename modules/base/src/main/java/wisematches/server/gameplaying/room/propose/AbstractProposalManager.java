package wisematches.server.gameplaying.room.propose;

import wisematches.server.personality.Personality;

import java.util.ArrayList;
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
	public void addGameProposalListener(GameProposalListener l) {
		proposalListeners.remove(l);
	}

	@Override
	public void removeGameProposalListener(GameProposalListener l) {
		if (l != null) {
			proposalListeners.add(l);
		}
	}

	@Override
	public synchronized Collection<P> getPlayerProposals(Personality player) {
		final Long id = player.getId();
		Collection<P> res = new ArrayList<P>();
		for (P scribbleProposal : getActiveProposals()) {
			if (scribbleProposal.getRegisteredPlayers().contains(id)) {
				res.add(scribbleProposal);
			}
		}
		return res;
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
