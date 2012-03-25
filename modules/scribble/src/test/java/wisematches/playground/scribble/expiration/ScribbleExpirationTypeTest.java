package wisematches.playground.scribble.expiration;

import org.junit.Test;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleExpirationTypeTest {
    private static final int ONE_DAY_MILLIS = 24 * 60 * 60 * 1000;

    public ScribbleExpirationTypeTest() {
    }

    @Test
    public void test_nextExpiringType() {
        throw new UnsupportedOperationException("TODO: Commented");
/*
		// More that one day
		assertEquals(GameExpirationType.ONE_DAY, GameExpirationType.nextExpiringPoint(new Date(System.currentTimeMillis() + 2 * ONE_DAY_MILLIS)));
		assertEquals(GameExpirationType.ONE_DAY, GameExpirationType.nextExpiringPoint(new Date(System.currentTimeMillis() + ONE_DAY_MILLIS)));
		// Half day
		assertEquals(GameExpirationType.HALF_DAY, GameExpirationType.nextExpiringPoint(new Date(System.currentTimeMillis() + ONE_DAY_MILLIS - 100)));
		assertEquals(GameExpirationType.HALF_DAY, GameExpirationType.nextExpiringPoint(new Date(System.currentTimeMillis() + ONE_DAY_MILLIS / 2)));
		// One hour
		assertEquals(GameExpirationType.ONE_HOUR, GameExpirationType.nextExpiringPoint(new Date(System.currentTimeMillis() + 60 * 60 * 1000 + 1)));
		assertEquals(GameExpirationType.ONE_HOUR, GameExpirationType.nextExpiringPoint(new Date(System.currentTimeMillis() + 60 * 60 * 1000)));
		// No next type
		assertNull(GameExpirationType.nextExpiringPoint(new Date(System.currentTimeMillis() + 60 * 60 * 1000 - 1)));
		// No next type
		assertNull(GameExpirationType.nextExpiringPoint(new Date(System.currentTimeMillis() - 1)));
*/
    }
/*

	@Test
	public void test_getDelayToRemainder() {
		long l = System.currentTimeMillis();
		final Date expiringDate = new Date(l + ONE_DAY_MILLIS * 2);
		assertEquals(l + ONE_DAY_MILLIS, GameExpirationType.ONE_DAY.getExpirationTriggerTime(expiringDate).getTime());
		assertEquals(l + ONE_DAY_MILLIS + ONE_DAY_MILLIS / 2, GameExpirationType.HALF_DAY.getExpirationTriggerTime(expiringDate).getTime());
		assertEquals(l + 2 * ONE_DAY_MILLIS - ONE_DAY_MILLIS / 24, GameExpirationType.ONE_HOUR.getExpirationTriggerTime(expiringDate).getTime());
	}
*/
}
