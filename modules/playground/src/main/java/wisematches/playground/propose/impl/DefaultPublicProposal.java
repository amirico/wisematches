package wisematches.playground.propose.impl;

import wisematches.core.Player;
import wisematches.playground.GameSettings;
import wisematches.playground.propose.CriterionViolation;
import wisematches.playground.propose.PlayerCriterion;
import wisematches.playground.propose.ProposalType;
import wisematches.playground.propose.PublicProposal;
import wisematches.playground.tracking.Statistics;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultPublicProposal<S extends GameSettings> extends AbstractGameProposal<S> implements PublicProposal<S> {
	private final Player[] players;
	private final Collection<PlayerCriterion> criterion;

	public DefaultPublicProposal(long id, S settings, Player initiator, int opponentsCount, Collection<PlayerCriterion> criterion) {
		super(id, settings, initiator);
		this.criterion = criterion;

		this.players = new Player[opponentsCount + 1];
		this.players[0] = initiator;
	}

	@Override
	protected void attach(Player player) {
		if (player == null) {
			throw new NullPointerException("Player can't be null");
		}
		if (containsPlayer(player)) {
			throw new IllegalArgumentException("Player already registered");
		}
		for (int i = 0; i < players.length; i++) {
			if (players[i] == null) {
				players[i] = player;
				return;
			}
		}
		throw new IllegalArgumentException("Proposal is ready");
	}

	@Override
	protected void detach(Player player) {
		if (player == null) {
			throw new NullPointerException("Player can't be null");
		}
		if (initiator.equals(player)) {
			throw new IllegalArgumentException("Initiator can't be removed");
		}
		if (!containsPlayer(player)) {
			throw new IllegalArgumentException("Unknown player");
		}
		boolean move = false;
		for (int i = 0; i < players.length - 1; i++) {
			if (!move && player.equals(players[i])) {
				move = true;
			}

			if (move) {
				players[i] = players[i + 1];
			}
		}
		players[players.length - 1] = null;
	}

	@Override
	public List<Player> getPlayers() {
		return Arrays.asList(players);
	}

	@Override
	public List<Player> getJoinedPlayers() {
		final List<Player> res = new ArrayList<>(players.length);
		for (Player player : players) {
			if (player != null) {
				res.add(player);
			}
		}
		return res;
	}


	@Override
	public Collection<PlayerCriterion> getPlayerCriterion() {
		return Collections.unmodifiableCollection(criterion);
	}

	@Override
	public boolean containsPlayer(Player player) {
		for (Player p : players) {
			if (player.equals(p)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean validatePlayer(Player player) {
		for (Player p : players) {
			if (player.equals(p)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Collection<CriterionViolation> checkViolations(Player player, Statistics statistics) {
		if (criterion == null || criterion.isEmpty()) {
			return null;
		}

		Collection<CriterionViolation> res = new ArrayList<>();
		for (PlayerCriterion c : criterion) {
			final CriterionViolation v = c.checkViolation(player, statistics);
			if (v != null) {
				res.add(v);
			}
		}
		return res.isEmpty() ? null : res;
	}

	@Override
	public boolean isReady() {
		for (Player player : getPlayers()) {
			if (player == null) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ProposalType getProposalType() {
		return ProposalType.PUBLIC;
	}
}
