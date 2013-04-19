package wisematches.client.android.view;

import wisematches.client.android.core.Player;

import java.util.TimeZone;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PlayerInfo implements Player {
	private final long id;
	private final String nickname;
	private final String language;
	private final TimeZone timeZone;

	private final String type;
	private final String membership;

	private final boolean online;

	public PlayerInfo(long id, String nickname, String language, TimeZone timeZone, String type, String membership, boolean online) {
		this.id = id;
		this.nickname = nickname;
		this.language = language;
		this.timeZone = timeZone;
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

	public String getLanguage() {
		return language;
	}

	public TimeZone getTimeZone() {
		return timeZone;
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

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Player{");
		sb.append("id=").append(id);
		sb.append(", nickname='").append(nickname).append('\'');
		sb.append(", language='").append(language).append('\'');
		sb.append(", timeZone=").append(timeZone);
		sb.append(", type='").append(type).append('\'');
		sb.append(", membership='").append(membership).append('\'');
		sb.append(", online=").append(online);
		sb.append('}');
		return sb.toString();
	}
}
