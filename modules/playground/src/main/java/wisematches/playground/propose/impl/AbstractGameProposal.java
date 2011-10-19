package wisematches.playground.propose.impl;

import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.playground.GameSettings;
import wisematches.playground.propose.GameProposal;
import wisematches.playground.propose.ViolatedRestrictionException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class AbstractGameProposal<S extends GameSettings> implements GameProposal<S>, Serializable {
	private final long id;
	private final S gameSettings;
	private final int playersCount;
	private final Player initiator;
	private final Collection<Personality> players;

	private static final long serialVersionUID = -7719928493587170213L;

	protected AbstractGameProposal(long id, S gameSettings, int playersCount, Player initiator) {
		if (id == 0) {
			throw new IllegalArgumentException("error.proposal.null.id");
		}
		if (gameSettings == null) {
			throw new NullPointerException("error.proposal.null.settings");
		}
		if (initiator == null) {
			throw new NullPointerException("error.proposal.null.initiator");
		}
		if (playersCount < 2) {
			throw new IllegalArgumentException("error.proposal.illegal.count");
		}

		this.id = id;
		this.initiator = initiator;
		this.gameSettings = gameSettings;
		this.playersCount = playersCount;

		this.players = new ArrayList<Personality>(playersCount);
		this.players.add(Personality.untie(initiator));
	}

	@Override
	public long getId() {
		return id;
	}

	public Player getInitiator() {
		return initiator;
	}

	@Override
	public S getGameSettings() {
		return gameSettings;
	}

	@Override
	public int getPlayersCount() {
		return playersCount;
	}

	@Override
	public Collection<Personality> getPlayers() {
		return Collections.unmodifiableCollection(players);
	}

	@Override
	public void isSuitablePlayer(Player player) throws ViolatedRestrictionException {
		if (player == null) {
			throw new ViolatedRestrictionException("player.null");
		}
		if (players.contains(player)) {
			throw new ViolatedRestrictionException("player.exist");
		}

		validateRestrictions(player);
	}

	@Override
	public boolean containsPlayer(Personality personality) {
		return players.contains(personality);
	}

	@Override
	public boolean isReady() {
		return players.size() == playersCount;
	}

	protected abstract void validateRestrictions(Player player) throws ViolatedRestrictionException;

	/**
	 * Attaches this player to this proposal.
	 *
	 * @param player the player to be attached
	 * @throws IllegalArgumentException	 if player is null
	 * @throws IllegalStateException		if proposal already full and new player can't be added.
	 * @throws ViolatedRestrictionException if player can't be attached because one or more restrictions are broken.
	 */
	void attachPlayer(Player player) throws ViolatedRestrictionException {
		if (player == null) {
			throw new ViolatedRestrictionException("player.null");
		}
		if (this.players.contains(player)) {
			throw new ViolatedRestrictionException("player.exist");
		}
		if (isReady()) {
			throw new ViolatedRestrictionException("ready");
		}
		validateRestrictions(player);
		players.add(Personality.untie(player));
	}

	/**
	 * Attaches this player to this proposal.
	 *
	 * @param player the player to be attached
	 * @throws IllegalArgumentException	 if player is null
	 * @throws ViolatedRestrictionException if player can't be detached because one or more restrictions are broken.
	 */
	void detachPlayer(Player player) throws ViolatedRestrictionException {
		if (player == null) {
			throw new ViolatedRestrictionException("player.null");
		}
		players.remove(player);
	}
}
