package wisematches.client.gwt.app.client.content.gameboard.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import wisematches.client.gwt.app.client.content.gameboard.GameboardItemBean;

public interface GameboardServiceAsync {
    void loadPlayerGames(long playerId, AsyncCallback<GameboardItemBean[]> async);
}
