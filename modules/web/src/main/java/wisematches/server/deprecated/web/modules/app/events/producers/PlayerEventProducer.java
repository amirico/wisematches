package wisematches.server.deprecated.web.modules.app.events.producers;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerEventProducer {//implements EventProducer {
/*
    private EventNotificator notificator;

    private PlayerStatisticsManager statisticsManager;

    private final LockAccountListener lockAccountListener = new TheLockAccountListener();
    private final PlayerStatisticListener playerStatisticListener = new ThePlayerStatisticListener();
    private LockAccountManager lockAccountManager;

    public void activateProducer(EventNotificator notificator) {
        this.notificator = notificator;
    }

    public void deactivateProducer() {
        this.notificator = null;
    }

    public void setPlayerStatisticsManager(PlayerStatisticsManager statisticsManager) {
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
        return new PlayerInfoBean(player.getId(), player.getNickname(), getMemberType(player), player.getRating());
    }

    public static MemberType getMemberType(Player player) {
        if (player instanceof RobotPlayer) {
            return MemberType.ROBOT;
        } else if (player instanceof GuestPlayer) {
            return MemberType.GUEST;
        }
        return MemberType.GUEST;
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
