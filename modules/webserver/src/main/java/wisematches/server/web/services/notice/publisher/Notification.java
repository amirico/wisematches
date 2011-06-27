package wisematches.server.web.services.notice.publisher;

import wisematches.personality.Personality;
import wisematches.server.web.services.notice.NotificationDescription;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class Notification {
	private final Personality personality;
	private final NotificationDescription description;
	private final Object context;

	public Notification(Personality personality, NotificationDescription description, Object context) {
		this.personality = personality;
		this.description = description;
		this.context = context;
	}

	public Personality getPersonality() {
		return personality;
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
		sb.append("{personality=").append(personality);
		sb.append(", description=").append(description);
		sb.append(", context=").append(context);
		sb.append('}');
		return sb.toString();
	}
}
