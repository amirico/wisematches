package wisematches.client.gwt.app.client.events.board;

import wisematches.client.gwt.app.client.content.player.PlayerInfoBean;
import wisematches.client.gwt.core.client.events.Correlation;
import wisematches.client.gwt.core.client.events.Correlative;
import wisematches.client.gwt.core.client.events.Event;

/**
 * This event indicates that plaer was added or removed to/from game. Event contains base information about
 * player and action.
 * <p/>
 * Two events with the same {@code boardId} and {@code playerId} but with different actions are {@code MUTUAL_EXCLUSION}.
 * Events with the same active are just {@code EXCLUSION} one other.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class GamePlayersEvent extends GameBoardEvent implements Correlative {
    private Action action;
    private PlayerInfoBean playerInfoBean;

    /**
     * This is GWT Serialization only constructor.
     */
    @Deprecated
    public GamePlayersEvent() {
    }

    /**
     * Cretes new event with specified parameter.
     *
     * @param boardId        the board id.
     * @param playerInfoBean the player info bean.
     * @param action         the happend action.
     * @throws NullPointerException if {@code playerInfoBean} or {@code action} is null.
     */
    public GamePlayersEvent(long boardId, PlayerInfoBean playerInfoBean, Action action) {
        super(boardId);
        this.playerInfoBean = playerInfoBean;
        this.action = action;
    }

    /**
     * Returns information about player.
     *
     * @return the player information.
     */
    public PlayerInfoBean getPlayerInfoBean() {
        return playerInfoBean;
    }

    /**
     * Returns happend action.
     *
     * @return the happend action.
     */
    public Action getAction() {
        return action;
    }

    /**
     * If specified event is {@code GamePlayersEvent} and has the same board and player ids then
     * it {@code EXCLUSION} if both events have the same action and {@code MUTUAL_EXCLUSION} if not.
     * <p/>
     * In all other cases {@code NEUTRAL} correlation will be returned.
     *
     * @param event the event.
     * @return the event correlation.
     */
    public Correlation eventCorrelation(Event event) {
        if (event instanceof GamePlayersEvent) {
            final GamePlayersEvent gpe = (GamePlayersEvent) event;
            if (gpe.getBoardId() == getBoardId() &&
                    gpe.getPlayerInfoBean().getPlayerId() == getPlayerInfoBean().getPlayerId()) {
                if (gpe.getAction() == getAction()) {
                    return Correlation.EXCLUSION;
                } else {
                    return Correlation.MUTUAL_EXCLUSION;
                }
            }
        }
        return Correlation.NEUTRAL;
    }

    /**
     * The action that was happend.
     */
    public static enum Action {
        /**
         * Indicates that player was added.
         */
        ADDED,
        /**
         * Indicates that player was removed.
         */
        REMOVED
    }
}