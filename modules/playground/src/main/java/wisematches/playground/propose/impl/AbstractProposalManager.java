package wisematches.playground.propose.impl;

import org.springframework.beans.factory.InitializingBean;
import wisematches.core.Personality;
import wisematches.core.personality.Player;
import wisematches.core.search.Orders;
import wisematches.core.search.Range;
import wisematches.core.search.SearchFilter;
import wisematches.playground.GameSettings;
import wisematches.playground.criteria.CriterionViolation;
import wisematches.playground.criteria.PlayerCriterion;
import wisematches.playground.criteria.ViolatedCriteriaException;
import wisematches.playground.propose.*;
import wisematches.playground.tracking.StatisticManager;

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
	private StatisticManager statisticManager;

	private final Lock lock = new ReentrantLock();
	private final AtomicLong proposalIds = new AtomicLong();

	private final Map<Long, DefaultGameProposal<S>> proposals = new ConcurrentHashMap<>();
	private final Collection<GameProposalListener> proposalListeners = new CopyOnWriteArraySet<>();

	protected AbstractProposalManager() {
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		lock.lock();
		try {
			long max = 0;
			final Collection<DefaultGameProposal<S>> gameProposals = loadGameProposals();
			for (DefaultGameProposal<S> proposal : gameProposals) {
				max = Math.max(max, proposal.getId());
				proposals.put(proposal.getId(), proposal);
			}
			proposalIds.set(max + 1);
		} finally {
			lock.unlock();
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
	public GameProposal<S> initiate(S settings, Player initiator, Collection<Player> opponents, String commentary) {
		return registerProposal(new DefaultGameProposal<>(proposalIds.incrementAndGet(), commentary, settings, initiator, opponents.toArray(new Personality[opponents.size()])));
	}

	@Override
	public GameProposal<S> initiate(S settings, Player initiator, int opponentsCount, PlayerCriterion... criteria) {
		return registerProposal(new DefaultGameProposal<>(proposalIds.incrementAndGet(), settings, initiator, new Personality[opponentsCount], criteria));
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
				fireGameProposalFinalized(proposal, null, ProposalResolution.READY);
			} else {
				storeGameProposal(proposal);
				fireGameProposalUpdated(proposal, player, ProposalDirective.ACCEPTED);
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
			if (proposal == null || !proposal.getPlayers().contains(player)) {
				return null;
			}

			if (proposal.getInitiator().equals(player)) {
				proposals.remove(proposalId);
				removeGameProposal(proposal);
				fireGameProposalFinalized(proposal, player, ProposalResolution.REPUDIATED);
			} else {
				if (proposal.getProposalType() == ProposalType.CHALLENGE) {
					proposals.remove(proposalId);
					removeGameProposal(proposal);
					fireGameProposalFinalized(proposal, player, ProposalResolution.REJECTED);
				} else {
					proposal.detach(player);
					storeGameProposal(proposal);
					fireGameProposalUpdated(proposal, player, ProposalDirective.REJECTED);
				}
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

			final List<Personality> players = new ArrayList<>(proposal.getPlayers());
			players.removeAll(proposal.getJoinedPlayers());

			Personality terminated = null;
			if (players.size() != 0) {
				terminated = players.get(0);
			}
			fireGameProposalFinalized(proposal, terminated, ProposalResolution.TERMINATED);
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
		if (proposal.getProposalType() == ProposalType.CHALLENGE) {
			if (!proposal.isPlayerWaiting(player)) {
				return Collections.singleton(new CriterionViolation("player.unexpected", player, ""));
			}
			return null;
		}
		return proposal.checkViolation(player, statisticManager.getPlayerStatistic(player));
	}


	@Override
	public <Ctx extends ProposalRelation> int getTotalCount(Personality person, Ctx context) {
		return getFilteredCount(person, context, null);
	}

	@Override
	public <Ctx extends ProposalRelation, Fl extends SearchFilter> int getFilteredCount(Personality person, Ctx context, Fl filter) {
		lock.lock();
		try {
			int res = 0;
			if (context == ProposalRelation.AVAILABLE) {
				res = proposals.size();
			} else if (context == ProposalRelation.INVOLVED) {
				for (DefaultGameProposal<S> proposal : proposals.values()) {
					if (proposal.isPlayerJoined(person)) {
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
	public <Ctx extends ProposalRelation, Fl extends SearchFilter> List<GameProposal<S>> searchEntities(Personality person, Ctx context, Fl filter, Orders orders, Range range) {
		lock.lock();
		try {
			if (context == ProposalRelation.AVAILABLE) {
				return new ArrayList<GameProposal<S>>(proposals.values());
			} else if (context == ProposalRelation.INVOLVED) {
				final List<GameProposal<S>> res = new ArrayList<>();
				for (DefaultGameProposal<S> proposal : proposals.values()) {
					if (proposal.isPlayerJoined(person)) {
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


	public void setPlayerStatisticManager(StatisticManager statisticManager) {
		this.statisticManager = statisticManager;
	}

	protected void fireGameProposalInitiated(GameProposal<S> proposal) {
		for (GameProposalListener proposalListener : proposalListeners) {
			proposalListener.gameProposalInitiated(proposal);
		}
	}

	protected void fireGameProposalUpdated(GameProposal<S> proposal, Personality player, ProposalDirective directive) {
		for (GameProposalListener proposalListener : proposalListeners) {
			proposalListener.gameProposalUpdated(proposal, player, directive);
		}
	}

	protected void fireGameProposalFinalized(GameProposal<S> proposal, Personality player, ProposalResolution reason) {
		for (GameProposalListener proposalListener : proposalListeners) {
			proposalListener.gameProposalFinalized(proposal, reason, player);
		}
	}
}