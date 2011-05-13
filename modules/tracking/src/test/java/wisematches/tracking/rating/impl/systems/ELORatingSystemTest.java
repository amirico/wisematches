package wisematches.tracking.rating.impl.systems;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ELORatingSystemTest {
	public ELORatingSystemTest() {
	}

	@Test
	public void testCalculateRatings() {
		short[] ints;
		final ELORatingSystem e = new ELORatingSystem();

		ints = e.calculateRatings(
				new short[]{1613, 1609, 1477, 1388, 1586, 1720},
				new short[]{100, 200, 100, 80, 80, 200});
		assertEquals(1601, ints[0]);

		ints = e.calculateRatings(
				new short[]{1613, 1609, 1477, 1388, 1586, 1720},
				new short[]{100, 80, 80, 200, 100, 100});
		assertEquals(1617, ints[0]);
	}
}
