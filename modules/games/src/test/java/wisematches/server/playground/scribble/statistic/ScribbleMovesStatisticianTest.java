package wisematches.server.playground.scribble.statistic;

import org.junit.Before;
import org.junit.Test;
import wisematches.server.playground.board.GameMove;
import wisematches.server.playground.board.PlayerMove;
import wisematches.server.playground.scribble.Direction;
import wisematches.server.playground.scribble.Position;
import wisematches.server.playground.scribble.Tile;
import wisematches.server.playground.scribble.Word;
import wisematches.server.playground.scribble.board.ExchangeTilesMove;
import wisematches.server.playground.scribble.board.MakeWordMove;
import wisematches.server.playground.scribble.board.ScribbleBoard;
import wisematches.server.standing.statistic.PlayerStatistic;

import java.util.Arrays;
import java.util.Date;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleMovesStatisticianTest {
	private ScribbleMovesStatistician movesStatistician;

	public ScribbleMovesStatisticianTest() {
	}

	@Before
	public void setUp() {
		movesStatistician = new ScribbleMovesStatistician();
	}

	@Test
	public void testUpdateMovesStatistic() throws Exception {
		final long moveTime = System.currentTimeMillis();

		final PlayerStatistic playerStatistic = createMock(PlayerStatistic.class);
		final ScribbleMovesStatisticEditor editor = new ScribbleMovesStatisticEditor();

		final PlayerMove move1 = new MakeWordMove(13L, new Word(new Position(1, 2), Direction.HORIZONTAL, new Tile(1, 'A', 3), new Tile(1, 'B', 4)));
		final PlayerMove move2 = new MakeWordMove(13L, new Word(new Position(1, 2), Direction.HORIZONTAL, new Tile(1, 'A', 3), new Tile(1, 'B', 4), new Tile(1, 'C', 1), new Tile(1, 'D', 1)));
		final PlayerMove move3 = new ExchangeTilesMove(13, new int[0]);

		final ScribbleBoard gb = createMock(ScribbleBoard.class);
		expect(gb.getGameMoves())
				.andReturn(Arrays.asList(new GameMove(move1, 20, 1, new Date(moveTime - 1000)), null))
				.andReturn(Arrays.asList(new GameMove(move1, 10, 2, new Date(moveTime)), null))
				.andReturn(Arrays.asList(new GameMove(move1, 6, 3, new Date(moveTime + 3000)), null));
		replay(gb);

		movesStatistician.updateMovesStatistic(gb, new GameMove(move1, 20, 1, new Date(moveTime)), playerStatistic, editor);
		assertEquals(20, editor.getAvgPoints());
		assertEquals(1000, editor.getAverageTurnTime());
		assertEquals(2, editor.getAverageWordLength());
		assertEquals(0, editor.getExchangesCount());
		assertEquals("AB", editor.getLastLongestWord());
		assertEquals(moveTime, editor.getLastMoveTime().getTime());
		assertEquals("AB", editor.getLastValuableWord());
		assertEquals(20, editor.getMaxPoints());
		assertEquals(20, editor.getMinPoints());
		assertEquals(0, editor.getPassesCount());
		assertEquals(1, editor.getTurnsCount());
		assertEquals(1, editor.getWordsCount());

		movesStatistician.updateMovesStatistic(gb, new GameMove(move2, 10, 1, new Date(moveTime + 3000)), playerStatistic, editor);
		assertEquals(15, editor.getAvgPoints());
		assertEquals((1000 + 3000) / 2, editor.getAverageTurnTime());
		assertEquals(3, editor.getAverageWordLength());
		assertEquals(0, editor.getExchangesCount());
		assertEquals("ABCD", editor.getLastLongestWord());
		assertEquals(moveTime + 3000, editor.getLastMoveTime().getTime());
		assertEquals("AB", editor.getLastValuableWord());
		assertEquals(20, editor.getMaxPoints());
		assertEquals(10, editor.getMinPoints());
		assertEquals(0, editor.getPassesCount());
		assertEquals(2, editor.getTurnsCount());
		assertEquals(2, editor.getWordsCount());

		movesStatistician.updateMovesStatistic(gb, new GameMove(move3, 6, 1, new Date(moveTime + 8000)), playerStatistic, editor);
		assertEquals(12, editor.getAvgPoints());
		assertEquals((1000 + 3000 + 5000) / 3, editor.getAverageTurnTime());
		assertEquals(3, editor.getAverageWordLength());
		assertEquals(1, editor.getExchangesCount());
		assertEquals("ABCD", editor.getLastLongestWord());
		assertEquals(moveTime + 8000, editor.getLastMoveTime().getTime());
		assertEquals("AB", editor.getLastValuableWord());
		assertEquals(20, editor.getMaxPoints());
		assertEquals(6, editor.getMinPoints());
		assertEquals(0, editor.getPassesCount());
		assertEquals(3, editor.getTurnsCount());
		assertEquals(2, editor.getWordsCount());

		verify(gb);
	}
}
