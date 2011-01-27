package wisematches.server.testimonial.rating.impl.systems;

import org.junit.Test;
import wisematches.server.player.Player;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ELORatingSystemTest {
	@Test
	public void testCalculateRatings() {
		Player p1 = createMockPlayer(1, 1613);
		Player p2 = createMockPlayer(1, 1609);
		Player p3 = createMockPlayer(1, 1477);
		Player p4 = createMockPlayer(1, 1388);
		Player p5 = createMockPlayer(1, 1586);
		Player p6 = createMockPlayer(1, 1720);

		final ELORatingSystem e = new ELORatingSystem();
		int[] ints;

		ints = e.calculateRatings(
				new Player[]{p1, p2, p3, p4, p5, p6},
				new int[]{100, 200, 100, 80, 80, 200});
		assertEquals(1601, ints[0]);

		ints = e.calculateRatings(
				new Player[]{p1, p2, p3, p4, p5, p6},
				new int[]{100, 80, 80, 200, 100, 100});
		assertEquals(1617, ints[0]);
	}

	private Player createMockPlayer(long id, int rating) {
		Player player = createMock(Player.class);
		expect(player.getId()).andReturn(id).anyTimes();
		expect(player.getRating()).andReturn(rating).anyTimes();
		replay(player);
		return player;
	}
}
