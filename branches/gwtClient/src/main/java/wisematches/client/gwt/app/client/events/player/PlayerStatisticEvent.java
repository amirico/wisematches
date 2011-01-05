package wisematches.client.gwt.app.client.events.player;

import wisematches.client.gwt.core.client.events.Correlation;
import wisematches.client.gwt.core.client.events.Correlative;
import wisematches.client.gwt.core.client.events.Event;

/**
 * Indicates that player statistic or rating was changed.
 * <p/>
 * This is empty event and informatin about changed player should be taken from server using service.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerStatisticEvent extends PlayerStateEvent implements Correlative {
    /**
     * This is GWT Serialization only constructor.
     */
    @Deprecated
    public PlayerStatisticEvent() {
    }

    public PlayerStatisticEvent(long playerId) {
        super(playerId);
    }

    public Correlation eventCorrelation(Event event) {
        if (event instanceof PlayerStatisticEvent) {
            final PlayerStatisticEvent e = (PlayerStatisticEvent) event;
            if (e.getPlayerId() == getPlayerId()) {
                return Correlation.EXCLUSION;
            }
        }
        return Correlation.NEUTRAL;
    }
}
