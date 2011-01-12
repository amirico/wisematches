package wisematches.server.core.guest;

import wisematches.kernel.notification.PlayerNotification;
import wisematches.kernel.notification.PlayerNotifications;
import wisematches.server.core.account.impl.ResourceablePlayer;
import wisematches.server.player.Language;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class GuestPlayer extends ResourceablePlayer {
	public static final int PLAYER_ID = 1001;

	public static final GuestPlayer GUEST_PLAYER = new GuestPlayer(Language.ENGLISH);

	private static final GuestPlayerNotifications NOTIFICATOR = new GuestPlayerNotifications();

	private GuestPlayer(Language locale) {
		super(PLAYER_ID, 0, locale, "i18n/players", "guest");
	}

	@Override
	protected ResourceablePlayer createNewPlayer(Language locale) {
		return new GuestPlayer(locale);
	}

	@Override
	public GuestPlayer getNationalityPlayer(Language language) {
		return (GuestPlayer) super.getNationalityPlayer(language);
	}

	@Override
	public PlayerNotifications getPlayerNotifications() {
		return NOTIFICATOR;
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
