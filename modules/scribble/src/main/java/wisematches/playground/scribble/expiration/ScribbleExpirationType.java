package wisematches.playground.scribble.expiration;

import wisematches.playground.expiration.ExpirationType;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public enum ScribbleExpirationType implements ExpirationType {
    /**
     * Player has one day before time is up.
     */
    ONE_DAY("game.timeout.day", 24 * 60 * 60 * 1000),
    /**
     * Player has half of day before time is up
     */
    HALF_DAY("game.timeout.half", 12 * 60 * 60 * 1000),
    /**
     * Player has one hour before time is up.
     */
    ONE_HOUR("game.timeout.hour", 60 * 60 * 1000);

    private final String code;
    private final long expiringMillis;

    ScribbleExpirationType(String code, long expiringMillis) {
        this.code = code;
        this.expiringMillis = expiringMillis;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public long getTriggerTime(long extinctionTime) {
        return extinctionTime - expiringMillis;
    }
}
