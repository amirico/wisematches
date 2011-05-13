package wisematches.playground.expiration;

import java.util.Date;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public enum GameExpirationType {
	/**
	 * Player has one day before time is up.
	 */
	ONE_DAY(24 * 60 * 60 * 1000),
	/**
	 * Player has half of day before time is up
	 */
	HALF_DAY(12 * 60 * 60 * 1000),
	/**
	 * Player has one hour before time is up.
	 */
	ONE_HOUR(60 * 60 * 1000);

	private final long expiringMillis;

	private static final long MILLISECOND_IN_DAY = 24 * 60 * 60 * 1000;

	GameExpirationType(long expiringMillis) {
		this.expiringMillis = expiringMillis;
	}

	public Date getExpirationTriggerTime(Date expiringDate) {
		return new Date(expiringDate.getTime() - expiringMillis);
	}

	public static GameExpirationType nextExpiringPoint(Date expiringDate) {
		final long currentTime = System.currentTimeMillis();
		for (GameExpirationType type : values()) {
			if (expiringDate.getTime() - currentTime >= type.expiringMillis) {
				return type;
			}
		}
		return null;
	}
}
