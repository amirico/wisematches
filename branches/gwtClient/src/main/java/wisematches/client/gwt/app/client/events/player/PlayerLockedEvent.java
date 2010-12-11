package wisematches.client.gwt.app.client.events.player;

import wisematches.client.gwt.core.client.events.Correlation;
import wisematches.client.gwt.core.client.events.Correlative;
import wisematches.client.gwt.core.client.events.Event;

/**
 * Indicates that player was locked by some reasone. This event is sent only to locked plaer.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerLockedEvent extends PlayerStateEvent implements Correlative {
    private String reasone;
    private long unlockTime;

    /**
     * This is GWT Serialization only constructor.
     */
    @Deprecated
    public PlayerLockedEvent() {
    }

    /**
     * Creates new event with specified parameters.
     *
     * @param playerId   the id of player who was locked.
     * @param reasone    the lock reasone.
     * @param unlockTime the unlock time.
     */
    public PlayerLockedEvent(long playerId, String reasone, long unlockTime) {
        super(playerId);
        this.reasone = reasone;
        this.unlockTime = unlockTime;
    }

    /**
     * Returns reasone of lock.
     *
     * @return the lock's reasone
     */
    public String getReasone() {
        return reasone;
    }

    /**
     * Return time when player will be unlocked.
     *
     * @return the time when player will be unlocked.
     */
    public long getUnlockTime() {
        return unlockTime;
    }

    /**
     * This event correlates with {@code PlayerLockedEvent} and {@code PlayerUnlockedEvent}.
     *
     * @param event the event to be checked.
     * @return {@code EXCLUSION} if event is {@code PlayerLockedEvent} or {@code PlayerUnlockedEvent} and
     *         has the same player id; {@code NEUTRAL} - otherwise.
     */
    public Correlation eventCorrelation(Event event) {
        if (event instanceof PlayerLockedEvent) {
            final PlayerLockedEvent ple = (PlayerLockedEvent) event;
            if (ple.getPlayerId() == getPlayerId()) {
                return Correlation.EXCLUSION;
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
