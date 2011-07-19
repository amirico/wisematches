package wisematches.playground.scribble;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ScribblePlayerHandTest extends TestCase {
	public ScribblePlayerHandTest() {
	}

	public void testTiles() {
		ScribblePlayerHand ph = new ScribblePlayerHand(1);
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
