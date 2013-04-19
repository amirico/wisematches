package wisematches.client.android.app.playground.scribble.model;

import wisematches.client.android.core.Player;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribblePlayer implements Player {
	private final long id;
	private final String nickname;
	private final String type;
	private final String membership;
	private final boolean online;

	private int points;
	private int oldRating;
	private int newRating;
	private boolean winner;

	public ScribblePlayer(long id, String nickname, String type, String membership, boolean online) {
		this.id = id;
		this.nickname = nickname;
		this.type = type;
		this.membership = membership;
		this.online = online;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public String getNickname() {
		return nickname;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String getMembership() {
		return membership;
	}

	@Override
	public boolean isOnline() {
		return online;
	}

	public int getPoints() {
		return points;
	}

	public int getOldRating() {
		return oldRating;
	}

	public int getNewRating() {
		return newRating;
	}

	public boolean isWinner() {
		return winner;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public void setOldRating(int oldRating) {
		this.oldRating = oldRating;
	}

	public void setNewRating(int newRating) {
		this.newRating = newRating;
	}

	public void setWinner(boolean winner) {
		this.winner = winner;
	}
}
