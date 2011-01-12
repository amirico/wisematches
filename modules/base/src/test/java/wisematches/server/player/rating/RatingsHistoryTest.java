package wisematches.server.player.rating;

import org.junit.Test;
import wisematches.server.games.room.RatedBoardsInfo;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RatingsHistoryTest {
	@Test
	public void test_Iterator() {
		final long[] boardIds = {
				100, 101, 102, 103, 104, 105, 106
		};
		final long[] longs = {
				12, 13, 114, 15, 16, 17, 18
		};
		final int[] ints = {
				1, 2, 3, 4, 5, 6, 7
		};
		RatedBoardsInfo history = new RatedBoardsInfo(boardIds, longs, ints);

		final Iterator<RatedBoardsInfo.Record> recordIterator = history.iterator();
		for (int i = 0; i < 7; i++) {
			assertTrue(recordIterator.hasNext());

			final RatedBoardsInfo.Record ratingRecord = recordIterator.next();
			assertEquals(boardIds[i], ratingRecord.getBoardId());
			assertEquals(longs[i], ratingRecord.getTime());
			assertEquals(ints[i], ratingRecord.getRating());
		}

		assertFalse(recordIterator.hasNext());
		try {
			recordIterator.next();
			fail("Exception must be here: NoSuchElementException");
		} catch (NoSuchElementException ex) {
			;
		}
	}
}
