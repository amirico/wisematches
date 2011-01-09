package wisematches.server.core.robot.impl;

import wisematches.kernel.notification.PlayerNotification;
import wisematches.kernel.notification.PlayerNotifications;
import wisematches.kernel.util.Language;
import wisematches.server.core.account.impl.ResourceablePlayer;
import wisematches.server.core.robot.RobotPlayer;
import wisematches.server.core.robot.RobotType;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
final class RobotPlayerImpl extends ResourceablePlayer implements RobotPlayer {
    private final RobotType robotType;

    private static final RobotPlayerNotifications NOTIFICATOR = new RobotPlayerNotifications();

    public RobotPlayerImpl(long id, RobotType robotType) {
        this(id, robotType, Language.ENGLISH);
    }

    private RobotPlayerImpl(long id, RobotType robotType, Language locale) {
        super(id, robotType.getRating(), locale, "i18n/players", robotType.name().toLowerCase());
        this.robotType = robotType;
    }

    @Override
    protected ResourceablePlayer createNewPlayer(Language locale) {
        return new RobotPlayerImpl(getId(), robotType, locale);
    }

    @Override
    public RobotType getRobotType() {
        return robotType;
    }

    @Override
    public RobotPlayerImpl getNationalityPlayer(Language language) {
        return (RobotPlayerImpl) super.getNationalityPlayer(language);
    }

    @Override
    public PlayerNotifications getPlayerNotifications() {
        return NOTIFICATOR;
    }

    @Override
    public String toString() {
        return "RobotPlayerImpl{" +
                "robotId=" + getId() +
                "robotType=" + robotType +
                '}';
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
