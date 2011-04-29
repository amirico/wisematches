package wisematches.server.playground.scribble;

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class WordTest {
	@Test
	public void testIteratorVertical() {
		Word w = new Word(new Position(3, 3), Direction.VERTICAL,
				new Tile(1, 'A', 1),
				new Tile(2, 'B', 2),
				new Tile(3, 'C', 3)
		);

		final Iterator<Word.IteratorItem> it = w.iterator();

		assertTrue(it.hasNext());
		Word.IteratorItem wordItem = it.next();
		assertEquals('A', wordItem.getTile().getLetter());
		assertEquals(3, wordItem.getRow());
		assertEquals(3, wordItem.getColumn());

		assertTrue(it.hasNext());
		wordItem = it.next();
		assertEquals('B', wordItem.getTile().getLetter());
		assertEquals(4, wordItem.getRow());
		assertEquals(3, wordItem.getColumn());

		assertTrue(it.hasNext());
		wordItem = it.next();
		assertEquals('C', wordItem.getTile().getLetter());
		assertEquals(5, wordItem.getRow());
		assertEquals(3, wordItem.getColumn());

		assertFalse(it.hasNext());
		try {
			it.next();
			fail("Exception must be here");
		} catch (IllegalStateException ex) {
			;
		}
	}

	@Test
	public void testIteratorHorizontal() {
		Word w = new Word(new Position(3, 3), Direction.HORIZONTAL,
				new Tile(1, 'A', 1),
				new Tile(2, 'B', 2),
				new Tile(3, 'C', 3)
		);

		final Iterator<Word.IteratorItem> it = w.iterator();

		assertTrue(it.hasNext());
		Word.IteratorItem wordItem = it.next();
		assertEquals('A', wordItem.getTile().getLetter());
		assertEquals(3, wordItem.getRow());
		assertEquals(3, wordItem.getColumn());

		assertTrue(it.hasNext());
		wordItem = it.next();
		assertEquals('B', wordItem.getTile().getLetter());
		assertEquals(3, wordItem.getRow());
		assertEquals(4, wordItem.getColumn());

		assertTrue(it.hasNext());
		wordItem = it.next();
		assertEquals('C', wordItem.getTile().getLetter());
		assertEquals(3, wordItem.getRow());
		assertEquals(5, wordItem.getColumn());

		assertFalse(it.hasNext());
		try {
			it.next();
			fail("Exception must be here");
		} catch (IllegalStateException ex) {
			;
		}
	}
}
