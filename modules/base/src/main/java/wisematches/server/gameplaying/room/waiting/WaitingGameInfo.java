package wisematches.server.gameplaying.room.waiting;

import wisematches.server.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class WaitingGameInfo {
	private long id;
	private String title;
	private int timeLimits;
	private int playersCount;
	private int minRating = 0;
	private int maxRating = Integer.MAX_VALUE;
	private List<Long> acceptedPlayers = new ArrayList<Long>(2);

	protected WaitingGameInfo() {
	}

	protected WaitingGameInfo(long id, String title, int timeLimits, int playersCount, Player player) {
		this.id = id;
		this.title = title;
		this.timeLimits = timeLimits;
		this.playersCount = playersCount;
		acceptedPlayers.add(player.getId());
	}

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public int getTimeLimits() {
		return timeLimits;
	}

	public void setTimeLimits(int timeLimits) {
		this.timeLimits = timeLimits;
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
		if (isSuitPlayer(player)) {
			acceptedPlayers.add(player.getId());
		}
	}

	public void detachPlayer(Player player) {
		acceptedPlayers.remove(player.getId());
	}

	public boolean isSuitPlayer(Player p) {
		return p.getRating() > minRating && p.getRating() < maxRating;
	}

	public List<Long> getRegisteredPlayers() {
		return Collections.unmodifiableList(acceptedPlayers);
	}

	public boolean isGameReady() {
		return acceptedPlayers.size() == playersCount;
	}
}
