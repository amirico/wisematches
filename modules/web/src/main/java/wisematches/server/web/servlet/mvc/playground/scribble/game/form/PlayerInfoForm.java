package wisematches.server.web.servlet.mvc.playground.scribble.game.form;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PlayerInfoForm {
	private final long id;
	private final String nickname;
	private final String membership;
	private final boolean online;
	private final int points;

	public PlayerInfoForm(long id, String nickname, String membership, boolean online, int points) {
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

	public String getMembership() {
		return membership;
	}

	public boolean isOnline() {
		return online;
	}

	public int getPoints() {
		return points;
	}
}
