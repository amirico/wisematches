package wisematches.server.gameplaying.board;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PlayersIteratorTest extends TestCase {
	public void failed() {
		throw new UnsupportedOperationException("TestCase should be validated!");
	}

	public void testIterator1() {
		GamePlayerHand h1 = new GamePlayerHand(1);
		GamePlayerHand h2 = new GamePlayerHand(2);

		PlayersIterator<GamePlayerHand> iterator;

		iterator = new PlayersIterator<GamePlayerHand>(new ArrayList<GamePlayerHand>());
		assertFalse(iterator.hasNext());

		iterator = new PlayersIterator<GamePlayerHand>(Arrays.asList(h1));
		assertFalse(iterator.hasNext());

		try {
			iterator.setPlayerTurn(h2);
		} catch (NoSuchElementException ex) {
			;
		}
		iterator.setPlayerTurn(h1);
		assertTrue(iterator.hasNext());

		iterator = new PlayersIterator<GamePlayerHand>(new ArrayList<GamePlayerHand>());
		assertFalse(iterator.hasNext());
	}

	public void testIterator2() {
		GamePlayerHand h1 = new GamePlayerHand(1);
		GamePlayerHand h2 = new GamePlayerHand(2);
		GamePlayerHand h3 = new GamePlayerHand(3);

		PlayersIterator<GamePlayerHand> pi = new PlayersIterator<GamePlayerHand>(Arrays.asList(h1, h2, h3), h2);
		assertSame(h2, pi.getPlayerTurn());
		assertTrue(pi.hasNext());

		assertSame(h3, pi.next());
		assertSame(h3, pi.getPlayerTurn());

		assertSame(h1, pi.next());
		assertSame(h1, pi.getPlayerTurn());

		assertSame(h2, pi.next());
		assertSame(h2, pi.getPlayerTurn());

		assertSame(h3, pi.next());
		assertSame(h3, pi.getPlayerTurn());

		assertSame(h1, pi.next());
		assertSame(h1, pi.getPlayerTurn());

		assertEquals(3, pi.getPlayerHands().size());
	}
}
