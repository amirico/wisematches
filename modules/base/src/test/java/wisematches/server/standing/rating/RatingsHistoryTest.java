package wisematches.server.standing.rating;

import org.junit.Test;
import wisematches.server.gameplaying.room.RatedBoardsInfo;

import java.sql.Timestamp;
import java.util.Date;
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
		final Date[] times = {
				new Timestamp(12), new Timestamp(13), new Timestamp(114),
				new Timestamp(15), new Timestamp(16), new Timestamp(17), new Timestamp(18)
		};
		final int[] ints = {
				1, 2, 3, 4, 5, 6, 7
		};
		RatedBoardsInfo history = new RatedBoardsInfo(boardIds, times, ints);

		final Iterator<RatedBoardsInfo.Record> recordIterator = history.iterator();
		for (int i = 0; i < 7; i++) {
			assertTrue(recordIterator.hasNext());

			final RatedBoardsInfo.Record ratingRecord = recordIterator.next();
			assertEquals(boardIds[i], ratingRecord.getBoardId());
			assertEquals(times[i], ratingRecord.getTime());
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
