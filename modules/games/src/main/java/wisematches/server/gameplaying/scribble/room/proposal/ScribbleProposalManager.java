package wisematches.server.gameplaying.scribble.room.proposal;

import wisematches.server.gameplaying.room.propose.AbstractProposalManager;
import wisematches.server.player.Player;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleProposalManager extends AbstractProposalManager<ScribbleProposal> {
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
	public synchronized ScribbleProposal attachPlayer(long proposalId, Player player) {
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
		if (info.isGameReady()) { // if game is ready - close it.
			closeGameProposal(info);
		}
		return info;
	}

	@Override
	public synchronized ScribbleProposal detachPlayer(long proposalId, Player player) {
		final ScribbleProposal info = proposals.get(proposalId);
		if (info == null) {
			throw new UnsupportedOperationException("Checking is not implemented");
		}
		info.detachPlayer(player);
		fireGameProposalUpdated(info);
		return info;
	}

	@Override
	public synchronized void closeGameProposal(ScribbleProposal proposal) {
		proposals.remove(proposal.getId());
		fireGameProposalClosed(proposal);
	}

	@Override
	public synchronized Collection<ScribbleProposal> getActiveProposals() {
		return proposals.values();
	}
}
