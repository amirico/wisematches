package wisematches.playground.tracking.impl;

import org.junit.Test;
import wisematches.core.Personality;
import wisematches.core.Player;
import wisematches.core.personality.DefaultMember;
import wisematches.playground.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@SuppressWarnings("unchecked")
public class StatisticsTrapperTest {
	private StatisticsTrapper statisticsTrapper = new StatisticsTrapper<StatisticsEditor>();

	private static final Player player1 = new DefaultMember(901, null, null, null, null, null);
	private static final Player player2 = new DefaultMember(902, null, null, null, null, null);
	private static final Player player3 = new DefaultMember(903, null, null, null, null, null);

	public StatisticsTrapperTest() {
	}

	@Test
	public void testUpdateGamesStatistic1() throws Exception {
		final Player player = new DefaultMember(900, null, null, null, null, null);
		final StatisticsEditor editor = new StatisticsEditor();

		statisticsTrapper.trapGameStarted(player, editor);
		assertEquals(1, editor.getActiveGames());

		statisticsTrapper.trapGameStarted(player, editor);
		assertEquals(2, editor.getActiveGames());
	}

	@Test
	public void testUpdateGamesStatistic2() throws Exception {
		final StatisticsEditor editor = new StatisticsEditor();
		editor.setActiveGames(4);

		final GameBoard board = createMock(GameBoard.class);
		expect(board.getGameMoves()).andReturn(Arrays.<GameMove>asList(
				new MockMove(player1), //, 10, 1, new Date()
				new MockMove(player2), //, 20, 2, new Date()
				new MockMove(player1))); //, 6, 3, new Date()
		expect(board.isRated()).andReturn(true);
		expect(board.getPlayers()).andReturn(Arrays.<Personality>asList(player1, player2, player3));
		expect(board.getPlayerHand(player1)).andReturn(new MockPlayerHand((short) 20, (short) 1200, (short) 1300));
		expect(board.getPlayerHand(player2)).andReturn(new MockPlayerHand((short) -5, (short) 1250, (short) 1300));
		expect(board.getPlayerHand(player3)).andReturn(new MockPlayerHand((short) -10, (short) 1300, (short) 1220));
		expect(board.getResolution()).andReturn(GameResolution.RESIGNED);
		expect(board.getPlayerTurn()).andReturn(player1);
		expect(board.getWonPlayers()).andReturn(Collections.<Personality>emptyList());
		replay(board);

		statisticsTrapper.trapGameFinished(player1, editor, board);
		assertEquals(3, editor.getActiveGames());
		assertEquals(1, editor.getFinishedGames());
		assertEquals(2, editor.getAverageMovesPerGame(), 0.00000001);
		assertEquals(0, editor.getWins());
		assertEquals(1, editor.getDraws());
		assertEquals(0, editor.getLoses());
		assertEquals(1, editor.getResigned());
		assertEquals(0, editor.getTimeouts());
		assertEquals(0, editor.getStalemates());
		assertEquals(0, editor.getUnratedGames());
		verify(board);

		reset(board);
		expect(board.getGameMoves()).andReturn(Arrays.<GameMove>asList(
				new MockMove(player1),
				new MockMove(player2),
				new MockMove(player1)));
		expect(board.isRated()).andReturn(true);
		expect(board.getPlayers()).andReturn(Arrays.<Personality>asList(player1, player2, player3));
		expect(board.getPlayerHand(player1)).andReturn(new MockPlayerHand((short) 20, (short) 1200, (short) 1300));
		expect(board.getPlayerHand(player2)).andReturn(new MockPlayerHand((short) -5, (short) 1250, (short) 1300));
		expect(board.getPlayerHand(player3)).andReturn(new MockPlayerHand((short) -10, (short) 1300, (short) 1220));
		expect(board.getResolution()).andReturn(GameResolution.INTERRUPTED);
		expect(board.getPlayerTurn()).andReturn(player1);
		expect(board.getWonPlayers()).andReturn(Arrays.<Personality>asList(player2));
		replay(board);

		statisticsTrapper.trapGameFinished(player1, editor, board);
		assertEquals(2, editor.getActiveGames());
		assertEquals(2, editor.getFinishedGames());
		assertEquals(2, editor.getAverageMovesPerGame(), 0.00000001);
		assertEquals(0, editor.getWins());
		assertEquals(1, editor.getDraws());
		assertEquals(1, editor.getLoses());
		assertEquals(1, editor.getResigned());
		assertEquals(1, editor.getTimeouts());
		assertEquals(0, editor.getStalemates());
		assertEquals(0, editor.getUnratedGames());
		verify(board);

		reset(board);
		expect(board.getGameMoves()).andReturn(Arrays.<GameMove>asList(
				new MockMove(player1),
				new MockMove(player2),
				new MockMove(player1)));
		expect(board.isRated()).andReturn(true);
		expect(board.getPlayers()).andReturn(Arrays.<Personality>asList(player1, player2, player3));
		expect(board.getPlayerHand(player1)).andReturn(new MockPlayerHand((short) 20, (short) 1200, (short) 1300));
		expect(board.getPlayerHand(player2)).andReturn(new MockPlayerHand((short) -5, (short) 1250, (short) 1300));
		expect(board.getPlayerHand(player3)).andReturn(new MockPlayerHand((short) -10, (short) 1300, (short) 1220));
		expect(board.getResolution()).andReturn(GameResolution.STALEMATE);
		expect(board.getPlayerTurn()).andReturn(player1);
		expect(board.getWonPlayers()).andReturn(Arrays.<Personality>asList(player1));
		replay(board);

		statisticsTrapper.trapGameFinished(player1, editor, board);
		assertEquals(1, editor.getActiveGames());
		assertEquals(3, editor.getFinishedGames());
		assertEquals(2, editor.getAverageMovesPerGame(), 0.00000001);
		assertEquals(1, editor.getWins());
		assertEquals(1, editor.getDraws());
		assertEquals(1, editor.getLoses());
		assertEquals(1, editor.getResigned());
		assertEquals(1, editor.getTimeouts());
		assertEquals(1, editor.getStalemates());
		assertEquals(0, editor.getUnratedGames());
		verify(board);

		reset(board);
		expect(board.getGameMoves()).andReturn(Arrays.<GameMove>asList(
				new MockMove(player1),
				new MockMove(player2),
				new MockMove(player1)));
		expect(board.isRated()).andReturn(false);
		expect(board.getResolution()).andReturn(GameResolution.FINISHED);
		expect(board.getPlayerTurn()).andReturn(player1);
		replay(board);

		statisticsTrapper.trapGameFinished(player1, editor, board);
		assertEquals(0, editor.getActiveGames());
		assertEquals(4, editor.getFinishedGames());
		assertEquals(2, editor.getAverageMovesPerGame(), 0.00000001);
		assertEquals(1, editor.getWins());
		assertEquals(1, editor.getDraws());
		assertEquals(1, editor.getLoses());
		assertEquals(1, editor.getResigned());
		assertEquals(1, editor.getTimeouts());
		assertEquals(1, editor.getStalemates());
		assertEquals(1, editor.getUnratedGames());
		verify(board);
	}

	@Test
	public void testPreviousMoveTime() throws Exception {
		final long time = System.currentTimeMillis();

		final GameMove m1 = new MockMove(player1, 0, new Date(time - 5000));
		final GameMove m2 = new MockMove(player2, 0, new Date(time - 15000));
		final GameMove m3 = new MockMove(player1, 0, new Date(time - 5000));

		final GameBoard gb = createStrictMock(GameBoard.class);
		expect(gb.getGameMoves()).andReturn(Collections.<GameMove>emptyList());

		expect(gb.getStartedDate()).andReturn(new Date(time - 2000));
		expect(gb.getGameMoves()).andReturn(Arrays.asList(m1));

		expect(gb.getStartedDate()).andReturn(new Date(time - 1000));
		expect(gb.getGameMoves()).andReturn(Arrays.asList(m2, m3));
		replay(gb);

		// No one move. Returns started time
		assertEquals(time - 2000, statisticsTrapper.previousMoveTime(gb, null).getTime());
		// One move - start time anyway
		assertEquals(time - 1000, statisticsTrapper.previousMoveTime(gb, m1).getTime());
		// returns last move time
		assertEquals(time - 15000, statisticsTrapper.previousMoveTime(gb, m3).getTime());
		verify(gb);
	}

	@Test
	public void testUpdateMovesStatistic() throws Exception {
		final long moveTime = System.currentTimeMillis();

		final StatisticsEditor editor = new StatisticsEditor();

		final GameMove m1 = new MockMove(player1, 10, new Date(moveTime));
		final GameMove m2 = new MockMove(player1, 20, new Date(moveTime + 3000));
		final GameMove m3 = new MockMove(player1, 6, new Date(moveTime + 8000));

		final GameBoard gb = createMock(GameBoard.class);

		expect(gb.getGameMoves())
				.andReturn(Arrays.<GameMove>asList(new MockMove(player1, 0, new Date(moveTime - 1000)), m1))
				.andReturn(Arrays.<GameMove>asList(new MockMove(player1, 0, new Date(moveTime)), m2))
				.andReturn(Arrays.<GameMove>asList(new MockMove(player1, 0, new Date(moveTime + 3000)), m3));
		replay(gb);

		statisticsTrapper.trapGameMoveDone(player1, editor, gb, m1, null);
		assertEquals(10, editor.getAveragePoints(), 0.00000001);
		assertEquals(1000, editor.getAverageMoveTime(), 0.00000001);
		assertEquals(moveTime, editor.getLastMoveTime().getTime());
		assertEquals(10, editor.getHighestPoints());
		assertEquals(10, editor.getLowestPoints());
		assertEquals(1, editor.getTurnsCount());

		statisticsTrapper.trapGameMoveDone(player1, editor, gb, m2, null);
		assertEquals(15, editor.getAveragePoints(), 0.00000001);
		assertEquals((1000 + 3000) / 2, editor.getAverageMoveTime(), 0.00000001);
		assertEquals(moveTime + 3000, editor.getLastMoveTime().getTime());
		assertEquals(20, editor.getHighestPoints());
		assertEquals(10, editor.getLowestPoints());
		assertEquals(2, editor.getTurnsCount());

		statisticsTrapper.trapGameMoveDone(player1, editor, gb, m3, null);
		assertEquals(12, editor.getAveragePoints(), 0.00000001);
		assertEquals((1000 + 3000 + 5000) / 3, editor.getAverageMoveTime(), 0.00000001);
		assertEquals(moveTime + 8000, editor.getLastMoveTime().getTime());
		assertEquals(20, editor.getHighestPoints());
		assertEquals(6, editor.getLowestPoints());
		assertEquals(3, editor.getTurnsCount());

		verify(gb);
	}

	@Test
	public void testUpdateRatingsStatistic() throws Exception {
		final StatisticsEditor editor = new StatisticsEditor();

		final GameBoard board = createMock(GameBoard.class);
		{
			reset(board);
			expect(board.isRated()).andReturn(true);
			expect(board.getPlayers()).andReturn(Arrays.<Personality>asList(player1, player2, player3));
			expect(board.getPlayerHand(player1)).andReturn(new MockPlayerHand((short) 100, (short) 1000, (short) 991));
			expect(board.getPlayerHand(player2)).andReturn(new MockPlayerHand((short) 200, (short) 1400, (short) 1403));
			expect(board.getPlayerHand(player3)).andReturn(new MockPlayerHand((short) 300, (short) 1800, (short) 1812));
			expect(board.getGameMoves()).andReturn(Collections.<GameMove>emptyList());
			expect(board.getWonPlayers()).andReturn(Collections.<Personality>emptyList());
			expect(board.getResolution()).andReturn(GameResolution.FINISHED);
			expect(board.getPlayerTurn()).andReturn(null);
			replay(board);

			statisticsTrapper.trapGameFinished(player2, editor, board);
			assertEquals(1403, editor.getAverageRating(), 0.00000001);
			assertEquals((1000 + 1800) / 2, editor.getAverageOpponentRating(), 0.00000001);
			assertEquals(1403, editor.getHighestRating());
			assertEquals(1400, editor.getLowestRating());
			assertEquals(1000, editor.getHighestWonOpponentRating());
			assertEquals(901L, editor.getHighestWonOpponentId());
			assertEquals(1800, editor.getLowestLostOpponentRating());
			assertEquals(903L, editor.getLowestLostOpponentId());
			verify(board);
		}

		{
			reset(board);
			expect(board.isRated()).andReturn(true);
			expect(board.getPlayers()).andReturn(Arrays.<Personality>asList(player1, player2, player3));
			expect(board.getPlayerHand(player1)).andReturn(new MockPlayerHand((short) 200, (short) 1000, (short) 1011));
			expect(board.getPlayerHand(player2)).andReturn(new MockPlayerHand((short) 300, (short) 1400, (short) 1425));
			expect(board.getPlayerHand(player3)).andReturn(new MockPlayerHand((short) 100, (short) 1800, (short) 1782));
			expect(board.getGameMoves()).andReturn(Collections.<GameMove>emptyList());
			expect(board.getWonPlayers()).andReturn(Collections.<Personality>emptyList());
			expect(board.getResolution()).andReturn(GameResolution.FINISHED);
			expect(board.getPlayerTurn()).andReturn(null);
			replay(board);

			statisticsTrapper.trapGameFinished(player2, editor, board);
			assertEquals((1403 + 1425) / 2, editor.getAverageRating(), 0.00000001);
			assertEquals(((1000 + 1800) / 2 + (1000 + 1800) / 2) / 2, editor.getAverageOpponentRating(), 0.00000001);
			assertEquals(1425, editor.getHighestRating());
			assertEquals(1400, editor.getLowestRating());
			assertEquals(1800, editor.getHighestWonOpponentRating());
			assertEquals(903L, editor.getHighestWonOpponentId());
			assertEquals(1800, editor.getLowestLostOpponentRating());
			assertEquals(903L, editor.getLowestLostOpponentId());
			verify(board);
		}

		{
			reset(board);
			expect(board.isRated()).andReturn(true);
			expect(board.getPlayers()).andReturn(Arrays.<Personality>asList(player1, player2, player3));
			expect(board.getPlayerHand(player1)).andReturn(new MockPlayerHand((short) 300, (short) 1000, (short) 1025));
			expect(board.getPlayerHand(player2)).andReturn(new MockPlayerHand((short) 100, (short) 1400, (short) 1392));
			expect(board.getPlayerHand(player3)).andReturn(new MockPlayerHand((short) 200, (short) 1800, (short) 1782));
			expect(board.getGameMoves()).andReturn(Collections.<GameMove>emptyList());
			expect(board.getWonPlayers()).andReturn(Collections.<Personality>emptyList());
			expect(board.getResolution()).andReturn(GameResolution.FINISHED);
			expect(board.getPlayerTurn()).andReturn(null);
			replay(board);

			statisticsTrapper.trapGameFinished(player2, editor, board);
			assertEquals((1403 + 1425 + 1392) / 3f, editor.getAverageRating(), 0.00000001);
			assertEquals(((1000 + 1800) / 2 + (1000 + 1800) / 2 + (1000 + 1800) / 2) / 3f, editor.getAverageOpponentRating(), 0.00000001);
			assertEquals(1425, editor.getHighestRating());
			assertEquals(1392, editor.getLowestRating());
			assertEquals(1800, editor.getHighestWonOpponentRating());
			assertEquals(903L, editor.getHighestWonOpponentId());
			assertEquals(1000, editor.getLowestLostOpponentRating());
			assertEquals(901L, editor.getLowestLostOpponentId());
			verify(board);
		}
	}
}
