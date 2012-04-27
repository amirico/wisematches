package wisematches.playground.propose.impl;

import wisematches.personality.Membership;
import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.personality.player.computer.ComputerPlayer;
import wisematches.personality.player.computer.robot.RobotPlayer;
import wisematches.playground.GameSettings;
import wisematches.playground.criteria.CriterionViolation;
import wisematches.playground.criteria.PlayerCriterion;
import wisematches.playground.criteria.ViolatedCriteriaException;
import wisematches.playground.propose.GameProposal;
import wisematches.playground.propose.ProposalType;
import wisematches.playground.tracking.Statistics;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultGameProposal<S extends GameSettings> implements GameProposal<S>, Serializable {
	private final long id;
	private final S gameSettings;
	private final Date creationDate;
	private final String commentary;
	private final ProposalType proposalType;
	private final Personality initiator;
	private final Personality[] players;
	private final PlayerCriterion[] criteria;

	private int joinedPlayersCount;
	private transient List<Personality> playersWrapper;

	private static final long serialVersionUID = -7715597943833759768L;

	public DefaultGameProposal(long id, S gameSettings, Personality initiator, Personality[] opponents) {
		this(id, null, gameSettings, initiator, opponents, null);
	}

	public DefaultGameProposal(long id, S gameSettings, Personality initiator, Personality[] opponents, PlayerCriterion[] criteria) {
		this(id, null, gameSettings, initiator, opponents, criteria);
	}

	public DefaultGameProposal(long id, String commentary, S gameSettings, Personality initiator, Personality[] opponents) {
		this(id, commentary, gameSettings, initiator, opponents, null);
	}

	public DefaultGameProposal(long id, String commentary, S gameSettings, Personality initiator, Personality[] opponents, PlayerCriterion[] criteria) {
		if (gameSettings == null) {
			throw new NullPointerException("Game settings can't be null");
		}
		if (initiator == null) {
			throw new NullPointerException("Initiator can't be null");
		}
		if (opponents == null) {
			throw new NullPointerException("Opponents can't be null");
		}
		if (opponents.length + 1 < gameSettings.getMinPlayers() || opponents.length + 1 > gameSettings.getMaxPlayers()) {
			throw new IllegalArgumentException("Wrong players count: " + opponents.length + ". Must be " +
					gameSettings.getMinPlayers() + " <= X <= " + gameSettings.getMaxPlayers());
		}

		this.id = id;
		this.gameSettings = gameSettings;
		this.creationDate = new Date();
		this.commentary = commentary;
		this.initiator = Personality.untie(initiator);
		if (criteria != null) {
			this.criteria = criteria.clone();
		} else {
			this.criteria = null;
		}

		players = new Personality[opponents.length + 1];
		playersWrapper = Arrays.asList(players);

		int robots = 0;
		int waits = 0;
		int challenges = 0;
		players[0] = this.initiator;
		for (int i = 0, opponentsLength = opponents.length; i < opponentsLength; i++) {
			final Personality opponent = opponents[i];
			if (opponent != null && playersWrapper.contains(opponent)) {
				throw new IllegalArgumentException("Player already in the list");
			}
			players[i + 1] = opponent;

			if (opponent == null) {
				waits++;
			} else if (RobotPlayer.isRobotPlayer(opponent)) {
				robots++;
			} else {
				challenges++;
			}
		}
		joinedPlayersCount = 1;

		if (robots + waits == opponents.length) {
			proposalType = ProposalType.INTENTION;
		} else if (robots + challenges == opponents.length) {
			proposalType = ProposalType.CHALLENGE;
		} else {
			throw new IllegalArgumentException("Wrong players. Proposal can't have mix of challenged players and waiting players.");
		}

		// Join robots
		for (int i = 0, opponentsLength = opponents.length; i < opponentsLength; i++) {
			if (opponents[i] != null) {
				final ComputerPlayer cp = RobotPlayer.getComputerPlayer(opponents[i].getId());
				if (cp != null && cp.getMembership() == Membership.ROBOT) {
					attachImpl(cp);
				}
			}
		}
	}

	@Override
	public final long getId() {
		return id;
	}

	@Override
	public final S getGameSettings() {
		return gameSettings;
	}

	@Override
	public final Date getCreationDate() {
		return creationDate;
	}

	@Override
	public final String getCommentary() {
		return commentary;
	}

	@Override
	public ProposalType getProposalType() {
		return proposalType;
	}

	@Override
	public final Personality getInitiator() {
		return initiator;
	}

	@Override
	public int getPlayersCount() {
		return players.length;
	}

	@Override
	public List<Personality> getPlayers() {
		return Arrays.asList(players);
	}

	@Override
	public int getJoinedPlayersCount() {
		return joinedPlayersCount;
	}

	@Override
	public List<Personality> getJoinedPlayers() {
		return Collections.unmodifiableList(playersWrapper.subList(0, joinedPlayersCount));
	}

	@Override
	public boolean isPlayerWaiting(Personality player) {
		return playersWrapper.indexOf(player) >= joinedPlayersCount;
	}

	@Override
	public boolean isPlayerJoined(Personality player) {
		final int i = playersWrapper.indexOf(player);
		return i >= 0 && i < joinedPlayersCount;
	}

	@Override
	public final boolean isReady() {
		return getPlayersCount() == getJoinedPlayersCount();
	}

	void attach(Player player) throws ViolatedCriteriaException {
		CriterionViolation criterionViolation = checkPlayer(player);
		if (criterionViolation != null) {
			throw new ViolatedCriteriaException(criterionViolation);
		}
		attachImpl(player);
	}

	void detach(Player player) throws ViolatedCriteriaException {
		if (initiator.equals(player)) {
			throw new ViolatedCriteriaException(new CriterionViolation("player.initiator", player));
		}
		detachImpl(player);
	}

	/**
	 * Checks that the player can join the proposal.
	 *
	 * @param player the player to be checked.
	 * @return {@code "player.joined"} if player already joined or {@code player.unexpected} if there is no free
	 *         place and the player not in the waiting list.
	 */
	protected final CriterionViolation checkPlayer(Player player) {
		if (player == null) {
			throw new NullPointerException("Player can't be joined");
		}
		if (isPlayerJoined(player)) {
			return new CriterionViolation("player.joined", player);
		}

		int i = playersWrapper.indexOf(player);
		if (i == -1) {
			for (Personality personality : players) {
				if (personality == null) {
					return null;
				}
			}
			return new CriterionViolation("player.unexpected", player);
		}
		return null;
	}

	protected final Collection<CriterionViolation> checkViolation(Player player, Statistics statistics) {
		Collection<CriterionViolation> res = null;

		CriterionViolation c = checkPlayer(player);
		if (c != null) {
			res = new ArrayList<CriterionViolation>();
			res.add(c);
		}

		if (criteria != null) {
			for (PlayerCriterion criterion : criteria) {
				CriterionViolation v = criterion.checkViolation(player, statistics);
				if (v != null) {
					if (res == null) {
						res = new ArrayList<CriterionViolation>();
					}
					res.add(v);
				}
			}
		}
		return res;
	}


	private void attachImpl(Player player) {
		int index = playersWrapper.indexOf(player);
		if (index == -1) {
			final int playersSize = players.length;
			for (index = joinedPlayersCount; index < playersSize; index++) {
				if (players[index] == null) {
					players[index] = Personality.untie(player);
					break;
				}
			}
		}

		if (index != players.length) {
			players[index] = players[joinedPlayersCount];
			players[joinedPlayersCount++] = Personality.untie(player);
		} else {
			throw new IllegalStateException("Wrong logic");
		}
	}

	private void detachImpl(Player player) {
		int index = playersWrapper.indexOf(player);
		if (index == -1) {
			return;
		}

		System.arraycopy(players, index + 1, players, index, players.length - 1 - index);
		players[players.length - 1] = null;
		joinedPlayersCount--;
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();

		playersWrapper = Arrays.asList(players);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("DefaultGameProposal");
		sb.append("{id=").append(id);
		sb.append(", gameSettings=").append(gameSettings);
		sb.append(", creationDate=").append(creationDate);
		sb.append(", commentary='").append(commentary).append('\'');
		sb.append(", initiator=").append(initiator);
		sb.append(", players=").append(players == null ? "null" : Arrays.asList(players).toString());
		sb.append(", criteria=").append(criteria == null ? "null" : Arrays.asList(criteria).toString());
		sb.append(", joinedPlayersCount=").append(joinedPlayersCount);
		sb.append('}');
		return sb.toString();
	}
}