package wisematches.playground.scribble.tracking.impl;

import org.junit.Before;
import org.junit.Test;
import wisematches.core.Player;
import wisematches.playground.GameMove;
import wisematches.playground.scribble.*;
import wisematches.playground.scribble.score.ScoreBonus;

import java.util.Arrays;
import java.util.Date;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleStatisticsTrapperTest {
	private ScribbleStatisticsTrapper movesStatistician;

	private final Player player = new MockPlayer(13L);

	public ScribbleStatisticsTrapperTest() {
	}

	@Before
	public void setUp() {
		movesStatistician = new ScribbleStatisticsTrapper();
	}

	@Test
	public void testUpdateMovesStatistic() throws Exception {
		final long moveTime = System.currentTimeMillis();

		final ScribbleStatisticsEditor editor = new ScribbleStatisticsEditor(player);

		final GameMove move1 = new MakeTurn(player, 20, new Date(moveTime), new Word(new Position(1, 2), Direction.HORIZONTAL, new Tile(1, 'A', 3), new Tile(1, 'B', 4)));
		final GameMove move2 = new MakeTurn(player, 10, new Date(moveTime + 3000), new Word(new Position(1, 2), Direction.HORIZONTAL, new Tile(1, 'A', 3), new Tile(1, 'B', 4), new Tile(1, 'C', 1), new Tile(1, 'D', 1)));
		final GameMove move3 = new ExchangeMove(player, 6, new Date(moveTime + 8000), new int[0]);
		final GameMove move4 = new PassTurn(player, 0, new Date(moveTime + 13000));

		final ScribbleBoard gb = createMock(ScribbleBoard.class);
		expect(gb.getGameMoves())
				.andReturn(Arrays.asList(new PassTurn(player, 0, new Date(moveTime - 1000)), move1))
				.andReturn(Arrays.asList(new PassTurn(player, 0, new Date(moveTime)), move2))
				.andReturn(Arrays.asList(new PassTurn(player, 0, new Date(moveTime + 3000)), move3))
				.andReturn(Arrays.asList(new PassTurn(player, 0, new Date(moveTime + 8000)), move4));
		replay(gb);

		final ScribbleMoveScore s1 = new ScribbleMoveScore((short) 20, false, ScoreBonus.Type.values(), "asd");
		final ScribbleMoveScore s2 = new ScribbleMoveScore((short) 10, true, ScoreBonus.Type.values(), "asd");
		final ScribbleMoveScore s3 = new ScribbleMoveScore((short) 5, true, ScoreBonus.Type.values(), "asd");

		movesStatistician.trapGameMoveDone(player, editor, gb, move1, s1);
		assertEquals(20, editor.getAveragePoints(), 0.000001);
		assertEquals(1000, editor.getAverageMoveTime(), 0.000001);
		assertEquals(2, editor.getAverageWordLength(), 0.000001);
		assertEquals(0, editor.getExchangesCount());
		assertEquals("AB", editor.getLastLongestWord().getText());
		assertEquals(moveTime, editor.getLastMoveTime().getTime());
		assertEquals("AB", editor.getLastValuableWord().getText());
		assertEquals(20, editor.getHighestPoints());
		assertEquals(20, editor.getLowestPoints());
		assertEquals(0, editor.getPassesCount());
		assertEquals(1, editor.getTurnsCount());
		assertEquals(1, editor.getWordsCount());
		assertEquals(0, editor.getAllHandTilesBonuses());

		movesStatistician.trapGameMoveDone(player, editor, gb, move2, s2);
		assertEquals(15, editor.getAveragePoints(), 0.000001);
		assertEquals((1000 + 3000) / 2, editor.getAverageMoveTime(), 0.000001);
		assertEquals(3, editor.getAverageWordLength(), 0.000001);
		assertEquals(0, editor.getExchangesCount());
		assertEquals("ABCD", editor.getLastLongestWord().getText());
		assertEquals(moveTime + 3000, editor.getLastMoveTime().getTime());
		assertEquals("AB", editor.getLastValuableWord().getText());
		assertEquals(20, editor.getHighestPoints());
		assertEquals(10, editor.getLowestPoints());
		assertEquals(0, editor.getPassesCount());
		assertEquals(2, editor.getTurnsCount());
		assertEquals(2, editor.getWordsCount());
		assertEquals(1, editor.getAllHandTilesBonuses());

		movesStatistician.trapGameMoveDone(player, editor, gb, move3, s3);
		assertEquals(12, editor.getAveragePoints(), 0.000001);
		assertEquals((1000 + 3000 + 5000) / 3, editor.getAverageMoveTime(), 0.000001);
		assertEquals(3, editor.getAverageWordLength(), 0.000001);
		assertEquals(1, editor.getExchangesCount());
		assertEquals("ABCD", editor.getLastLongestWord().getText());
		assertEquals(moveTime + 8000, editor.getLastMoveTime().getTime());
		assertEquals("AB", editor.getLastValuableWord().getText());
		assertEquals(20, editor.getHighestPoints());
		assertEquals(6, editor.getLowestPoints());
		assertEquals(0, editor.getPassesCount());
		assertEquals(3, editor.getTurnsCount());
		assertEquals(2, editor.getWordsCount());
		assertEquals(1, editor.getAllHandTilesBonuses());


		movesStatistician.trapGameMoveDone(player, editor, gb, move4, s3);
		assertEquals(9, editor.getAveragePoints(), 0.000001);
		assertEquals((1000 + 3000 + 5000 + 5000) / 4, editor.getAverageMoveTime(), 0.000001);
		assertEquals(3, editor.getAverageWordLength(), 0.000001);
		assertEquals(1, editor.getExchangesCount());
		assertEquals("ABCD", editor.getLastLongestWord().getText());
		assertEquals(moveTime + 13000, editor.getLastMoveTime().getTime());
		assertEquals("AB", editor.getLastValuableWord().getText());
		assertEquals(20, editor.getHighestPoints());
		assertEquals(6, editor.getLowestPoints());
		assertEquals(1, editor.getPassesCount());
		assertEquals(4, editor.getTurnsCount());
		assertEquals(2, editor.getWordsCount());
		assertEquals(1, editor.getAllHandTilesBonuses());

		verify(gb);
	}
}
