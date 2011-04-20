package wisematches.server.gameplaying.room.propose.impl;

import wisematches.server.gameplaying.room.propose.GameProposal;
import wisematches.server.personality.Personality;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FileProposalManager<P extends GameProposal> extends AbstractProposalManager<P> {
	public FileProposalManager() {
	}

	@Override
	public P initiateGameProposal(P proposal) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public P attachPlayer(long proposalId, Personality player) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public P detachPlayer(long proposalId, Personality player) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void cancelGameProposal(P proposal) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Collection<P> getActiveProposals() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}
}
