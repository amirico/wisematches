package wisematches.server.web.modules.app.events.producers;

import wisematches.kernel.player.Player;
import wisematches.server.core.account.LockAccountListener;
import wisematches.server.core.account.LockAccountManager;
import wisematches.server.core.account.PlayerManager;
import wisematches.server.core.guest.GuestPlayer;
import wisematches.server.core.robot.RobotPlayer;
import wisematches.server.core.statistic.PlayerStatistic;
import wisematches.server.core.statistic.PlayerStatisticListener;
import wisematches.server.core.statistic.StatisticsManager;
import wisematches.server.web.modules.app.events.EventNotificator;
import wisematches.server.web.modules.app.events.EventProducer;

import java.util.Date;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerEventProducer {//implements EventProducer {
/*
    private EventNotificator notificator;

    private StatisticsManager statisticsManager;

    private final LockAccountListener lockAccountListener = new TheLockAccountListener();
    private final PlayerStatisticListener playerStatisticListener = new ThePlayerStatisticListener();
    private LockAccountManager lockAccountManager;

    public void activateProducer(EventNotificator notificator) {
        this.notificator = notificator;
    }

    public void deactivateProducer() {
        this.notificator = null;
    }

    public void setStatisticsManager(StatisticsManager statisticsManager) {
        if (this.statisticsManager != null) {
            this.statisticsManager.removePlayerStatisticListener(playerStatisticListener);
        }

        this.statisticsManager = statisticsManager;

        if (this.statisticsManager != null) {
            this.statisticsManager.addPlayerStatisticListener(playerStatisticListener);
        }
    }

    public void setLockAccountManager(LockAccountManager lockAccountManager) {
        if (this.lockAccountManager != null) {
            this.lockAccountManager.removeLockAccountListener(lockAccountListener);
        }

        this.lockAccountManager = lockAccountManager;

        if (this.lockAccountManager != null) {
            this.lockAccountManager.addLockAccountListener(lockAccountListener);
        }
    }

    public static PlayerInfoBean convertPlayer(final long playerId, final PlayerManager playerManager) {
        return convertPlayer(playerManager.getPlayer(playerId));
    }

    public static PlayerInfoBean convertPlayer(final Player player) {
        return new PlayerInfoBean(player.getId(), player.getUsername(), getMemberType(player), player.getRating());
    }

    public static MemberType getMemberType(Player player) {
        if (player instanceof RobotPlayer) {
            return MemberType.ROBOT;
        } else if (player instanceof GuestPlayer) {
            return MemberType.GUEST;
        }
        return MemberType.BASIC;
    }

    private class ThePlayerStatisticListener implements PlayerStatisticListener {
        public void playerStatisticUpdated(long playerId, PlayerStatistic statistic) {
            if (notificator != null) {
                notificator.fireEvent(new PlayerStatisticEvent(playerId));
            }
        }
    }

    private class TheLockAccountListener implements LockAccountListener {
        public void accountLocked(Player player, String publicReason, String privateReason, Date unlockdate) {
            if (notificator != null) {
                notificator.fireEvent(new PlayerLockedEvent(player.getId(), publicReason, unlockdate.getTime()));
            }
        }

        public void accountUnlocked(Player player) {
            if (notificator != null) {
                notificator.fireEvent(new PlayerUnlockedEvent(player.getId()));
            }
        }
    }
*/
}
