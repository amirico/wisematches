package wisematches.server.web.services.notice;

import wisematches.personality.player.member.MemberPlayer;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class Notification {
	private final MemberPlayer member;
	private final NotificationDescription description;
	private final Object context;

	public Notification(MemberPlayer member, NotificationDescription description, Object context) {
		this.member = member;
		this.context = context;
		this.description = description;
	}

	public MemberPlayer getMember() {
		return member;
	}

	public NotificationDescription getDescription() {
		return description;
	}

	public Object getContext() {
		return context;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Notification");
		sb.append("{personality=").append(member);
		sb.append(", description=").append(description);
		sb.append(", context=").append(context);
		sb.append('}');
		return sb.toString();
	}
}
