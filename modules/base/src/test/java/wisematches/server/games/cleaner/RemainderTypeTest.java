package wisematches.server.games.cleaner;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RemainderTypeTest {
	private static final int ONE_DAY_MILLIS = 24 * 60 * 60 * 1000;

	@Test
	public void test_getNextReminderType() {
		assertEquals(RemainderType.ONE_DAY, RemainderType.getNextReminderType(3, System.currentTimeMillis()));
		assertEquals(RemainderType.HALF_OF_DAY, RemainderType.getNextReminderType(3, System.currentTimeMillis() - 2 * ONE_DAY_MILLIS - 1000)); //two days and 1 seconds elapsed
		assertEquals(RemainderType.ONE_HOUR, RemainderType.getNextReminderType(3, System.currentTimeMillis() - 2 * ONE_DAY_MILLIS - ONE_DAY_MILLIS / 2 - 1000)); //two days and half and 1 seconds elapsed
		assertEquals(RemainderType.TIME_IS_UP, RemainderType.getNextReminderType(3, System.currentTimeMillis() - 3 * ONE_DAY_MILLIS + 60 * 60 * 1000 - 1000)); //two days and half and 1 seconds elapsed
		assertEquals(null, RemainderType.getNextReminderType(3, System.currentTimeMillis() - 3 * ONE_DAY_MILLIS - 1000)); // time is up
	}

	@Test
	public void test_getDelayToRemainder() {
		assertEquals(2 * ONE_DAY_MILLIS, RemainderType.ONE_DAY.getDelayToRemainder(3, System.currentTimeMillis()));
		assertEquals(2 * ONE_DAY_MILLIS + ONE_DAY_MILLIS / 2, RemainderType.HALF_OF_DAY.getDelayToRemainder(3, System.currentTimeMillis()));
		assertEquals(3 * ONE_DAY_MILLIS - 60 * 60 * 1000, RemainderType.ONE_HOUR.getDelayToRemainder(3, System.currentTimeMillis()));

		assertEquals(2 * ONE_DAY_MILLIS - 1000000, RemainderType.ONE_DAY.getDelayToRemainder(3, System.currentTimeMillis() - 1000000));
	}
}
