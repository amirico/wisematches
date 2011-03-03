package wisematches.server.gameplaying.room.propose;

import wisematches.server.player.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class GameProposal implements Serializable {
	private long id;
	private String title;
	private int minRating;
	private int maxRating;
	private int timeLimits;
	private long gameOwner;
	private int opponentsCount;
	private List<Long> acceptedPlayers = new ArrayList<Long>(2);

	protected GameProposal() {
	}

	protected GameProposal(String title, int timeLimits, int opponentsCount, int minRating, int maxRating, Player player, List<Player> opponents) {
		this.title = title;
		this.timeLimits = timeLimits;
		this.opponentsCount = opponentsCount;
		this.minRating = minRating;
		this.maxRating = maxRating;
		this.gameOwner = player.getId();

		acceptedPlayers.add(gameOwner);
		if (opponents != null) {
			for (Player opponent : opponents) {
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

	public int getMinRating() {
		return minRating;
	}

	public int getMaxRating() {
		return maxRating;
	}

	public boolean isSuitablePlayer(Player p) {
		return getUnsuitableMessage(p) == null;
	}

	public String getUnsuitableMessage(Player p) {
		if (acceptedPlayers.contains(p.getId())) {
			return "exist";
		}
		if (opponentsCount + 1 == acceptedPlayers.size()) {
			return "full";
		}
		if (minRating != 0 && p.getRating() < minRating) {
			return "rating < " + minRating;
		}
		if (maxRating != 0 && p.getRating() > maxRating) {
			return "rating > " + maxRating;
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

	public void attachPlayer(Player player) {
		acceptedPlayers.add(player.getId());
	}

	public void detachPlayer(Player player) {
		acceptedPlayers.remove(player.getId());
	}

	public void setId(long id) {
		this.id = id;
	}
}
