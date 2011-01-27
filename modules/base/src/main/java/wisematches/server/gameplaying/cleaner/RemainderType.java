package wisematches.server.gameplaying.cleaner;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public enum RemainderType {
	/**
	 * Player has one day before time is up.
	 */
	ONE_DAY(24 * 60 * 60 * 1000),
	/**
	 * Player has half of day before time is up
	 */
	HALF_OF_DAY(12 * 60 * 60 * 1000),
	/**
	 * Player has one hour before time is up.
	 */
	ONE_HOUR(60 * 60 * 1000),
	/**
	 * Time is up. Game was terminated.
	 */
	TIME_IS_UP(0);

	private final long reminderPeriod;

	private static final long MILLISECOND_IN_DAY = 24 * 60 * 60 * 1000;
	private RemainderType previousRemainderType;

	RemainderType(long reminderPeriod) {
		this.reminderPeriod = reminderPeriod;
	}

	public long getReminderPeriod() {
		return reminderPeriod;
	}

	public long getDelayToRemainder(int daysPerMove, long lastMoveTime) {
		final long millisecondsPerMove = daysPerMove * MILLISECOND_IN_DAY;
		return lastMoveTime + millisecondsPerMove - reminderPeriod - System.currentTimeMillis();
	}

	public static RemainderType getNextReminderType(int daysPerMove, long lastMoveTime) {
		final RemainderType[] remainedTimes = RemainderType.values();
		for (RemainderType remainedTime : remainedTimes) {
			if (remainedTime.getDelayToRemainder(daysPerMove, lastMoveTime) > 0) {
				return remainedTime;
			}
		}
		return null;
	}

	public RemainderType getPreviousRemainderType() {
		if (ordinal() == 0) {
			return null;
		}
		return values()[ordinal() - 1];
	}
}
