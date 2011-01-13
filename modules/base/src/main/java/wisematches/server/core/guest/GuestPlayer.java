package wisematches.server.core.guest;

import wisematches.server.player.Language;
import wisematches.server.player.notice.PlayerNotification;
import wisematches.server.player.notice.PlayerNotifications;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class GuestPlayer {
	public static final int PLAYER_ID = 1001;

	public static final GuestPlayer GUEST_PLAYER = new GuestPlayer(Language.ENGLISH);

	private static final GuestPlayerNotifications NOTIFICATOR = new GuestPlayerNotifications();

	private GuestPlayer(Language locale) {
//		super(PLAYER_ID, 0, locale, "i18n/players", "guest");
	}

	/**
	 * {@code GuestPlayerNotificator} always returns {@code false} for eny notification types.
	 *
	 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
	 */
	private static final class GuestPlayerNotifications implements PlayerNotifications {
		public void addDisabledNotification(PlayerNotification type) {
			if (type == null) {
				throw new NullPointerException("Type can't be null");
			}
		}

		public void removeDisabledNotification(PlayerNotification type) {
			if (type == null) {
				throw new NullPointerException("Type can't be null");
			}
		}

		public boolean isNotificationEnabled(PlayerNotification type) {
			if (type == null) {
				throw new NullPointerException("Type can't be null");
			}
			return false;
		}
	}
}
