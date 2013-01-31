package wisematches.playground.scribble;

import org.junit.Test;
import wisematches.core.RobotType;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ScribblePlayerHandTest {
	public ScribblePlayerHandTest() {
	}

	@Test
	public void testTiles() {
		final ScribblePlayerHand ph = new ScribblePlayerHand(RobotType.DULL.getPlayer());
		ph.setTiles(new Tile[]{new Tile(1, 'A', 1)});
		assertEquals(1, ph.getTiles().length);

		ph.addTiles(new Tile[]{new Tile(2, 'A', 1), new Tile(3, 'B', 1)});
		assertEquals(3, ph.getTiles().length);
		assertEquals('A', ph.getTiles()[0].getLetter());
		assertEquals('A', ph.getTiles()[1].getLetter());
		assertEquals('B', ph.getTiles()[2].getLetter());

		ph.removeTiles(new Tile[]{new Tile(2, 'A', 1)});
		assertEquals(2, ph.getTiles().length);
		assertEquals('A', ph.getTiles()[0].getLetter());
		assertEquals('B', ph.getTiles()[1].getLetter());
	}
}
