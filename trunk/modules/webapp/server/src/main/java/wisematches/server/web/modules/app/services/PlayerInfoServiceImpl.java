package wisematches.server.web.modules.app.services;

import org.springframework.transaction.annotation.Transactional;
import wisematches.kernel.player.Player;
import wisematches.server.core.account.PlayerManager;
import wisematches.server.core.statistic.PlayerStatistic;
import wisematches.server.core.statistic.StatisticsManager;
import wisematches.server.core.sessions.PlayerSessionsManager;
import wisematches.server.core.sessions.impl.PlayerSessionsManagerImpl;
import wisematches.server.web.modules.app.events.producers.PlayerEventProducer;
import wisematches.server.web.rpc.GenericSecureRemoteService;

public class PlayerInfoServiceImpl extends GenericSecureRemoteService { //implements PlayerInfoService {
/*    private PlayerManager playerManager;
    private StatisticsManager statisticsManager;
    private PlayerSessionsManager playerSessionsManager;

    public PlayerInfoServiceImpl() {
    }

    @Override
    @Transactional(readOnly = true)
    public PlayerInfoBean getSessionPlayer() {
        return PlayerEventProducer.convertPlayer(getPlayer());
    }

    @Override
    @Transactional(readOnly = true)
    public PlayerInfoBean getPlayerInfoBean(long playerId) throws UnknownPlayerException {
        final Player player = playerManager.getPlayer(playerId);
        if (player == null) {
            throw new UnknownPlayerException();
        }
        return PlayerEventProducer.convertPlayer(player);
    }

    @Override
    @Transactional(readOnly = true)
    public ShortPlayerInfo getShortPlayerInfo(long playerId) throws UnknownPlayerException {
        final Player player = playerManager.getPlayer(playerId);
        if (player == null) {
            throw new UnknownPlayerException();
        }

        final PlayerStatistic statistic = statisticsManager.getPlayerStatistic(playerId);

        final ShortPlayerInfo bean = new ShortPlayerInfo(playerId, player.getUsername(), getMemberType(player), player.getRating());
        bean.setMaxRatings(statistic.getNinetyDaysRaingInfo().getHighestRating());
        bean.setAverageTimePerMove(statistic.getAverageTurnTime());
        bean.setFinishedGames(statistic.getFinishedGames());
        return bean;
    }

    @Override
    @Transactional(readOnly = true)
    public PlayerOnlineState getPlayerOnlineState(long playerId) throws UnknownPlayerException {
        final Player player = playerManager.getPlayer(playerId);
        if (player == null) {
            throw new UnknownPlayerException();
        }
        return playerSessionsManager.isPlayerOnline(player) ? PlayerOnlineState.ONLINE : PlayerOnlineState.OFFLINE;
    }

    public void setStatisticsManager(StatisticsManager statisticsManager) {
        this.statisticsManager = statisticsManager;
    }

    public void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    public void setPlayerSessionsManager(PlayerSessionsManager playerSessionsManager) {
        this.playerSessionsManager = playerSessionsManager;
    }*/
}