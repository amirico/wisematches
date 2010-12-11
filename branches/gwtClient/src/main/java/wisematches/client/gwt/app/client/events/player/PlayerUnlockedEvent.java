package wisematches.client.gwt.app.client.events.player;

import wisematches.client.gwt.core.client.events.Correlation;
import wisematches.client.gwt.core.client.events.Correlative;
import wisematches.client.gwt.core.client.events.Event;

/**
 * Indicates that player was unlocked.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerUnlockedEvent extends PlayerStateEvent implements Correlative {
    /**
     * This is GWT Serialization only constructor.
     */
    @Deprecated
    public PlayerUnlockedEvent() {
    }

    public PlayerUnlockedEvent(long playerId) {
        super(playerId);
    }

    /**
     * This event correlates with {@code PlayerLockedEvent} and {@code PlayerUnlockedEvent}.
     *
     * @param event the event to be checked.
     * @return {@code MUTUAL_EXCLUSION} if event is {@code PlayerLockedEvent} or
     *         {@code EXCLUSION} if event is  {@code PlayerUnlockedEvent} and has the same player id;
     *         {@code NEUTRAL} - otherwise.
     */
    public Correlation eventCorrelation(Event event) {
        if (event instanceof PlayerLockedEvent) {
            final PlayerLockedEvent ple = (PlayerLockedEvent) event;
            if (ple.getPlayerId() == getPlayerId()) {
                return Correlation.MUTUAL_EXCLUSION;
            }
        } else if (event instanceof PlayerUnlockedEvent) {
            final PlayerUnlockedEvent pue = (PlayerUnlockedEvent) event;
            if (pue.getPlayerId() == getPlayerId()) {
                return Correlation.EXCLUSION;
            }
        }
        return Correlation.NEUTRAL;
    }

    /**
     * This method returns true if spcified playerId equals with player id of this event.
     *
     * @param playerId the player id to be checked.
     * @return {@code true} if
     */
    @Override
    public boolean isEventForPlayer(long playerId) {
        return playerId == getPlayerId();
    }
}
