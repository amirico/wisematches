package wisematches.server.gameplaying.scribble.proposal;

import wisematches.server.gameplaying.propose.impl.AbstractProposalManager;
import wisematches.server.gameplaying.scribble.board.ScribbleSettings;
import wisematches.server.personality.Personality;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleProposalManager extends AbstractProposalManager<ScribbleSettings, ScribbleProposal> {
/*
	private final AtomicLong proposalIds = new AtomicLong();

	private final Map<Long, ScribbleProposal> proposals = new ConcurrentHashMap<Long, ScribbleProposal>();

	public ScribbleProposalManager() {
	}

	@Override
	public synchronized ScribbleProposal initiateGameProposal(ScribbleProposal proposal) {
		long id = proposalIds.incrementAndGet();
		proposal.setId(id);
		proposals.put(id, proposal);
		fireGameProposalInitiated(proposal);
		return proposal;
	}

	@Override
	public synchronized ScribbleProposal attachPlayer(long proposalId, Personality player) {
		final ScribbleProposal info = proposals.get(proposalId);
		if (info == null) {
			return null;
		}
		final String unsuitableMessage = info.getUnsuitableMessage(player);
		if (unsuitableMessage != null) {
			throw new IllegalStateException(unsuitableMessage);
		}
		info.attachPlayer(player);
		fireGameProposalUpdated(info);
		if (info.isGameReady()) { // if game is ready - resign it.
			cancelGameProposal(info);
		}
		return info;
	}

	@Override
	public synchronized ScribbleProposal detachPlayer(long proposalId, Personality player) {
		final ScribbleProposal info = proposals.get(proposalId);
		if (info == null) {
			throw new UnsupportedOperationException("Checking is not implemented");
		}
		info.detachPlayer(player);
		fireGameProposalUpdated(info);
		return info;
	}

	@Override
	public synchronized void cancelGameProposal(ScribbleProposal proposal) {
		proposals.remove(proposal.getId());
		fireGameProposalClosed(proposal);
	}

	@Override
	public synchronized Collection<ScribbleProposal> getActiveProposals() {
		return proposals.values();
	}
*/

	@Override
	public ScribbleProposal initiateGameProposal(ScribbleProposal proposal) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public ScribbleProposal attachPlayer(long proposalId, Personality player) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public ScribbleProposal detachPlayer(long proposalId, Personality player) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void cancelGameProposal(ScribbleProposal proposal) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Collection<ScribbleProposal> getActiveProposals() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}
}
