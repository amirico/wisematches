package wisematches.server.gameplaying.propose.impl;

import wisematches.server.personality.Personality;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class OldGameProposal implements Serializable {
	private long id;
	private String title;
	private int timeLimits;
	private long gameOwner;
	private int opponentsCount;
	private List<Long> acceptedPlayers = new ArrayList<Long>(2);

	private static final long serialVersionUID = 5270630804990980276L;

	protected OldGameProposal() {
	}

	protected OldGameProposal(String title, int timeLimits, int opponentsCount, Personality player, List<? extends Personality> opponents) {
		this.title = title;
		this.timeLimits = timeLimits;
		this.opponentsCount = opponentsCount;
		this.gameOwner = player.getId();

		acceptedPlayers.add(gameOwner);
		if (opponents != null) {
			for (Personality opponent : opponents) {
				if (opponent != null) {
					acceptedPlayers.add(opponent.getId());
				}
			}
		}
	}

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public long getGameOwner() {
		return gameOwner;
	}

	public int getTimeLimits() {
		return timeLimits;
	}

	public int getOpponentsCount() {
		return opponentsCount;
	}

	public boolean isSuitablePlayer(Personality p) {
		return getUnsuitableMessage(p) == null;
	}

	public String getUnsuitableMessage(Personality p) {
		if (acceptedPlayers.contains(p.getId())) {
			return "exist";
		}
		if (opponentsCount + 1 == acceptedPlayers.size()) {
			return "full";
		}
		return null;
	}

	public List<Long> getRegisteredPlayers() {
		return Collections.unmodifiableList(acceptedPlayers);
	}

	public List<Long> getAllPlayers() {
		final int count = opponentsCount + 1;
		if (acceptedPlayers.size() == count) {
			return Collections.unmodifiableList(acceptedPlayers);
		}
		final List<Long> res = new ArrayList<Long>(opponentsCount);
		res.addAll(acceptedPlayers);
		for (int i = res.size(); i < count; i++) {
			res.add(null);
		}
		return res;
	}

	public boolean isGameReady() {
		return acceptedPlayers.size() == opponentsCount;
	}

	public void attachPlayer(Personality player) {
		acceptedPlayers.add(player.getId());
	}

	public void detachPlayer(Personality player) {
		acceptedPlayers.remove(player.getId());
	}

	public void setId(long id) {
		this.id = id;
	}
}
