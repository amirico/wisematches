package wisematches.playground.propose.impl;

import org.springframework.beans.factory.InitializingBean;
import wisematches.database.Orders;
import wisematches.database.Range;
import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.playground.GameSettings;
import wisematches.playground.criteria.CriterionViolation;
import wisematches.playground.criteria.PlayerCriterion;
import wisematches.playground.criteria.ViolatedCriteriaException;
import wisematches.playground.propose.*;
import wisematches.playground.search.SearchFilter;
import wisematches.playground.tracking.PlayerStatisticManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class AbstractProposalManager<S extends GameSettings> implements GameProposalManager<S>, InitializingBean {
	private PlayerStatisticManager statisticManager;

	private final Lock lock = new ReentrantLock();
	private final AtomicLong proposalIds = new AtomicLong();

	private final Map<Long, DefaultGameProposal<S>> proposals = new ConcurrentHashMap<Long, DefaultGameProposal<S>>();
	private final Collection<GameProposalListener> proposalListeners = new CopyOnWriteArraySet<GameProposalListener>();

	protected AbstractProposalManager() {
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		long max = 0;
		final Collection<DefaultGameProposal<S>> gameProposals = loadGameProposals();
		for (DefaultGameProposal<S> proposal : gameProposals) {
			max = Math.max(max, proposal.getId());
			proposals.put(proposal.getId(), proposal);
		}
		proposalIds.set(max + 1);
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
	public GameProposal<S> initiate(S settings, Player initiator, Collection<Player> opponents, String commentary) {
		return registerProposal(new DefaultGameProposal<S>(proposalIds.incrementAndGet(), commentary, settings, initiator, opponents.toArray(new Personality[opponents.size()])));
	}

	@Override
	public GameProposal<S> initiate(S settings, Player initiator, int expectedOpponentsCount, PlayerCriterion... criteria) {
		return registerProposal(new DefaultGameProposal<S>(proposalIds.incrementAndGet(), settings, initiator, new Personality[expectedOpponentsCount], criteria));
	}


	@Override
	public GameProposal<S> accept(long proposalId, Player player) throws ViolatedCriteriaException {
		lock.lock();
		try {
			final DefaultGameProposal<S> proposal = proposals.get(proposalId);
			if (proposal == null) {
				return null;
			}

			Collection<CriterionViolation> violations = validate(proposalId, player);
			if (violations != null) {
				throw new ViolatedCriteriaException(violations);
			}

			proposal.attach(player);
			if (proposal.isReady()) {
				proposals.remove(proposalId);
				removeGameProposal(proposal);
				fireGameProposalFinalized(proposal, ProposalResolution.READY);
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
	public GameProposal<S> reject(long proposalId, Player player) throws ViolatedCriteriaException {
		lock.lock();
		try {
			final DefaultGameProposal<S> proposal = proposals.get(proposalId);
			if (proposal == null) {
				return null;
			}

			if (proposal.getInitiator().equals(player)) {
				proposals.remove(proposalId);
				removeGameProposal(proposal);
				fireGameProposalFinalized(proposal, ProposalResolution.REPUDIATED);
			} else if (proposal.isPlayerWaiting(player)) {
				proposals.remove(proposalId);
				removeGameProposal(proposal);
				fireGameProposalFinalized(proposal, ProposalResolution.REJECTED);
			} else {
				proposal.detach(player);
				storeGameProposal(proposal);
				fireGameProposalUpdated(proposal);
			}
			return proposal;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public GameProposal<S> terminate(long proposalId) {
		lock.lock();
		try {
			final DefaultGameProposal<S> proposal = proposals.get(proposalId);
			if (proposal == null) {
				return null;
			}
			proposals.remove(proposalId);
			removeGameProposal(proposal);
			fireGameProposalFinalized(proposal, ProposalResolution.TERMINATED);
			return proposal;
		} finally {
			lock.unlock();
		}
	}


	@Override
	public GameProposal<S> getProposal(long proposalId) {
		lock.lock();
		try {
			return proposals.get(proposalId);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public Collection<CriterionViolation> validate(long proposalId, Player player) {
		final DefaultGameProposal<S> proposal = proposals.get(proposalId);
		if (proposal == null) {
			return null;
		}
		return proposal.checkViolation(player, statisticManager.getPlayerStatistic(player));
	}


	@Override
	public int getTotalCount(Personality person, ProposalRightholder context) {
		return getFilteredCount(person, context, null);
	}

	@Override
	public int getFilteredCount(Personality person, ProposalRightholder context, SearchFilter filter) {
		lock.lock();
		try {
			int res = 0;
			if (context == ProposalRightholder.ANY) {
				res = proposals.size();
			} else if (context == ProposalRightholder.ACTIVE) {
				for (DefaultGameProposal<S> proposal : proposals.values()) {
					if (proposal.isPlayerWaiting(person) || proposal.isPlayerJoined(person)) {
						res++;
					}
				}
			} else if (context == ProposalRightholder.INITIATED) {
				for (DefaultGameProposal<S> proposal : proposals.values()) {
					if (proposal.getInitiator().equals(person)) {
						res++;
					}
				}
			} else {
				throw new IllegalArgumentException("Proposal type is not supported: " + context);
			}
			return res;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public List<GameProposal<S>> searchEntities(Personality person, ProposalRightholder context, SearchFilter filter, Orders orders, Range range) {
		lock.lock();
		try {
			if (context == ProposalRightholder.ANY) {
				return new ArrayList<GameProposal<S>>(proposals.values());
			} else if (context == ProposalRightholder.ACTIVE) {
				final List<GameProposal<S>> res = new ArrayList<GameProposal<S>>();
				for (DefaultGameProposal<S> proposal : proposals.values()) {
					if (proposal.isPlayerWaiting(person) || proposal.isPlayerJoined(person)) {
						res.add(proposal);
					}
				}
				return res;
			} else if (context == ProposalRightholder.INITIATED) {
				final List<GameProposal<S>> res = new ArrayList<GameProposal<S>>();
				for (DefaultGameProposal<S> proposal : proposals.values()) {
					if (proposal.getInitiator().equals(person)) {
						res.add(proposal);
					}
				}
				return res;
			} else {
				throw new IllegalArgumentException("Proposal type is not supported: " + context);
			}
		} finally {
			lock.unlock();
		}
	}


	private DefaultGameProposal<S> registerProposal(DefaultGameProposal<S> proposal) {
		lock.lock();
		try {
			proposals.put(proposal.getId(), proposal);
			storeGameProposal(proposal);
			fireGameProposalInitiated(proposal);
			return proposal;
		} finally {
			lock.unlock();
		}
	}

	protected abstract Collection<DefaultGameProposal<S>> loadGameProposals();

	protected abstract void storeGameProposal(DefaultGameProposal<S> proposal);

	protected abstract void removeGameProposal(DefaultGameProposal<S> proposal);


	protected void fireGameProposalInitiated(GameProposal<S> proposal) {
		for (GameProposalListener proposalListener : proposalListeners) {
			proposalListener.gameProposalInitiated(proposal);
		}
	}

	protected void fireGameProposalUpdated(GameProposal<S> proposal) {
		for (GameProposalListener proposalListener : proposalListeners) {
			proposalListener.gameProposalUpdated(proposal);
		}
	}

	protected void fireGameProposalFinalized(GameProposal<S> proposal, ProposalResolution reason) {
		for (GameProposalListener proposalListener : proposalListeners) {
			proposalListener.gameProposalFinalized(proposal, reason);
		}
	}


	public void setPlayerStatisticManager(PlayerStatisticManager statisticManager) {
		this.statisticManager = statisticManager;
	}
}
