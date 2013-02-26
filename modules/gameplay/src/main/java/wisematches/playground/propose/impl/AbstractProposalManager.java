package wisematches.playground.propose.impl;

import org.springframework.beans.factory.InitializingBean;
import wisematches.core.Personality;
import wisematches.core.Player;
import wisematches.core.search.Orders;
import wisematches.core.search.Range;
import wisematches.playground.GameSettings;
import wisematches.playground.propose.*;
import wisematches.playground.propose.criteria.ViolatedCriteriaException;
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
@SuppressWarnings("unchecked")
public abstract class AbstractProposalManager<S extends GameSettings> implements GameProposalManager<S>, InitializingBean {
	private StatisticManager statisticManager;

	private final Lock lock = new ReentrantLock();
	private final AtomicLong proposalIds = new AtomicLong();

	private final Map<Long, AbstractGameProposal<S>> proposals = new ConcurrentHashMap<>();
	private final Collection<GameProposalListener> proposalListeners = new CopyOnWriteArraySet<>();

	protected AbstractProposalManager() {
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		lock.lock();
		try {
			long max = 0;
			final Collection<AbstractGameProposal<S>> gameProposals = loadGameProposals();
			for (AbstractGameProposal<S> proposal : gameProposals) {
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
	public PrivateProposal<S> initiate(S settings, String commentary, Player initiator, Collection<Player> opponents) {
		final long id = proposalIds.incrementAndGet();
		return registerProposal(new DefaultPrivateProposal<>(id, commentary, settings, initiator, opponents));
	}

	@Override
	public PublicProposal<S> initiate(S settings, Player initiator, int opponentsCount, PlayerCriterion... criteria) {
		final long id = proposalIds.incrementAndGet();
		return registerProposal(new DefaultPublicProposal<>(id, settings, initiator, opponentsCount, Arrays.asList(criteria)));
	}

	@Override
	public GameProposal<S> accept(long proposalId, Player player) throws ViolatedCriteriaException {
		lock.lock();
		try {
			final AbstractGameProposal<S> proposal = proposals.get(proposalId);
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
			final AbstractGameProposal<S> proposal = proposals.get(proposalId);
			if (proposal == null || !proposal.getPlayers().contains(player)) {
				return null;
			}

			if (proposal.getInitiator().equals(player)) {
				proposals.remove(proposalId);
				removeGameProposal(proposal);
				fireGameProposalFinalized(proposal, player, ProposalResolution.REPUDIATED);
			} else {
				if (proposal.getProposalType() == ProposalType.PRIVATE) {
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
			final AbstractGameProposal<S> proposal = proposals.get(proposalId);
			if (proposal == null) {
				return null;
			}
			proposals.remove(proposalId);
			removeGameProposal(proposal);

			final List<Player> players = new ArrayList<>(proposal.getPlayers());
			players.removeAll(proposal.getJoinedPlayers());

			Player terminated = null;
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
		final AbstractGameProposal<S> proposal = proposals.get(proposalId);
		if (proposal == null) {
			return null;
		}
		if (proposal.getProposalType() == ProposalType.PRIVATE) {
			if (!proposal.validatePlayer(player)) {
				return Collections.singleton(new CriterionViolation("player.unexpected", player, ""));
			}
			return null;
		}
		if (proposal instanceof PublicProposal) {
			return ((PublicProposal) proposal).checkViolations(player, statisticManager.getStatistic(player));
		}
		return null;
	}


	@Override
	public <Ctx extends ProposalRelation> int getTotalCount(Personality person, Ctx context) {
		lock.lock();
		try {
			int res = 0;
			if (context == ProposalRelation.AVAILABLE) {
				res = proposals.size();
			} else if (context == ProposalRelation.INVOLVED) {
				for (AbstractGameProposal<S> proposal : proposals.values()) {
					if (person instanceof Player && proposal.containsPlayer((Player) person)) {
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
	public <Ctx extends ProposalRelation> List<GameProposal<S>> searchEntities(Personality person, Ctx context, Orders orders, Range range) {
		lock.lock();
		try {
			if (context == ProposalRelation.AVAILABLE) {
				return new ArrayList<GameProposal<S>>(proposals.values());
			} else if (context == ProposalRelation.INVOLVED) {
				final List<GameProposal<S>> res = new ArrayList<>();
				for (AbstractGameProposal<S> proposal : proposals.values()) {
					if (person instanceof Player && proposal.containsPlayer((Player) person)) {
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


	private <T extends AbstractGameProposal<S>> T registerProposal(T proposal) {
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

	protected abstract Collection<AbstractGameProposal<S>> loadGameProposals();

	protected abstract void storeGameProposal(AbstractGameProposal<S> proposal);

	protected abstract void removeGameProposal(AbstractGameProposal<S> proposal);


	public void setPlayerStatisticManager(StatisticManager statisticManager) {
		this.statisticManager = statisticManager;
	}

	protected void fireGameProposalInitiated(GameProposal<S> proposal) {
		for (GameProposalListener proposalListener : proposalListeners) {
			proposalListener.gameProposalInitiated(proposal);
		}
	}

	protected void fireGameProposalUpdated(GameProposal<S> proposal, Player player, ProposalDirective directive) {
		for (GameProposalListener proposalListener : proposalListeners) {
			proposalListener.gameProposalUpdated(proposal, player, directive);
		}
	}

	protected void fireGameProposalFinalized(GameProposal<S> proposal, Player player, ProposalResolution reason) {
		for (GameProposalListener proposalListener : proposalListeners) {
			proposalListener.gameProposalFinalized(proposal, player, reason);
		}
	}
}