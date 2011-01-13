package wisematches.server.robot.impl;

import wisematches.kernel.notification.PlayerNotification;
import wisematches.kernel.notification.PlayerNotifications;
import wisematches.server.player.Language;
import wisematches.server.player.Membership;
import wisematches.server.robot.RobotPlayer;
import wisematches.server.robot.RobotType;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
final class RobotPlayerImpl implements RobotPlayer {
	private final RobotType robotType;

	private static final RobotPlayerNotifications NOTIFICATOR = new RobotPlayerNotifications();

	public RobotPlayerImpl(long id, RobotType robotType) {
		this(id, robotType, Language.ENGLISH);
	}

	private RobotPlayerImpl(long id, RobotType robotType, Language locale) {
//		super(id, robotType.getRating(), locale, "i18n/players", robotType.name().toLowerCase());
		this.robotType = robotType;
	}

	@Override
	public RobotType getRobotType() {
		return robotType;
	}

	@Override
	public String toString() {
		return "RobotPlayerImpl{" +
				"robotId=" + getId() +
				"robotType=" + robotType +
				'}';
	}

	@Override
	public long getId() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public String getEmail() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public String getUsername() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public String getPassword() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Language getLanguage() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Membership getMembership() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public int getRating() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * {@code RobotPlayerNotificator} always returns {@code false} for eny notification types.
	 *
	 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
	 */
	private static final class RobotPlayerNotifications implements PlayerNotifications {
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
