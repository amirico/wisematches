package wisematches.client.android.dashboard;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
class PlayerItem {
	private final long id;
	private final String nickname;
	private final int points;

	public PlayerItem(long id, String nickname, int points) {
		this.id = id;
		this.nickname = nickname;
		this.points = points;
	}

	public long getId() {
		return id;
	}

	public String getNickname() {
		return nickname;
	}

	public int getPoints() {
		return points;
	}
}
