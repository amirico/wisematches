package wisematches.client.gwt.app.client.events.board;

import wisematches.client.gwt.app.client.content.dashboard.DashboardItemBean;

/**
 * This event indicates that new game was creted. Events contains settings of created game and list of
 * joined players.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class GameCreatedEvent extends GameBoardEvent {
    private DashboardItemBean gameSettingsBean;

    /**
     * This is GWT Serialization only constructor.
     */
    @Deprecated
    public GameCreatedEvent() {
    }

    /**
     * Creates new event with specified parameters.
     *
     * @param gameSettingsBean the settings of created game.
     * @throws NullPointerException if {@code gameSettingsBean} is null
     */
    public GameCreatedEvent(DashboardItemBean gameSettingsBean) {
        super(gameSettingsBean.getBoardId());
        this.gameSettingsBean = gameSettingsBean;
    }

    /**
     * Returns settings of created game.
     *
     * @return the create game settings.
     */
    public DashboardItemBean getGameSettingsBean() {
        return gameSettingsBean;
    }
}
