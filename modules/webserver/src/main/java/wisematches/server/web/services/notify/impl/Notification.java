package wisematches.server.web.services.notify.impl;

import wisematches.personality.player.member.MemberPlayer;
import wisematches.server.web.services.notify.NotificationMover;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class Notification {
	private final String code;
	private final String subject;
	private final String message;
	private final MemberPlayer player;
	private final NotificationMover mover;

	public Notification(String code, String subject, String message, MemberPlayer player, NotificationMover mover) {
		this.code = code;
		this.subject = subject;
		this.message = message;
		this.player = player;
		this.mover = mover;
	}

	public String getCode() {
		return code;
	}

	public String getSubject() {
		return subject;
	}

	public String getMessage() {
		return message;
	}

	public MemberPlayer getPlayer() {
		return player;
	}

	public NotificationMover getMover() {
		return mover;
	}
}
