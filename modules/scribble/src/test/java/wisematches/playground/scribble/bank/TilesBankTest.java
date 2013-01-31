package wisematches.playground.scribble.bank;

import org.junit.Test;
import wisematches.core.Language;
import wisematches.playground.scribble.Tile;
import wisematches.playground.scribble.bank.impl.TilesBankInfoEditor;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class TilesBankTest {
	public TilesBankTest() {
	}

	@Test
	public void testBank() {
		final TilesBankInfoEditor editor = new TilesBankInfoEditor(Language.EN);
		editor.add('a', 5, 1);
		editor.add('b', 4, 2);
		editor.add('c', 3, 3);
		editor.add('d', 2, 4);
		editor.add('z', 1, 5);

		TilesBank b = new TilesBank(editor.createTilesBankInfo());

		assertEquals(15, b.getBankCapacity());
		assertEquals(15, b.getTilesLimit());
		assertFalse(b.isEmpty());

		assertEquals('a', b.getTile(1).getLetter());
		assertEquals('a', b.getTile(4).getLetter());
		assertEquals('b', b.getTile(5).getLetter());
		assertEquals('b', b.getTile(8).getLetter());
		assertEquals('c', b.getTile(9).getLetter());
		assertEquals(15, b.getBankCapacity());
		assertEquals(15, b.getTilesLimit());

		final Tile tile = b.requestTile(1);
		assertNotNull(tile);
		assertEquals(15, b.getBankCapacity());
		assertEquals(14, b.getTilesLimit());
		try {
			b.requestTile(1);
			fail("Exception must be here: tile already requested");
		} catch (IllegalArgumentException ex) {
		}

		final Tile[] requredTiles = b.requestTiles(12);
		assertEquals(12, requredTiles.length);
		assertEquals(15, b.getBankCapacity());
		assertEquals(2, b.getTilesLimit());

		final Tile[] tiles = b.requestTiles(10);
		assertEquals(2, tiles.length);
		assertEquals(15, b.getBankCapacity());
		assertEquals(0, b.getTilesLimit());
		assertTrue(b.isEmpty());

		b.rollbackTile(1);
		assertEquals(1, b.getTilesLimit());
		assertFalse(b.isEmpty());

		b.rollbackTile(1);
		assertEquals(1, b.getTilesLimit());
		assertFalse(b.isEmpty());

		assertNotNull(b.requestTile(1));
	}
}
