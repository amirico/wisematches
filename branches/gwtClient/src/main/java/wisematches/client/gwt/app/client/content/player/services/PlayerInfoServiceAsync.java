package wisematches.client.gwt.app.client.content.player.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import wisematches.client.gwt.app.client.content.player.ShortPlayerInfo;
import wisematches.client.gwt.app.client.content.player.PlayerInfoBean;
import wisematches.client.gwt.app.client.content.player.PlayerOnlineState;

public interface PlayerInfoServiceAsync {

    /**
     * Returns id of player that belongs to this session.
     *
     * @return the id of player that belongs to this session.
     */
    void getSessionPlayer(AsyncCallback<PlayerInfoBean> async);

    void getPlayerInfoBean(long playerId, AsyncCallback<PlayerInfoBean> async);

    void getShortPlayerInfo(long playerId, AsyncCallback<ShortPlayerInfo> async);

    void getPlayerOnlineState(long playerId, AsyncCallback<PlayerOnlineState> async);
}
