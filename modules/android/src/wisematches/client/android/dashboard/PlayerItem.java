package wisematches.client.android.dashboard;

import wisematches.client.android.Membership;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
class PlayerItem {
	private final long id;
	private final String nickname;
	private final Membership membership;
	private final boolean online;
	private final int points;

	public PlayerItem(long id, String nickname, Membership membership, boolean online, int points) {
		this.id = id;
		this.nickname = nickname;
		this.membership = membership;
		this.online = online;
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

	public Membership getMembership() {
		return membership;
	}

	public boolean isOnline() {
		return online;
	}
}
