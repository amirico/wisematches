package wisematches.client.android;

import java.util.TimeZone;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class Player {
	private final long id;
	private final String nickname;
	private final String language;
	private final TimeZone timeZone;

	private final String type;
	private final String membership;

	private final boolean online;

	public Player(long id, String nickname, String language, TimeZone timeZone, String type, String membership, boolean online) {
		this.id = id;
		this.nickname = nickname;
		this.language = language;
		this.timeZone = timeZone;
		this.type = type;
		this.membership = membership;
		this.online = online;
	}

	public long getId() {
		return id;
	}

	public String getNickname() {
		return nickname;
	}

	public String getLanguage() {
		return language;
	}

	public TimeZone getTimeZone() {
		return timeZone;
	}

	public String getType() {
		return type;
	}

	public String getMembership() {
		return membership;
	}

	public boolean isOnline() {
		return online;
	}
}
