package wisematches.server.standing.statistic.impl.statistician;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import wisematches.server.playground.board.*;
import wisematches.server.standing.statistic.PlayerStatistic;
import wisematches.server.standing.statistic.statistician.GamesStatisticEditor;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@SuppressWarnings("unchecked")
public class DefaultGamesStatisticianTest {
	private DefaultGamesStatistician gamesStatistician;

	public DefaultGamesStatisticianTest() {
	}

	@Before
	public void setUp() throws Exception {
		gamesStatistician = new DefaultGamesStatistician();
	}

	@Test
	public void testUpdateGamesStatistic1() throws Exception {
		final GamesStatisticEditor editor = new GamesStatisticEditor();

		gamesStatistician.updateGamesStatistic(null, null, editor);
		assertEquals(1, editor.getActive());

		gamesStatistician.updateGamesStatistic(null, null, editor);
		assertEquals(2, editor.getActive());
	}

	@Test
	public void testUpdateGamesStatistic2() throws Exception {
		final GamesStatisticEditor editor = new GamesStatisticEditor();
		editor.setActive(4);

		final PlayerMove move1 = new MakeTurnMove(13L);
		final PlayerMove move2 = new MakeTurnMove(14L);
		final PlayerMove move3 = new PassTurnMove(13L);

		final GameBoard board = createMock(GameBoard.class);
		expect(board.getGameMoves()).andReturn(Arrays.asList(
				new GameMove(move1, 10, 1, new Date()),
				new GameMove(move2, 20, 2, new Date()),
				new GameMove(move3, 6, 3, new Date()))).times(4);
		expect(board.isRatedGame()).andReturn(true).times(3).andReturn(false);
		replay(board);

		final PlayerStatistic statistic = createMock(PlayerStatistic.class);
		expect(statistic.getPlayerId()).andReturn(13L).anyTimes();
		replay(statistic);

		gamesStatistician.updateGamesStatistic(board, GameResolution.TIMEOUT, Collections.emptyList(), statistic, editor);
		assertEquals(3, editor.getActive());
		assertEquals(1, editor.getFinished());
		assertEquals(2, editor.getAverageMovesPerGame());
		assertEquals(0, editor.getWins());
		assertEquals(1, editor.getDraws());
		assertEquals(0, editor.getLoses());
		assertEquals(1, editor.getTimeouts());
		assertEquals(0, editor.getUnrated());

		gamesStatistician.updateGamesStatistic(board, GameResolution.FINISHED, Arrays.asList(new GamePlayerHand(14L, (short) 1)), statistic, editor);
		assertEquals(2, editor.getActive());
		assertEquals(2, editor.getFinished());
		assertEquals(2, editor.getAverageMovesPerGame());
		assertEquals(0, editor.getWins());
		assertEquals(1, editor.getDraws());
		assertEquals(1, editor.getLoses());
		assertEquals(1, editor.getTimeouts());
		assertEquals(0, editor.getUnrated());

		gamesStatistician.updateGamesStatistic(board, GameResolution.FINISHED, Arrays.asList(new GamePlayerHand(13L, (short) 1)), statistic, editor);
		assertEquals(1, editor.getActive());
		assertEquals(3, editor.getFinished());
		assertEquals(2, editor.getAverageMovesPerGame());
		assertEquals(1, editor.getWins());
		assertEquals(1, editor.getDraws());
		assertEquals(1, editor.getLoses());
		assertEquals(1, editor.getTimeouts());
		assertEquals(0, editor.getUnrated());

		gamesStatistician.updateGamesStatistic(board, GameResolution.FINISHED, Arrays.asList(new GamePlayerHand(13L, (short) 1)), statistic, editor);
		assertEquals(0, editor.getActive());
		assertEquals(4, editor.getFinished());
		assertEquals(2, editor.getAverageMovesPerGame());
		assertEquals(1, editor.getWins());
		assertEquals(1, editor.getDraws());
		assertEquals(1, editor.getLoses());
		assertEquals(1, editor.getTimeouts());
		assertEquals(1, editor.getUnrated());

		verify(statistic, board);
	}
}
