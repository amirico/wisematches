package wisematches.client.gwt.app.client.content.player.services;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import wisematches.client.gwt.app.client.content.UnknownPlayerException;
import wisematches.client.gwt.app.client.content.player.ShortPlayerInfo;
import wisematches.client.gwt.app.client.content.player.PlayerInfoBean;
import wisematches.client.gwt.app.client.content.player.PlayerOnlineState;

public interface PlayerInfoService extends RemoteService {
    /**
     * Returns id of player that belongs to this session.
     *
     * @return the id of player that belongs to this session.
     */
    PlayerInfoBean getSessionPlayer();

    PlayerInfoBean getPlayerInfoBean(long playerId) throws UnknownPlayerException;

    ShortPlayerInfo getShortPlayerInfo(long playerId) throws UnknownPlayerException;

    /**
     * Returns state of player by specified id.
     *
     * @param playerId the id of player which state should be returned
     * @return the state of player.
     * @throws UnknownPlayerException if player with specified id is unknown.
     */
    PlayerOnlineState getPlayerOnlineState(long playerId) throws UnknownPlayerException;

    /**
     * Utility/Convenience class.
     * Use PlayerInfoService.App.getInstance() to access static instance of PlayerInfoServiceAsync
     */
    public static class App {
        private static final PlayerInfoServiceAsync ourInstance;

        static {
            ourInstance = (PlayerInfoServiceAsync) GWT.create(PlayerInfoService.class);
            ((ServiceDefTarget) ourInstance).setServiceEntryPoint("/rpc/PlayerInfoService");
        }

        public static PlayerInfoServiceAsync getInstance() {
            return ourInstance;
        }
    }
}
