package wisematches.tracking.impl.statistician;

import org.junit.Before;
import org.junit.Test;
import wisematches.server.playground.board.GameBoard;
import wisematches.server.playground.board.GameResolution;
import wisematches.server.standing.rating.PlayerRatingManager;
import wisematches.server.standing.rating.RatingChange;
import wisematches.server.standing.statistic.GamesStatistic;
import wisematches.server.standing.statistic.PlayerStatistic;
import wisematches.server.standing.statistic.statistician.RatingsStatisticEditor;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.easymock.EasyMock.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultRatingsStatisticianTest {
	private DefaultRatingsStatistician ratingsStatistician;

	public DefaultRatingsStatisticianTest() {
	}

	@Before
	public void setUp() throws Exception {
		ratingsStatistician = new DefaultRatingsStatistician();
	}

	@Test
	public void testUpdateRatingsStatistic() throws Exception {
		final RatingsStatisticEditor editor = new RatingsStatisticEditor();

		final GameBoard board = createMock(GameBoard.class);

		final PlayerRatingManager manager = createMock(PlayerRatingManager.class);

		final GamesStatistic gamesStatistic = createMock(GamesStatistic.class);
		expect(gamesStatistic.getFinished()).andReturn(1).andReturn(2).andReturn(3);
		replay(gamesStatistic);

		final PlayerStatistic statistic = createMock(PlayerStatistic.class);
		expect(statistic.getPlayerId()).andReturn(14L).anyTimes();
		expect(statistic.getGamesStatistic()).andReturn(gamesStatistic).anyTimes();
		replay(statistic);

		ratingsStatistician.setRatingManager(manager);

		{
			final RatingChange change1 = new RatingChange(13L, 13L, new Date(), (short) 1000, (short) 991, (short) 100);
			final RatingChange change2 = new RatingChange(14L, 13L, new Date(), (short) 1400, (short) 1403, (short) 200);
			final RatingChange change3 = new RatingChange(15L, 13L, new Date(), (short) 1800, (short) 1812, (short) 300);

			reset(manager, board);
			expect(manager.getRatingChanges(board)).andReturn(Arrays.asList(change1, change2, change3));
			replay(manager, board);

			ratingsStatistician.updateRatingsStatistic(board, GameResolution.FINISHED, Collections.emptyList(), statistic, editor);
			assertEquals(1403, editor.getAverage());
			assertEquals((1000 + 1800) / 2, editor.getAverageOpponentRating());
			assertEquals(1403, editor.getHighest());
			assertEquals(1400, editor.getLowest());
			assertEquals(1000, editor.getHighestWonOpponentRating());
			assertEquals(13L, editor.getHighestWonOpponentId());
			assertEquals(1800, editor.getLowestLostOpponentRating());
			assertEquals(15L, editor.getLowestLostOpponentId());
			verify(manager, board);
		}

		{
			final RatingChange change1 = new RatingChange(13L, 13L, new Date(), (short) 1000, (short) 1011, (short) 200);
			final RatingChange change2 = new RatingChange(14L, 13L, new Date(), (short) 1400, (short) 1425, (short) 300);
			final RatingChange change3 = new RatingChange(15L, 13L, new Date(), (short) 1800, (short) 1782, (short) 100);

			reset(manager, board);
			expect(manager.getRatingChanges(board)).andReturn(Arrays.asList(change1, change2, change3));
			replay(manager, board);

			ratingsStatistician.updateRatingsStatistic(board, GameResolution.FINISHED, Collections.emptyList(), statistic, editor);
			assertEquals((1403 + 1425) / 2, editor.getAverage());
			assertEquals(((1000 + 1800) / 2 + (1000 + 1800) / 2) / 2, editor.getAverageOpponentRating());
			assertEquals(1425, editor.getHighest());
			assertEquals(1400, editor.getLowest());
			assertEquals(1800, editor.getHighestWonOpponentRating());
			assertEquals(15L, editor.getHighestWonOpponentId());
			assertEquals(1800, editor.getLowestLostOpponentRating());
			assertEquals(15L, editor.getLowestLostOpponentId());
			verify(manager, board);
		}

		{
			final RatingChange change1 = new RatingChange(13L, 13L, new Date(), (short) 1000, (short) 1025, (short) 300);
			final RatingChange change2 = new RatingChange(14L, 13L, new Date(), (short) 1400, (short) 1392, (short) 100);
			final RatingChange change3 = new RatingChange(15L, 13L, new Date(), (short) 1800, (short) 1782, (short) 200);

			reset(manager, board);
			expect(manager.getRatingChanges(board)).andReturn(Arrays.asList(change1, change2, change3));
			replay(manager, board);

			ratingsStatistician.updateRatingsStatistic(board, GameResolution.FINISHED, Collections.emptyList(), statistic, editor);
			assertEquals((1403 + 1425 + 1392) / 3, editor.getAverage());
			assertEquals(((1000 + 1800) / 2 + (1000 + 1800) / 2 + (1000 + 1800) / 2) / 3, editor.getAverageOpponentRating());
			assertEquals(1425, editor.getHighest());
			assertEquals(1392, editor.getLowest());
			assertEquals(1800, editor.getHighestWonOpponentRating());
			assertEquals(15L, editor.getHighestWonOpponentId());
			assertEquals(1000, editor.getLowestLostOpponentRating());
			assertEquals(13L, editor.getLowestLostOpponentId());
			verify(manager, board);
		}
	}
}
