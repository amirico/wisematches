package wisematches.server.standing.statistic.impl.statistician;

import org.junit.Before;
import org.junit.Test;
import wisematches.server.playground.board.*;
import wisematches.server.standing.statistic.PlayerStatistic;
import wisematches.server.standing.statistic.statistician.MovesStatisticEditor;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@SuppressWarnings("unchecked")
public class DefaultMovesStatisticianTest {
	private DefaultMovesStatistician movesStatistician;

	public DefaultMovesStatisticianTest() {
	}

	@Before
	public void setUp() {
		movesStatistician = new DefaultMovesStatistician();
	}

	@Test
	public void testPreviousMoveTime() throws Exception {
		final long time = System.currentTimeMillis();

		final PlayerMove move1 = createNiceMock(PlayerMove.class);
		final PlayerMove move2 = createNiceMock(PlayerMove.class);
		replay(move1, move2);

		final GameBoard gb = createStrictMock(GameBoard.class);
		expect(gb.getGameMoves()).andReturn(Collections.emptyList());
		expect(gb.getStartedTime()).andReturn(new Date(time - 2000));
		expect(gb.getGameMoves()).andReturn(Arrays.asList(new GameMove(move1, 0, 0, new Date(time - 5000))));
		expect(gb.getStartedTime()).andReturn(new Date(time - 1000));
		expect(gb.getGameMoves()).andReturn(Arrays.asList(new GameMove(move2, 0, 0, new Date(time - 15000)), new GameMove(move2, 0, 0, new Date(time - 5000))));
		replay(gb);

		// No one move. Returns started time
		assertEquals(time - 2000, movesStatistician.previousMoveTime(gb).getTime());
		// One move - start time anyway
		assertEquals(time - 1000, movesStatistician.previousMoveTime(gb).getTime());
		// returns last move time
		assertEquals(time - 15000, movesStatistician.previousMoveTime(gb).getTime());
		verify(gb, move1, move2);
	}

	@Test
	public void testUpdateMovesStatistic() throws Exception {
		final long moveTime = System.currentTimeMillis();

		final PlayerStatistic playerStatistic = createMock(PlayerStatistic.class);
		final MovesStatisticEditor editor = new MovesStatisticEditor();

		final PlayerMove move1 = new MakeTurnMove(13L);
		final PlayerMove move2 = new MakeTurnMove(13L);
		final PlayerMove move3 = new PassTurnMove(13L);

		final GameBoard gb = createMock(GameBoard.class);
		expect(gb.getGameMoves())
				.andReturn(Arrays.asList(new GameMove(move1, 0, 0, new Date(moveTime - 1000)), null))
				.andReturn(Arrays.asList(new GameMove(move1, 0, 0, new Date(moveTime)), null))
				.andReturn(Arrays.asList(new GameMove(move1, 0, 0, new Date(moveTime + 3000)), null));
		replay(gb);

		movesStatistician.updateMovesStatistic(gb, new GameMove(move1, 10, 1, new Date(moveTime)), playerStatistic, editor);
		assertEquals(10, editor.getAvgPoints());
		assertEquals(1000, editor.getAverageTurnTime());
		assertEquals(0, editor.getAverageWordLength());
		assertEquals(0, editor.getExchangesCount());
		assertEquals(null, editor.getLastLongestWord());
		assertEquals(moveTime, editor.getLastMoveTime().getTime());
		assertEquals(null, editor.getLastValuableWord());
		assertEquals(10, editor.getMaxPoints());
		assertEquals(10, editor.getMinPoints());
		assertEquals(0, editor.getPassesCount());
		assertEquals(1, editor.getTurnsCount());
		assertEquals(0, editor.getWordsCount());

		movesStatistician.updateMovesStatistic(gb, new GameMove(move2, 20, 1, new Date(moveTime + 3000)), playerStatistic, editor);
		assertEquals(15, editor.getAvgPoints());
		assertEquals((1000 + 3000) / 2, editor.getAverageTurnTime());
		assertEquals(0, editor.getAverageWordLength());
		assertEquals(0, editor.getExchangesCount());
		assertEquals(null, editor.getLastLongestWord());
		assertEquals(moveTime + 3000, editor.getLastMoveTime().getTime());
		assertEquals(null, editor.getLastValuableWord());
		assertEquals(20, editor.getMaxPoints());
		assertEquals(10, editor.getMinPoints());
		assertEquals(0, editor.getPassesCount());
		assertEquals(2, editor.getTurnsCount());
		assertEquals(0, editor.getWordsCount());

		movesStatistician.updateMovesStatistic(gb, new GameMove(move3, 6, 1, new Date(moveTime + 8000)), playerStatistic, editor);
		assertEquals(12, editor.getAvgPoints());
		assertEquals((1000 + 3000 + 5000) / 3, editor.getAverageTurnTime());
		assertEquals(0, editor.getAverageWordLength());
		assertEquals(0, editor.getExchangesCount());
		assertEquals(null, editor.getLastLongestWord());
		assertEquals(moveTime + 8000, editor.getLastMoveTime().getTime());
		assertEquals(null, editor.getLastValuableWord());
		assertEquals(20, editor.getMaxPoints());
		assertEquals(6, editor.getMinPoints());
		assertEquals(1, editor.getPassesCount());
		assertEquals(3, editor.getTurnsCount());
		assertEquals(0, editor.getWordsCount());

		verify(gb);
	}
}
