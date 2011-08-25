package wisematches.server.web.services.notify.impl.publish.reducer;

import wisematches.personality.player.member.MemberPlayer;
import wisematches.server.web.services.notify.NotificationDescription;
import wisematches.server.web.services.notify.NotificationMover;

import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
class NotificationInfo {
	final String code;
	final MemberPlayer player;
	final NotificationMover mover;
	final Map<String, Object> model;
	final NotificationDescription description;

	NotificationInfo(String code, MemberPlayer player, NotificationMover mover, NotificationDescription description, Map<String, Object> model) {
		this.code = code;
		this.player = player;
		this.mover = mover;
		this.model = model;
		this.description = description;
	}
}
