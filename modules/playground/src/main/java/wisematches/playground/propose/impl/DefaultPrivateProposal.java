package wisematches.playground.propose.impl;

import wisematches.core.Player;
import wisematches.playground.GameSettings;
import wisematches.playground.propose.PrivateProposal;
import wisematches.playground.propose.ProposalType;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultPrivateProposal<S extends GameSettings> extends AbstractGameProposal<S> implements PrivateProposal<S> {
	private final String commentary;
	private final List<Player> players;
	private final Set<Long> joinedPlayers = new HashSet<>();

	public DefaultPrivateProposal(long id, String commentary, S settings, Player initiator, Collection<Player> opponents) {
		super(id, settings, initiator);
		this.players = new ArrayList<>();
		this.players.add(initiator);
		this.players.addAll(opponents);

		this.commentary = commentary;
		this.joinedPlayers.add(initiator.getId());
	}

	@Override
	public String getCommentary() {
		return commentary;
	}

	@Override
	public List<Player> getPlayers() {
		return players;
	}

	@Override
	protected void attach(Player player) {
		if (player == null) {
			throw new NullPointerException("Player can't be null");
		}
		if (!players.contains(player)) {
			throw new IllegalArgumentException("Player is not waiting");
		}
		if (joinedPlayers.contains(player.getId())) {
			throw new IllegalArgumentException("Player is joined");
		}

		joinedPlayers.add(player.getId());
	}

	@Override
	protected void detach(Player player) {
		if (player == null) {
			throw new NullPointerException("Player can't be null");
		}
		if (initiator.equals(player)) {
			throw new IllegalArgumentException("Initiator can't be removed");
		}
		if (!players.contains(player)) {
			throw new IllegalArgumentException("Player is not waiting");
		}
		if (!joinedPlayers.contains(player.getId())) {
			throw new IllegalArgumentException("Player is not joined");
		}

		joinedPlayers.remove(player.getId());
	}

	@Override
	public List<Player> getJoinedPlayers() {
		final List<Player> res = new ArrayList<>(players.size());
		for (Player player : players) {
			if (isPlayerJoined(player)) {
				res.add(player);
			}
		}
		return res;
	}

	@Override
	public boolean isPlayerJoined(Player player) {
		return joinedPlayers.contains(player.getId());
	}

	@Override
	public boolean containsPlayer(Player player) {
		return players.contains(player);
	}

	@Override
	public boolean validatePlayer(Player player) {
		return player != null && players.contains(player) && !joinedPlayers.contains(player.getId());
	}

	@Override
	public boolean isReady() {
		return players.size() == joinedPlayers.size();
	}

	@Override
	public ProposalType getProposalType() {
		return ProposalType.PRIVATE;
	}
}
