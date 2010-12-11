package wisematches.client.gwt.app.client.content.dashboard.services;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import wisematches.client.gwt.app.client.content.dashboard.DashboardItemBean;

/**
 * Service that allows create, join and view all games that waiting a players.
 */
public interface DashboardService extends RemoteService {
    /**
     * Create new game with specified parameters.
     *
     * @param bashboard the parameters of the game and waiting opponents.
     * @return the id of created game or {@code null} if game was not created.
     */
    Long createBoard(DashboardItemBean bashboard);

    /**
     * Joins current player to specified board id.
     *
     * @param boardId the board id that should be joined.
     * @return the joining result.
     */
    JoinResult joinToBoard(long boardId);

    /**
     * Returns current dashboard items.
     *
     * @return the array of all waiting dashboard items.
     */
    DashboardItemBean[] getWaitingDashboardItems();

    /**
     * Result of join operation.
     */
    enum JoinResult {
        /**
         * Player joined and the game sucessfully
         */
        SUCCESS,
        /**
         * Game already has all players and no one can be joined.
         */
        TO_MANY_PLAYERS,
        /**
         * This player already in game
         */
        ALREDY_IN_GAME,
        /**
         * Id of game is unknown
         */
        UNKNOWN_GAME,
        /**
         * Internal error is happend.
         */
        INTERNAL_ERROR,
        /**
         * Indicates that your rating is low for this game
         */
        LOW_RATING,
        /**
         * Indicates that your rating is high for this game
         */
        HIGH_RATING,
        /**
         * Indicates that guest can't join to any game
         */
        GUEST_NOT_ALLOWED
    }

    /**
     * Utility/Convenience class.
     * Use BashboardService.App.getInstance() to access static instance of BashboardServiceAsync
     */
    public static class App {
        private static final DashboardServiceAsync ourInstance;

        static {
            ourInstance = (DashboardServiceAsync) GWT.create(DashboardService.class);
            ((ServiceDefTarget) ourInstance).setServiceEntryPoint("/rpc/DashboardService");
        }

        public static DashboardServiceAsync getInstance() {
            return ourInstance;
        }
    }
}
