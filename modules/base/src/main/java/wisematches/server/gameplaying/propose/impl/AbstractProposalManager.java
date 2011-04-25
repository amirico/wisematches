package wisematches.server.gameplaying.propose.impl;

import org.springframework.beans.factory.InitializingBean;
import wisematches.server.gameplaying.board.GameSettings;
import wisematches.server.gameplaying.propose.GameProposal;
import wisematches.server.gameplaying.propose.GameProposalListener;
import wisematches.server.gameplaying.propose.GameProposalManager;
import wisematches.server.personality.Personality;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class AbstractProposalManager<S extends GameSettings> implements GameProposalManager<S>, InitializingBean {
	private final Lock lock = new ReentrantLock();

	private final AtomicLong proposalIds = new AtomicLong();
	private final Map<Long, GameProposal<S>> proposals = new ConcurrentHashMap<Long, GameProposal<S>>();

	private final Collection<GameProposalListener> proposalListeners = new CopyOnWriteArraySet<GameProposalListener>();

	protected AbstractProposalManager() {
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		long max = 0;
		final Collection<GameProposal<S>> gameProposals = loadGameProposals();
		for (GameProposal<S> proposal : gameProposals) {
			max = Math.max(max, proposal.getId());
			proposals.put(proposal.getId(), proposal);
		}
	}

	@Override
	public void addGameProposalListener(GameProposalListener l) {
		proposalListeners.add(l);
	}

	@Override
	public void removeGameProposalListener(GameProposalListener l) {
		if (l != null) {
			proposalListeners.remove(l);
		}
	}

	@Override
	public GameProposal<S> initiateGameProposal(S settings, int playersCount, Collection<Personality> players) {
		lock.lock();
		try {
			final DefaultGameProposal<S> proposal = new DefaultGameProposal<S>(proposalIds.incrementAndGet(), settings, playersCount, players);
			proposals.put(proposal.getId(), proposal);
			storeGameProposal(proposal);
			fireGameProposalInitiated(proposal);
			return proposal;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public GameProposal<S> attachPlayer(long proposalId, Personality player) {
		lock.lock();
		try {
			final DefaultGameProposal<S> proposal = (DefaultGameProposal<S>) proposals.get(proposalId);
			if (proposal == null) {
				return null;
			}
			proposal.attachPlayer(player);
			if (proposal.getPlayersCount() == proposal.getPlayers().size()) {
				proposals.remove(proposalId);
				removeGameProposal(proposal);
				fireGameProposalClosed(proposal);
			} else {
				storeGameProposal(proposal);
				fireGameProposalUpdated(proposal);
			}
			return proposal;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public GameProposal<S> detachPlayer(long proposalId, Personality player) {
		lock.lock();
		try {
			final DefaultGameProposal<S> proposal = (DefaultGameProposal<S>) proposals.get(proposalId);
			if (proposal == null) {
				return null;
			}
			proposal.detachPlayer(player);
			storeGameProposal(proposal);
			fireGameProposalUpdated(proposal);
			return proposal;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public Collection<GameProposal<S>> getActiveProposals() {
		lock.lock();
		try {
			return Collections.unmodifiableCollection(proposals.values());
		} finally {
			lock.unlock();
		}
	}

	@Override
	public Collection<GameProposal<S>> getPlayerProposals(Personality player) {
		lock.lock();
		try {
			final Collection<GameProposal<S>> res = new ArrayList<GameProposal<S>>();
			for (GameProposal<S> scribbleProposal : getActiveProposals()) {
				if (scribbleProposal.containsPlayer(player)) {
					res.add(scribbleProposal);
				}
			}
			return res;
		} finally {
			lock.unlock();
		}
	}

	protected abstract Collection<GameProposal<S>> loadGameProposals();

	protected abstract void storeGameProposal(GameProposal<S> proposal);

	protected abstract void removeGameProposal(GameProposal<S> proposal);

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
