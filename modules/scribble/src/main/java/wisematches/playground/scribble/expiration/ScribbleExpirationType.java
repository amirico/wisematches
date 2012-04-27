package wisematches.playground.scribble.expiration;

import wisematches.playground.expiration.ExpirationType;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public enum ScribbleExpirationType implements ExpirationType {
	/**
	 * Player has one day before time is up.
	 */
	ONE_DAY("playground.game.expiration.day", 24 * 60 * 60 * 1000),
	/**
	 * Player has half of day before time is up
	 */
	HALF_DAY("playground.game.expiration.half", 12 * 60 * 60 * 1000),
	/**
	 * Player has one hour before time is up.
	 */
	ONE_HOUR("playground.game.expiration.hour", 60 * 60 * 1000);

	private final String code;
	private final long remainedMillis;

	ScribbleExpirationType(String code, long remainedMillis) {
		this.code = code;
		this.remainedMillis = remainedMillis;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public long getRemainedTime() {
		return remainedMillis;
	}

	@Override
	public long getTriggerTime(long extinctionTime) {
		return extinctionTime - remainedMillis;
	}


}