package wisematches.server.gameplaying.propose.impl;

import wisematches.server.gameplaying.board.GameSettings;
import wisematches.server.gameplaying.propose.GameProposal;
import wisematches.server.personality.Personality;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class DefaultGameProposal<S extends GameSettings> implements GameProposal<S>, Serializable {
	private final long id;
	private final S gameSettings;
	private final int playersCount;
	private final Collection<Personality> players;

	/**
	 * Opens new waiting game in this manager.
	 *
	 * @param id		   id of this proposal. Can't be zero.
	 * @param gameSettings the original game settings. Can't be null.
	 * @param playersCount the max players count. Can't be less than 2.
	 * @param players	  current players count. List must contains at least one player (initiator). Size can't be more
	 *                     or equals than {@code playersCount}
	 * @throws IllegalArgumentException if {@code settings} or {@code players} are null or if {@code playersCount}
	 *                                  is less than two or if {@code players} collection size more than
	 *                                  {@code playersCount} or contains null.
	 */
	protected DefaultGameProposal(long id, S gameSettings, int playersCount, Collection<Personality> players) {
		if (id == 0) {
			throw new IllegalArgumentException("error.proposal.null.id");
		}
		if (gameSettings == null) {
			throw new IllegalArgumentException("error.proposal.null.settings");
		}
		if (players == null) {
			throw new IllegalArgumentException("error.proposal.null.players");
		}
		if (playersCount < 2) {
			throw new IllegalArgumentException("error.proposal.illegal.count");
		}
		if (players.size() < 1) {
			throw new IllegalArgumentException("error.proposal.notenough.players");
		}
		if (players.size() > playersCount) {
			throw new IllegalArgumentException("error.proposal.many.players");
		}

		this.id = id;
		this.gameSettings = gameSettings;
		this.playersCount = playersCount;

		this.players = new ArrayList<Personality>(playersCount);
		for (Personality player : players) {
			if (player == null) {
				throw new IllegalArgumentException("error.proposal.null.player");
			}
			if (this.players.contains(player)) {
				throw new IllegalArgumentException("error.proposal.twice.player");
			}
			this.players.add(Personality.untie(player));
		}
	}

	@Override
	public long getId() {
		return id;
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
	public boolean containsPlayer(Personality personality) {
		return players.contains(personality);
	}

	/**
	 * Attaches this player to this proposal.
	 *
	 * @param player the player to be attached
	 * @throws IllegalArgumentException if player is null
	 * @throws IllegalStateException	if proposal already full and new player can't be added.
	 */
	void attachPlayer(Personality player) {
		if (player == null) {
			throw new IllegalArgumentException("error.proposal.null.player");
		}
		if (this.players.contains(player)) {
			throw new IllegalArgumentException("error.proposal.twice.player");
		}
		if (players.size() == playersCount) {
			throw new IllegalStateException("error.proposal.ready");
		}
		players.add(Personality.untie(player));
	}

	/**
	 * Attaches this player to this proposal.
	 *
	 * @param player the player to be attached
	 * @throws IllegalArgumentException if player is null
	 */
	void detachPlayer(Personality player) {
		if (player == null) {
			throw new IllegalArgumentException("error.proposal.null.player");
		}
		players.remove(player);
	}
}
