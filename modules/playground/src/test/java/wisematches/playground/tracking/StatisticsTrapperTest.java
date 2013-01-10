package wisematches.playground.tracking;

import org.junit.Test;
import wisematches.personality.Personality;
import wisematches.playground.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@SuppressWarnings("unchecked")
public class StatisticsTrapperTest {
	private StatisticsTrapper statisticsTrapper = new StatisticsTrapper<StatisticsEditor>(StatisticsEditor.class) {
		@Override
		public StatisticsEditor createStatisticsEditor(Personality person) {
			return new StatisticsEditor(person);
		}
	};

	public StatisticsTrapperTest() {
	}

	@Test
	public void testUpdateGamesStatistic1() throws Exception {
		final StatisticsEditor editor = statisticsTrapper.createStatisticsEditor(Personality.person(13L));

		statisticsTrapper.trapGameStarted(editor);
		assertEquals(1, editor.getActiveGames());

		statisticsTrapper.trapGameStarted(editor);
		assertEquals(2, editor.getActiveGames());
	}

	@Test
	public void testUpdateGamesStatistic2() throws Exception {
		final StatisticsEditor editor = statisticsTrapper.createStatisticsEditor(Personality.person(13L));
		editor.setActiveGames(4);

		GameRatingChange rc1 = new GameRatingChange(13L, (short) 20, (short) 1200, (short) 1300);
		GameRatingChange rc2 = new GameRatingChange(14L, (short) -5, (short) 1250, (short) 1300);
		GameRatingChange rc3 = new GameRatingChange(15L, (short) -10, (short) 1300, (short) 1220);

		final List<GamePlayerHand> hands = Arrays.asList(
				new GamePlayerHand(13L, (short) 20),
				new GamePlayerHand(14L, (short) -5),
				new GamePlayerHand(15L, (short) -10));

		final PlayerMove move1 = new MakeTurnMove(13L);
		final PlayerMove move2 = new MakeTurnMove(14L);
		final PlayerMove move3 = new PassTurnMove(13L);

		final GameBoard board = createMock(GameBoard.class);
		expect(board.getGameMoves()).andReturn(Arrays.asList(
				new GameMove(move1, 10, 1, new Date()),
				new GameMove(move2, 20, 2, new Date()),
				new GameMove(move3, 6, 3, new Date())));
		expect(board.isRatedGame()).andReturn(true);
		expect(board.getPlayersHands()).andReturn(hands);
		expect(board.getPlayerHand(13L)).andReturn(hands.get(0));
		expect(board.getRatingChange(hands.get(0))).andReturn(rc1);
		expect(board.getRatingChanges()).andReturn(Arrays.asList(rc1, rc2, rc3));
		expect(board.getGameResolution()).andReturn(GameResolution.RESIGNED);
		expect(board.getPlayerTurn()).andReturn(new GamePlayerHand(editor.getPlayerId(), (short) 1));
		expect(board.getWonPlayers()).andReturn(Collections.emptyList());
		replay(board);

		statisticsTrapper.trapGameFinished(board, editor);
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
		expect(board.getGameMoves()).andReturn(Arrays.asList(
				new GameMove(move1, 10, 1, new Date()),
				new GameMove(move2, 20, 2, new Date()),
				new GameMove(move3, 6, 3, new Date())));
		expect(board.isRatedGame()).andReturn(true);
		expect(board.getPlayersHands()).andReturn(hands);
		expect(board.getPlayerHand(13L)).andReturn(hands.get(0));
		expect(board.getRatingChange(hands.get(0))).andReturn(rc1);
		expect(board.getRatingChanges()).andReturn(Arrays.asList(rc1, rc2, rc3));
		expect(board.getGameResolution()).andReturn(GameResolution.TIMEOUT);
		expect(board.getPlayerTurn()).andReturn(new GamePlayerHand(editor.getPlayerId(), (short) 1));
		expect(board.getWonPlayers()).andReturn(Arrays.asList(new GamePlayerHand(14L, (short) 1)));
		replay(board);

		statisticsTrapper.trapGameFinished(board, editor);
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
		expect(board.getGameMoves()).andReturn(Arrays.asList(
				new GameMove(move1, 10, 1, new Date()),
				new GameMove(move2, 20, 2, new Date()),
				new GameMove(move3, 6, 3, new Date())));
		expect(board.isRatedGame()).andReturn(true);
		expect(board.getPlayersHands()).andReturn(hands);
		expect(board.getPlayerHand(13L)).andReturn(hands.get(0));
		expect(board.getRatingChange(hands.get(0))).andReturn(rc1);
		expect(board.getRatingChanges()).andReturn(Arrays.asList(rc1, rc2, rc3));
		expect(board.getGameResolution()).andReturn(GameResolution.STALEMATE);
		expect(board.getPlayerTurn()).andReturn(new GamePlayerHand(editor.getPlayerId(), (short) 1));
		expect(board.getWonPlayers()).andReturn(Arrays.asList(new GamePlayerHand(13L, (short) 1)));
		replay(board);
		statisticsTrapper.trapGameFinished(board, editor);
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
		expect(board.getGameMoves()).andReturn(Arrays.asList(
				new GameMove(move1, 10, 1, new Date()),
				new GameMove(move2, 20, 2, new Date()),
				new GameMove(move3, 6, 3, new Date())));
		expect(board.isRatedGame()).andReturn(false);
		expect(board.getGameResolution()).andReturn(GameResolution.FINISHED);
		expect(board.getPlayerTurn()).andReturn(new GamePlayerHand(editor.getPlayerId(), (short) 1));
		replay(board);
		statisticsTrapper.trapGameFinished(board, editor);
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

		final PlayerMove move1 = createNiceMock(PlayerMove.class);
		final PlayerMove move2 = createNiceMock(PlayerMove.class);
		replay(move1, move2);

		final GameMove m1 = new GameMove(move1, 0, 0, new Date(time - 5000));
		final GameMove m2 = new GameMove(move2, 0, 0, new Date(time - 15000));
		final GameMove m3 = new GameMove(move2, 0, 0, new Date(time - 5000));

		final GameBoard gb = createStrictMock(GameBoard.class);
		expect(gb.getGameMoves()).andReturn(Collections.<GameMove>emptyList());

		expect(gb.getStartedTime()).andReturn(new Date(time - 2000));
		expect(gb.getGameMoves()).andReturn(Arrays.asList(m1));

		expect(gb.getStartedTime()).andReturn(new Date(time - 1000));
		expect(gb.getGameMoves()).andReturn(Arrays.asList(m2, m3));
		replay(gb);

		// No one move. Returns started time
		assertEquals(time - 2000, statisticsTrapper.previousMoveTime(gb, null).getTime());
		// One move - start time anyway
		assertEquals(time - 1000, statisticsTrapper.previousMoveTime(gb, m1).getTime());
		// returns last move time
		assertEquals(time - 15000, statisticsTrapper.previousMoveTime(gb, m3).getTime());
		verify(gb, move1, move2);
	}

	@Test
	public void testUpdateMovesStatistic() throws Exception {
		final long moveTime = System.currentTimeMillis();

		final StatisticsEditor editor = statisticsTrapper.createStatisticsEditor(Personality.person(13L));

		final PlayerMove move1 = new MakeTurnMove(13L);
		final PlayerMove move2 = new MakeTurnMove(13L);
		final PlayerMove move3 = new PassTurnMove(13L);

		final GameMove m1 = new GameMove(move1, 10, 1, new Date(moveTime));
		final GameMove m2 = new GameMove(move2, 20, 1, new Date(moveTime + 3000));
		final GameMove m3 = new GameMove(move3, 6, 1, new Date(moveTime + 8000));

		final GameBoard gb = createMock(GameBoard.class);
		expect(gb.getGameMoves())
				.andReturn(Arrays.asList(new GameMove(move1, 0, 0, new Date(moveTime - 1000)), m1))
				.andReturn(Arrays.asList(new GameMove(move1, 0, 0, new Date(moveTime)), m2))
				.andReturn(Arrays.asList(new GameMove(move1, 0, 0, new Date(moveTime + 3000)), m3));
		replay(gb);

		statisticsTrapper.trapGameMoveDone(gb, m1, null, editor);
		assertEquals(10, editor.getAveragePoints(), 0.00000001);
		assertEquals(1000, editor.getAverageMoveTime(), 0.00000001);
		assertEquals(moveTime, editor.getLastMoveTime().getTime());
		assertEquals(10, editor.getHighestPoints());
		assertEquals(10, editor.getLowestPoints());
		assertEquals(0, editor.getPassesCount());
		assertEquals(1, editor.getTurnsCount());

		statisticsTrapper.trapGameMoveDone(gb, m2, null, editor);
		assertEquals(15, editor.getAveragePoints(), 0.00000001);
		assertEquals((1000 + 3000) / 2, editor.getAverageMoveTime(), 0.00000001);
		assertEquals(moveTime + 3000, editor.getLastMoveTime().getTime());
		assertEquals(20, editor.getHighestPoints());
		assertEquals(10, editor.getLowestPoints());
		assertEquals(0, editor.getPassesCount());
		assertEquals(2, editor.getTurnsCount());

		statisticsTrapper.trapGameMoveDone(gb, m3, null, editor);
		assertEquals(12, editor.getAveragePoints(), 0.00000001);
		assertEquals((1000 + 3000 + 5000) / 3, editor.getAverageMoveTime(), 0.00000001);
		assertEquals(moveTime + 8000, editor.getLastMoveTime().getTime());
		assertEquals(20, editor.getHighestPoints());
		assertEquals(6, editor.getLowestPoints());
		assertEquals(1, editor.getPassesCount());
		assertEquals(3, editor.getTurnsCount());

		verify(gb);
	}

	@Test
	public void testUpdateRatingsStatistic() throws Exception {
		final StatisticsEditor editor = statisticsTrapper.createStatisticsEditor(Personality.person(14L));

		List<GamePlayerHand> gamePlayerHands = Arrays.asList(
				new GamePlayerHand(13L, (short) 12),
				new GamePlayerHand(14L, (short) 12),
				new GamePlayerHand(15L, (short) 12));

		final GameBoard board = createMock(GameBoard.class);
		{
			final GameRatingChange change1 = new GameRatingChange(13L, (short) 100, (short) 1000, (short) 991);
			final GameRatingChange change2 = new GameRatingChange(14L, (short) 200, (short) 1400, (short) 1403);
			final GameRatingChange change3 = new GameRatingChange(15L, (short) 300, (short) 1800, (short) 1812);

			reset(board);
			expect(board.isRatedGame()).andReturn(true);
			expect(board.getPlayersHands()).andReturn(gamePlayerHands);
			expect(board.getPlayerHand(14L)).andReturn(gamePlayerHands.get(1));
			expect(board.getRatingChange(gamePlayerHands.get(1))).andReturn(change2);
			expect(board.getRatingChanges()).andReturn(Arrays.asList(change1, change2, change3));
			expect(board.getGameMoves()).andReturn(Collections.<GameMove>emptyList());
			expect(board.getWonPlayers()).andReturn(Collections.emptyList());
			expect(board.getGameResolution()).andReturn(GameResolution.FINISHED);
			expect(board.getPlayerTurn()).andReturn(null);
			replay(board);

			statisticsTrapper.trapGameFinished(board, editor);
			assertEquals(1403, editor.getAverageRating(), 0.00000001);
			assertEquals((1000 + 1800) / 2, editor.getAverageOpponentRating(), 0.00000001);
			assertEquals(1403, editor.getHighestRating());
			assertEquals(1400, editor.getLowestRating());
			assertEquals(1000, editor.getHighestWonOpponentRating());
			assertEquals(13L, editor.getHighestWonOpponentId());
			assertEquals(1800, editor.getLowestLostOpponentRating());
			assertEquals(15L, editor.getLowestLostOpponentId());
			verify(board);
		}

		{
			final GameRatingChange change1 = new GameRatingChange(13L, (short) 200, (short) 1000, (short) 1011);
			final GameRatingChange change2 = new GameRatingChange(14L, (short) 300, (short) 1400, (short) 1425);
			final GameRatingChange change3 = new GameRatingChange(15L, (short) 100, (short) 1800, (short) 1782);

			reset(board);
			expect(board.isRatedGame()).andReturn(true);
			expect(board.getPlayersHands()).andReturn(gamePlayerHands);
			expect(board.getPlayerHand(14L)).andReturn(gamePlayerHands.get(1));
			expect(board.getRatingChange(gamePlayerHands.get(1))).andReturn(change2);
			expect(board.getRatingChanges()).andReturn(Arrays.asList(change1, change2, change3));
			expect(board.getGameMoves()).andReturn(Collections.<GameMove>emptyList());
			expect(board.getWonPlayers()).andReturn(Collections.emptyList());
			expect(board.getGameResolution()).andReturn(GameResolution.FINISHED);
			expect(board.getPlayerTurn()).andReturn(null);
			replay(board);

			statisticsTrapper.trapGameFinished(board, editor);
			assertEquals((1403 + 1425) / 2, editor.getAverageRating(), 0.00000001);
			assertEquals(((1000 + 1800) / 2 + (1000 + 1800) / 2) / 2, editor.getAverageOpponentRating(), 0.00000001);
			assertEquals(1425, editor.getHighestRating());
			assertEquals(1400, editor.getLowestRating());
			assertEquals(1800, editor.getHighestWonOpponentRating());
			assertEquals(15L, editor.getHighestWonOpponentId());
			assertEquals(1800, editor.getLowestLostOpponentRating());
			assertEquals(15L, editor.getLowestLostOpponentId());
			verify(board);
		}

		{
			final GameRatingChange change1 = new GameRatingChange(13L, (short) 300, (short) 1000, (short) 1025);
			final GameRatingChange change2 = new GameRatingChange(14L, (short) 100, (short) 1400, (short) 1392);
			final GameRatingChange change3 = new GameRatingChange(15L, (short) 200, (short) 1800, (short) 1782);

			reset(board);
			expect(board.isRatedGame()).andReturn(true);
			expect(board.getPlayersHands()).andReturn(gamePlayerHands);
			expect(board.getPlayerHand(14L)).andReturn(gamePlayerHands.get(1));
			expect(board.getRatingChange(gamePlayerHands.get(1))).andReturn(change2);
			expect(board.getRatingChanges()).andReturn(Arrays.asList(change1, change2, change3));
			expect(board.getGameMoves()).andReturn(Collections.<GameMove>emptyList());
			expect(board.getWonPlayers()).andReturn(Collections.emptyList());
			expect(board.getGameResolution()).andReturn(GameResolution.FINISHED);
			expect(board.getPlayerTurn()).andReturn(null);
			replay(board);

			statisticsTrapper.trapGameFinished(board, editor);
			assertEquals((1403 + 1425 + 1392) / 3f, editor.getAverageRating(), 0.00000001);
			assertEquals(((1000 + 1800) / 2 + (1000 + 1800) / 2 + (1000 + 1800) / 2) / 3f, editor.getAverageOpponentRating(), 0.00000001);
			assertEquals(1425, editor.getHighestRating());
			assertEquals(1392, editor.getLowestRating());
			assertEquals(1800, editor.getHighestWonOpponentRating());
			assertEquals(15L, editor.getHighestWonOpponentId());
			assertEquals(1000, editor.getLowestLostOpponentRating());
			assertEquals(13L, editor.getLowestLostOpponentId());
			verify(board);
		}
	}
}
