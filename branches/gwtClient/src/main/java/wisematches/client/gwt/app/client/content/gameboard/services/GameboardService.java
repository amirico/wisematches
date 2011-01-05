package wisematches.client.gwt.app.client.content.gameboard.services;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import wisematches.client.gwt.app.client.content.gameboard.GameboardItemBean;

public interface GameboardService extends RemoteService {
    GameboardItemBean[] loadPlayerGames(long playerId);

    /**
     * Utility/Convenience class.
     * Use GameboardService.App.getInstance() to access static instance of GameboardServiceAsync
     */
    public static class App {
        private static final GameboardServiceAsync ourInstance;

        static {
            ourInstance = (GameboardServiceAsync) GWT.create(GameboardService.class);
            ((ServiceDefTarget) ourInstance).setServiceEntryPoint("/rpc/GameboardService");
        }

        public static GameboardServiceAsync getInstance() {
            return ourInstance;
        }
    }
}
