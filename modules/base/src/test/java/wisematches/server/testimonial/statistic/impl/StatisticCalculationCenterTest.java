package wisematches.server.testimonial.statistic.impl;

import org.easymock.IAnswer;
import org.junit.Before;
import org.junit.Test;
import wisematches.server.gameplaying.board.*;
import wisematches.server.gameplaying.room.*;
import wisematches.server.testimonial.statistic.PlayerRatingInfo;
import wisematches.server.testimonial.statistic.PlayerStatistic;

import java.util.Arrays;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class StatisticCalculationCenterTest {
	private StatisticCalculationCenter calculationCenter;

	private RoomBoardsListener roomBoardsListener;

	@Before
	public void init() {
		calculationCenter = new StatisticCalculationCenter();
	}

	@Test
	public void test_getPreviousMoveTime() {
		final long time = System.currentTimeMillis();

		final PlayerMove move1 = createNiceMock(PlayerMove.class);
		final PlayerMove move2 = createNiceMock(PlayerMove.class);
		replay(move1, move2);

		final GameBoard gb = createStrictMock(GameBoard.class);
		expect(gb.getGameMoves()).andReturn(Arrays.asList(new GameMove(move1, 0, 0, time - 5000)));
		expect(gb.getStartedTime()).andReturn(time - 1000);
		expect(gb.getGameMoves()).andReturn(Arrays.asList(new GameMove(move2, 0, 0, time - 15000), new GameMove(move2, 0, 0, time - 5000)));
		replay(gb);

		// No one move. Returns started time
		assertEquals(time - 1000, calculationCenter.getPreviousMoveTime(gb));
		// returns last move time
		assertEquals(time - 15000, calculationCenter.getPreviousMoveTime(gb));
		verify(gb, move1, move2);
	}

	@Test
	public void test_updateTurnsStatistic() {
		final PlayerStatistic statistic = new PlayerStatistic(12L);
		final long moveTime = System.currentTimeMillis();

		calculationCenter.updateTurnsStatistic(statistic, moveTime - 1000, moveTime);
		assertEquals(1, statistic.getTurnsCount());
		assertEquals(moveTime, statistic.getLastMoveTime());
		assertEquals(1000, statistic.getAverageTurnTime());

		calculationCenter.updateTurnsStatistic(statistic, moveTime, moveTime + 3000);
		assertEquals(2, statistic.getTurnsCount());
		assertEquals(moveTime + 3000, statistic.getLastMoveTime());
		assertEquals((1000 + 3000) / 2, statistic.getAverageTurnTime());

		calculationCenter.updateTurnsStatistic(statistic, moveTime + 3000, moveTime + 8000);
		assertEquals(3, statistic.getTurnsCount());
		assertEquals(moveTime + 8000, statistic.getLastMoveTime());
		assertEquals((1000 + 3000 + 5000) / 3, statistic.getAverageTurnTime());
	}

	@Test
	public void test_updateRatingInfo() {
		final PlayerStatistic statistic = new PlayerStatistic(12L);
		final PlayerRatingInfo ri = statistic.getAllGamesRaingInfo();

		{
			statistic.setLostGames(1);
			final GamePlayerHand hand1 = new GamePlayerHand(13L, 100, 1000, -9);
			final GamePlayerHand hand2 = new GamePlayerHand(14L, 200, 1400, 3);
			final GamePlayerHand hand3 = new GamePlayerHand(15L, 300, 1800, 12);

			final GameMove move1 = new GameMove(new MakeTurnMove(14L), 0, 0, 1);
			final GameMove move2 = new GameMove(new MakeTurnMove(15L), 0, 0, 1);
			final GameMove move3 = new GameMove(new MakeTurnMove(13L), 0, 0, 1);
			final GameMove move4 = new GameMove(new MakeTurnMove(14L), 0, 0, 1);
			final GameMove move5 = new GameMove(new MakeTurnMove(15L), 0, 0, 1);
			final GameMove move6 = new GameMove(new MakeTurnMove(13L), 0, 0, 1);

			final GameBoard board = createMock(GameBoard.class);
			expect(board.getPlayersHands()).andReturn(Arrays.asList(hand1, hand2, hand3)).anyTimes();
			expect(board.getGameMoves()).andReturn(Arrays.asList(move1, move2, move3, move4, move5, move6));
			replay(board);

			calculationCenter.updateRatingInfo(statistic, ri, board, hand2);
			assertEquals(1403, ri.getAverageRating());
			assertEquals(2, ri.getAverageMovesPerGame());
			assertEquals((1000 + 1800) / 2, ri.getAverageOpponentRating());
			assertEquals(1403, ri.getHighestRating());
			assertEquals(1400, ri.getLowestRating());
			assertEquals(1000, ri.getHighestWonOpponentRating());
			assertEquals(13L, ri.getHighestWonOpponentId());
			assertEquals(1800, ri.getLowestLostOpponentRating());
			assertEquals(15L, ri.getLowestLostOpponentId());
			verify(board);
		}

		{
			statistic.setWonGames(1);
			final GamePlayerHand hand1 = new GamePlayerHand(13L, 200, 1000, 11);
			final GamePlayerHand hand2 = new GamePlayerHand(14L, 300, 1400, 25);
			final GamePlayerHand hand3 = new GamePlayerHand(15L, 100, 1800, -18);

			final GameMove move1 = new GameMove(new MakeTurnMove(14L), 0, 0, 1);
			final GameMove move2 = new GameMove(new MakeTurnMove(14L), 0, 0, 1);
			final GameMove move3 = new GameMove(new MakeTurnMove(14L), 0, 0, 1);
			final GameMove move4 = new GameMove(new MakeTurnMove(14L), 0, 0, 1);

			final GameBoard board = createMock(GameBoard.class);
			expect(board.getPlayersHands()).andReturn(Arrays.asList(hand1, hand2, hand3)).anyTimes();
			expect(board.getGameMoves()).andReturn(Arrays.asList(move1, move2, move3, move4));
			replay(board);

			calculationCenter.updateRatingInfo(statistic, ri, board, hand2);
			assertEquals((1403 + 1425) / 2, ri.getAverageRating());
			assertEquals((2 + 4) / 2, ri.getAverageMovesPerGame());
			assertEquals(((1000 + 1800) / 2 + (1000 + 1800) / 2) / 2, ri.getAverageOpponentRating());
			assertEquals(1425, ri.getHighestRating());
			assertEquals(1400, ri.getLowestRating());
			assertEquals(1800, ri.getHighestWonOpponentRating());
			assertEquals(15L, ri.getHighestWonOpponentId());
			assertEquals(1800, ri.getLowestLostOpponentRating());
			assertEquals(15L, ri.getLowestLostOpponentId());
			verify(board);
		}

		{
			statistic.setLostGames(2);
			final GamePlayerHand hand1 = new GamePlayerHand(13L, 300, 1000, 25);
			final GamePlayerHand hand2 = new GamePlayerHand(14L, 100, 1400, -8);
			final GamePlayerHand hand3 = new GamePlayerHand(15L, 200, 1800, -18);

			final GameMove move1 = new GameMove(new MakeTurnMove(14L), 0, 0, 1);
			final GameMove move2 = new GameMove(new MakeTurnMove(14L), 0, 0, 1);
			final GameMove move3 = new GameMove(new MakeTurnMove(14L), 0, 0, 1);
			final GameMove move4 = new GameMove(new MakeTurnMove(14L), 0, 0, 1);

			final GameBoard board = createMock(GameBoard.class);
			expect(board.getPlayersHands()).andReturn(Arrays.asList(hand1, hand2, hand3)).anyTimes();
			expect(board.getGameMoves()).andReturn(Arrays.asList(move1, move2, move3, move4));
			replay(board);

			calculationCenter.updateRatingInfo(statistic, ri, board, hand2);
			assertEquals((1403 + 1425 + 1392) / 3, ri.getAverageRating());
			assertEquals((2 + 4 + 4) / 3, ri.getAverageMovesPerGame());
			assertEquals(((1000 + 1800) / 2 + (1000 + 1800) / 2 + (1000 + 1800) / 2) / 3, ri.getAverageOpponentRating());
			assertEquals(1425, ri.getHighestRating());
			assertEquals(1392, ri.getLowestRating());
			assertEquals(1800, ri.getHighestWonOpponentRating());
			assertEquals(15L, ri.getHighestWonOpponentId());
			assertEquals(1000, ri.getLowestLostOpponentRating());
			assertEquals(13L, ri.getLowestLostOpponentId());
			verify(board);
		}

	}

	@Test
	public void test_processGameStarted() {
		final PlayerStatistic s1 = new PlayerStatistic(13L);
		final PlayerStatistic s2 = new PlayerStatistic(14L);

		final HibernatePlayerStatisticsManager mi = createStrictMock(HibernatePlayerStatisticsManager.class);
		mi.lockPlayerStatistic(13L);
		expect(mi.getPlayerStatistic(13L)).andReturn(s1);
		mi.updatePlayerStatistic(s1);
		mi.unlockPlayerStatistic(13L);
		mi.lockPlayerStatistic(14L);
		expect(mi.getPlayerStatistic(14L)).andReturn(s2);
		mi.updatePlayerStatistic(s2);
		mi.unlockPlayerStatistic(14L);
		replay(mi);

		final GamePlayerHand hand1 = new GamePlayerHand(13L, 0, 1000, 25);
		final GamePlayerHand hand2 = new GamePlayerHand(14L, 0, 1400, -8);

		final GameBoard board = createMock(GameBoard.class);
		expect(board.getPlayersHands()).andReturn(Arrays.asList(hand1, hand2)).anyTimes();
		expect(board.getGameMoves()).andReturn(Arrays.asList()).anyTimes();
		replay(board);

		calculationCenter.setPlayerStatisticsManager(mi);
		calculationCenter.processGameStarted(board);

		assertEquals(1, s1.getActiveGames());
		assertEquals(1, s2.getActiveGames());

		verify(mi, board);
	}

	@Test
	public void test_processGameFinished_Rated() {
		final PlayerStatistic s1 = new PlayerStatistic(13L);
		final PlayerStatistic s2 = new PlayerStatistic(14L);

		final HibernatePlayerStatisticsManager mi = createStrictMock(HibernatePlayerStatisticsManager.class);
		mi.lockPlayerStatistic(13L);
		expect(mi.getPlayerStatistic(13L)).andReturn(s1);
		mi.updatePlayerStatistic(s1);
		mi.unlockPlayerStatistic(13L);
		mi.lockPlayerStatistic(14L);
		expect(mi.getPlayerStatistic(14L)).andReturn(s2);
		mi.updatePlayerStatistic(s2);
		mi.unlockPlayerStatistic(14L);
		replay(mi);

		final GamePlayerHand hand1 = new GamePlayerHand(13L, 300, 1000, 25);
		final GamePlayerHand hand2 = new GamePlayerHand(14L, 100, 1400, -8);

		final GameBoard board = createMock(GameBoard.class);
		expect(board.isRatedGame()).andReturn(true);
		expect(board.getPlayersHands()).andReturn(Arrays.asList(hand1, hand2)).anyTimes();
		expect(board.getGameMoves()).andReturn(Arrays.asList()).anyTimes();
		replay(board);

		calculationCenter.setPlayerStatisticsManager(mi);
		calculationCenter.processGameFinished(board, hand1);

		assertEquals(1, s1.getWonGames());
		assertEquals(1, s1.getFinishedGames());
		assertEquals(-1, s1.getActiveGames());

		assertEquals(1, s2.getLostGames());
		assertEquals(1, s2.getFinishedGames());
		assertEquals(-1, s2.getActiveGames());

		verify(mi, board);
	}

	@Test
	public void test_processGameDraw() {
		final PlayerStatistic s1 = new PlayerStatistic(13L);
		final PlayerStatistic s2 = new PlayerStatistic(14L);

		final HibernatePlayerStatisticsManager mi = createStrictMock(HibernatePlayerStatisticsManager.class);
		mi.lockPlayerStatistic(13L);
		expect(mi.getPlayerStatistic(13L)).andReturn(s1);
		mi.updatePlayerStatistic(s1);
		mi.unlockPlayerStatistic(13L);
		mi.lockPlayerStatistic(14L);
		expect(mi.getPlayerStatistic(14L)).andReturn(s2);
		mi.updatePlayerStatistic(s2);
		mi.unlockPlayerStatistic(14L);
		replay(mi);

		final GamePlayerHand hand1 = new GamePlayerHand(13L, 100, 1000, 25);
		final GamePlayerHand hand2 = new GamePlayerHand(14L, 100, 1400, -8);

		final GameBoard board = createMock(GameBoard.class);
		expect(board.isRatedGame()).andReturn(true);
		expect(board.getPlayersHands()).andReturn(Arrays.asList(hand1, hand2)).anyTimes();
		expect(board.getGameMoves()).andReturn(Arrays.asList()).anyTimes();
		replay(board);

		calculationCenter.setPlayerStatisticsManager(mi);
		calculationCenter.processGameDraw(board);

		assertEquals(1, s1.getDrawGames());
		assertEquals(1, s1.getFinishedGames());
		assertEquals(-1, s1.getActiveGames());

		assertEquals(1, s2.getDrawGames());
		assertEquals(1, s2.getFinishedGames());
		assertEquals(-1, s2.getActiveGames());
	}

	@Test
	public void test_processGameInterrupted() {
		final PlayerStatistic s1 = new PlayerStatistic(13L);
		final PlayerStatistic s2 = new PlayerStatistic(14L);

		final HibernatePlayerStatisticsManager mi = createStrictMock(HibernatePlayerStatisticsManager.class);
		mi.lockPlayerStatistic(13L);
		expect(mi.getPlayerStatistic(13L)).andReturn(s1);
		mi.unlockPlayerStatistic(13L);
		mi.lockPlayerStatistic(13L);
		expect(mi.getPlayerStatistic(13L)).andReturn(s1);
		mi.updatePlayerStatistic(s1);
		mi.unlockPlayerStatistic(13L);
		mi.lockPlayerStatistic(14L);
		expect(mi.getPlayerStatistic(14L)).andReturn(s2);
		mi.updatePlayerStatistic(s2);
		mi.unlockPlayerStatistic(14L);
		replay(mi);

		final GamePlayerHand hand1 = new GamePlayerHand(13L, 0, 1000, 25);
		final GamePlayerHand hand2 = new GamePlayerHand(14L, 100, 1400, -8);

		final GameBoard board = createMock(GameBoard.class);
		expect(board.isRatedGame()).andReturn(true).times(2);
		expect(board.getPlayersHands()).andReturn(Arrays.asList(hand1, hand2)).anyTimes();
		expect(board.getGameMoves()).andReturn(Arrays.asList()).anyTimes();
		expect(board.getWonPlayer()).andReturn(hand2);
		replay(board);

		calculationCenter.setPlayerStatisticsManager(mi);
		calculationCenter.processGameInterrupted(board, hand1, true);

		assertEquals(1, s1.getLostGames());
		assertEquals(1, s1.getTimeouts());
		assertEquals(1, s1.getFinishedGames());
		assertEquals(-1, s1.getActiveGames());

		assertEquals(1, s2.getWonGames());
		assertEquals(0, s2.getTimeouts());
		assertEquals(1, s2.getFinishedGames());
		assertEquals(-1, s2.getActiveGames());

		verify(mi, board);
	}

	/**
	 * If game is not rated count of active games should be decreased but other things should not be changed.
	 */
	@Test
	public void test_processGameFinished_NotRated() {
		final PlayerStatistic s1 = new PlayerStatistic(13L);
		final PlayerStatistic s2 = new PlayerStatistic(14L);

		final HibernatePlayerStatisticsManager mi = createStrictMock(HibernatePlayerStatisticsManager.class);
		for (int i = 0; i < 3; i++) {
			mi.lockPlayerStatistic(13L);
			expect(mi.getPlayerStatistic(13L)).andReturn(s1);
			mi.updatePlayerStatistic(s1);
			mi.unlockPlayerStatistic(13L);
			mi.lockPlayerStatistic(14L);
			expect(mi.getPlayerStatistic(14L)).andReturn(s2);
			mi.updatePlayerStatistic(s2);
			mi.unlockPlayerStatistic(14L);
		}
		replay(mi);

		final GamePlayerHand hand1 = new GamePlayerHand(13L, 300, 1000, 25);
		final GamePlayerHand hand2 = new GamePlayerHand(14L, 100, 1400, -8);

		final GameBoard board = createMock(GameBoard.class);
		expect(board.isRatedGame()).andReturn(false).times(4);
		expect(board.getPlayersHands()).andReturn(Arrays.asList(hand1, hand2)).times(3);
		expect(board.getWonPlayer()).andReturn(null);
		replay(board);

		calculationCenter.setPlayerStatisticsManager(mi);

		calculationCenter.processGameDraw(board);
		calculationCenter.processGameFinished(board, null);
		calculationCenter.processGameInterrupted(board, null, false);

		assertEquals(0, s1.getDrawGames());
		assertEquals(0, s1.getWonGames());
		assertEquals(0, s1.getLostGames());
		assertEquals(0, s1.getTimeouts());
		assertEquals(-3, s1.getActiveGames());

		assertEquals(0, s2.getDrawGames());
		assertEquals(0, s2.getWonGames());
		assertEquals(0, s2.getLostGames());
		assertEquals(0, s2.getTimeouts());
		assertEquals(-3, s2.getActiveGames());

		verify(mi, board);
	}

	@Test
	public void test_initializeCenter() throws BoardLoadingException {
		final Room room = Room.valueOf("MOCK");

		final GameBoard board = createStrictMock(GameBoard.class);
		board.addGameStateListener(isA(GameStateListener.class));
		board.addGameMoveListener(isA(GameMoveListener.class));
		replay(board);

		final GameBoard openedBoard = createStrictMock(GameBoard.class);
		openedBoard.addGameStateListener(isA(GameStateListener.class));
		openedBoard.addGameMoveListener(isA(GameMoveListener.class));
		replay(openedBoard);

		final RoomManager roomManager = createStrictMock(RoomManager.class);
		roomManager.addRoomBoardsListener(isA(RoomBoardsListener.class));
		expectLastCall().andAnswer(new IAnswer<Object>() {
			public Object answer() throws Throwable {
				roomBoardsListener = (RoomBoardsListener) getCurrentArguments()[0];
				return null;
			}
		});
		expect(roomManager.getOpenedBoards()).andReturn(Arrays.asList(openedBoard));
		expect(roomManager.openBoard(13L)).andReturn(board);
		replay(roomManager);

		final RoomsManager rm = createStrictMock(RoomsManager.class);
		expect(rm.getRoomManagers()).andReturn(Arrays.asList(roomManager));
		expect(rm.getRoomManager(room)).andReturn(roomManager);
		replay(rm);

		calculationCenter.setRoomsManager(rm);

		roomBoardsListener.boardOpened(room, 13L);

		verify(board, roomManager, rm, openedBoard);
	}
}
