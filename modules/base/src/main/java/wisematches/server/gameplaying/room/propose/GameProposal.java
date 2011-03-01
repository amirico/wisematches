package wisematches.server.gameplaying.room.propose;

import wisematches.server.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class GameProposal {
	private long id;
	private String title;
	private int timeLimits;
	private int playersCount;
	private int minRating = 0;
	private int maxRating = Integer.MAX_VALUE;
	private long gameOwner;
	private List<Long> acceptedPlayers = new ArrayList<Long>(2);

	protected GameProposal() {
	}

	protected GameProposal(long id, String title, int timeLimits, int playersCount, Player player) {
		this.id = id;
		this.title = title;
		this.timeLimits = timeLimits;
		this.playersCount = playersCount;
		this.gameOwner = player.getId();
		acceptedPlayers.add(gameOwner);
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

	public int getPlayersCount() {
		return playersCount;
	}

	public int getMinRating() {
		return minRating;
	}

	public void setMinRating(int minRating) {
		this.minRating = minRating;
	}

	public int getMaxRating() {
		return maxRating;
	}

	public void setMaxRating(int maxRating) {
		this.maxRating = maxRating;
	}

	public void attachPlayer(Player player) {
		if (isSuitablePlayer(player)) {
			acceptedPlayers.add(player.getId());
		}
	}

	public void detachPlayer(Player player) {
		acceptedPlayers.remove(player.getId());
	}

	public boolean isSuitablePlayer(Player p) {
		return p.getRating() > minRating && p.getRating() < maxRating;
	}

	public List<Long> getRegisteredPlayers() {
		return Collections.unmodifiableList(acceptedPlayers);
	}

	public boolean isGameReady() {
		return acceptedPlayers.size() == playersCount;
	}
}
