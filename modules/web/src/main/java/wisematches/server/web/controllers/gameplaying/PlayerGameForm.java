package wisematches.server.web.controllers.gameplaying;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PlayerGameForm {
	private long playerId;
	private String nickname;
	private int rating;
	private int index;
	private int points;

	public PlayerGameForm() {
	}

	public PlayerGameForm(long playerId, String nickname, int rating, int index, int points) {
		this.playerId = playerId;
		this.nickname = nickname;
		this.rating = rating;
		this.index = index;
		this.points = points;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
}
