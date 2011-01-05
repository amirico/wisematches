package wisematches.client.gwt.app.client.content.dashboard.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import wisematches.client.gwt.app.client.content.dashboard.DashboardItemBean;

public interface DashboardServiceAsync {

    /**
     * Create new game with specified parameters.
     *
     * @param bashboard the parameters of the game and waiting opponents.
     * @return the id of created game or {@code null} if game was not created.
     */
    void createBoard(DashboardItemBean bashboard, AsyncCallback<Long> async);

    /**
     * Joins current player to specified board id.
     *
     * @param boardId the board id that should be joined.
     * @return the joining result.
     */
    void joinToBoard(long boardId, AsyncCallback<DashboardService.JoinResult> async);

    /**
     * Returns current dashboard items.
     *
     * @return the array of all waiting dashboard items.
     */
    void getWaitingDashboardItems(AsyncCallback<DashboardItemBean[]> async);
}
