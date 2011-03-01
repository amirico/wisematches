package wisematches.server.gameplaying.scribble.room.proposal;

import wisematches.server.gameplaying.room.propose.AbstractProposalManager;
import wisematches.server.player.Player;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleProposalManager extends AbstractProposalManager<ScribbleProposal> {
	private final Map<Long, ScribbleProposal> proposals = new ConcurrentHashMap<Long, ScribbleProposal>();

	public ScribbleProposalManager() {
	}

	@Override
	public synchronized void initiateGameProposal(ScribbleProposal proposal) {
		proposals.put(proposal.getId(), proposal);
		fireGameProposalInitiated(proposal);
	}

	@Override
	public synchronized ScribbleProposal attachPlayer(long proposalId, Player player) {
		final ScribbleProposal info = proposals.get(proposalId);
		if (info == null) {
			throw new UnsupportedOperationException("Checking is not implemented");
		}
		if (!info.isSuitablePlayer(player)) {
			throw new UnsupportedOperationException("Checking is not implemented");
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
