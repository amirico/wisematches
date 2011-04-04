package wisematches.server.standing.statistic.impl;

import org.hibernate.SessionFactory;
import org.hibernate.persister.collection.AbstractCollectionPersister;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import wisematches.server.standing.statistic.PlayerStatisticRating;
import wisematches.server.standing.statistic.PlayerStatistic;
import wisematches.server.standing.statistic.PlayerStatisticListener;
import wisematches.server.standing.statistic.PlayerStatisticManager;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.easymock.EasyMock.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerStatisticManagerImplTest {
/*
	private SessionFactory sessionFactory;
	private PlayerStatisticManager playerStatisticManager;
	private StatisticCalculationCenter calculationCenter;

	private BoardListener boardListener;

	protected String[] getConfigLocations() {
		return new String[]{"classpath:/config/test-server-base-config.xml"};
	}

	@Override
	protected void onTearDownInTransaction() throws Exception {
		jdbcTemplate.update("delete from stats_info where playerId = 1");
		jdbcTemplate.update("delete from stats_info where playerId = 2");

		sessionFactory.getCurrentSession().clear();

		//Clear hibernate second level cache
		@SuppressWarnings("unchecked")
		final Map<String, EntityPersister> classMetadata = sessionFactory.getAllClassMetadata();
		for (EntityPersister ep : classMetadata.values()) {
			if (ep.hasCache()) {
//				sessionFactory.evictEntity(ep.getCache().getRegionName());
			}
		}

		@SuppressWarnings("unchecked")
		final Map<String, AbstractCollectionPersister> collMetadata = sessionFactory.getAllCollectionMetadata();
		for (AbstractCollectionPersister acp : collMetadata.values()) {
			if (acp.hasCache()) {
//				sessionFactory.evictCollection(acp.getCache().getRegionName());
			}
		}
	}

	public void test_lockUnlock() throws InterruptedException {
		final ExecutorService executorService = Executors.newFixedThreadPool(2);

		playerStatisticManager.lockPlayerStatistic(1L);
		final Future<PlayerStatistic> future = executorService.submit(new Callable<PlayerStatistic>() {
			public PlayerStatistic call() throws Exception {
				final TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
				try {
					final PlayerStatistic statistic = playerStatisticManager.getPlayerStatistic(1L);
					transactionManager.commit(status);
					return statistic;
				} catch (Exception th) {
					transactionManager.rollback(status);
					th.printStackTrace();
					throw th;
				}
			}
		});
		final Future<PlayerStatistic> future2 = executorService.submit(new Callable<PlayerStatistic>() {
			public PlayerStatistic call() throws Exception {
				final TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
				try {
					final PlayerStatistic statistic = playerStatisticManager.getPlayerStatistic(2L);
					transactionManager.commit(status);
					return statistic;
				} catch (Exception th) {
					transactionManager.rollback(status);
					th.printStackTrace();
					throw th;
				}
			}
		});
		Thread.sleep(1000);
		assertFalse(future.isDone());
		assertTrue(future2.isDone());

		final PlayerStatistic statistic = playerStatisticManager.getPlayerStatistic(1L);
		assertNotNull(statistic);
		playerStatisticManager.unlockPlayerStatistic(1L);

		Thread.sleep(1000);
		assertTrue(future.isDone());
	}

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
		expect(gb.getGameMoves()).andReturn(Arrays.asList(new GameMove(move1, 0, 0, new Date(time - 5000))));
		expect(gb.getStartedTime()).andReturn(new Date(time - 1000));
		expect(gb.getGameMoves()).andReturn(Arrays.asList(new GameMove(move2, 0, 0, new Date(time - 15000)), new GameMove(move2, 0, 0, new Date(time - 5000))));
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

		calculationCenter.updateTurnsStatistic(statistic, new Date(moveTime - 1000), new Date(moveTime));
		assertEquals(1, statistic.getTurnsCount());
		assertEquals(moveTime, statistic.getLastMoveTime());
		assertEquals(1000, statistic.getAverageTurnTime());

		calculationCenter.updateTurnsStatistic(statistic, new Date(moveTime), new Date(moveTime + 3000));
		assertEquals(2, statistic.getTurnsCount());
		assertEquals(moveTime + 3000, statistic.getLastMoveTime());
		assertEquals((1000 + 3000) / 2, statistic.getAverageTurnTime());

		calculationCenter.updateTurnsStatistic(statistic, new Date(moveTime + 3000), new Date(moveTime + 8000));
		assertEquals(3, statistic.getTurnsCount());
		assertEquals(moveTime + 8000, statistic.getLastMoveTime());
		assertEquals((1000 + 3000 + 5000) / 3, statistic.getAverageTurnTime());
	}

	@Test
	public void test_updateRatingInfo() {
		final PlayerStatistic statistic = new PlayerStatistic(12L);
		final PlayerStatisticRating ri = statistic.getAllGamesStatisticRating();

		{
			statistic.setLostGames(1);
			final GamePlayerHand hand1 = new GamePlayerHand(13L, 100, 1000, -9);
			final GamePlayerHand hand2 = new GamePlayerHand(14L, 200, 1400, 3);
			final GamePlayerHand hand3 = new GamePlayerHand(15L, 300, 1800, 12);

			final GameMove move1 = new GameMove(new MakeTurnMove(14L), 0, 0, new Date());
			final GameMove move2 = new GameMove(new MakeTurnMove(15L), 0, 0, new Date());
			final GameMove move3 = new GameMove(new MakeTurnMove(13L), 0, 0, new Date());
			final GameMove move4 = new GameMove(new MakeTurnMove(14L), 0, 0, new Date());
			final GameMove move5 = new GameMove(new MakeTurnMove(15L), 0, 0, new Date());
			final GameMove move6 = new GameMove(new MakeTurnMove(13L), 0, 0, new Date());

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

			final GameMove move1 = new GameMove(new MakeTurnMove(14L), 0, 0, new Date());
			final GameMove move2 = new GameMove(new MakeTurnMove(14L), 0, 0, new Date());
			final GameMove move3 = new GameMove(new MakeTurnMove(14L), 0, 0, new Date());
			final GameMove move4 = new GameMove(new MakeTurnMove(14L), 0, 0, new Date());

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

			final GameMove move1 = new GameMove(new MakeTurnMove(14L), 0, 0, new Date());
			final GameMove move2 = new GameMove(new MakeTurnMove(14L), 0, 0, new Date());
			final GameMove move3 = new GameMove(new MakeTurnMove(14L), 0, 0, new Date());
			final GameMove move4 = new GameMove(new MakeTurnMove(14L), 0, 0, new Date());

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

		final HibernatePlayerStatisticManager mi = createStrictMock(HibernatePlayerStatisticManager.class);
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

		calculationCenter.setPlayerStatisticManager(mi);
		calculationCenter.processGameStarted(board);

		assertEquals(1, s1.getActiveGames());
		assertEquals(1, s2.getActiveGames());

		verify(mi, board);
	}

	@Test
	public void test_processGameFinished_Rated() {
		final PlayerStatistic s1 = new PlayerStatistic(13L);
		final PlayerStatistic s2 = new PlayerStatistic(14L);

		final HibernatePlayerStatisticManager mi = createStrictMock(HibernatePlayerStatisticManager.class);
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

		calculationCenter.setPlayerStatisticManager(mi);
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

		final HibernatePlayerStatisticManager mi = createStrictMock(HibernatePlayerStatisticManager.class);
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

		calculationCenter.setPlayerStatisticManager(mi);
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

		final HibernatePlayerStatisticManager mi = createStrictMock(HibernatePlayerStatisticManager.class);
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
		expect(board.getWonPlayers()).andReturn(hand2);
		replay(board);

		calculationCenter.setPlayerStatisticManager(mi);
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

	@Test
	public void test_processGameFinished_NotRated() {
		final PlayerStatistic s1 = new PlayerStatistic(13L);
		final PlayerStatistic s2 = new PlayerStatistic(14L);

		final HibernatePlayerStatisticManager mi = createStrictMock(HibernatePlayerStatisticManager.class);
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
		expect(board.getWonPlayers()).andReturn(null);
		replay(board);

		calculationCenter.setPlayerStatisticManager(mi);

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
		board.addGameBoardListener(isA(GameBoardListener.class));
		replay(board);

		final GameBoard openedBoard = createStrictMock(GameBoard.class);
		openedBoard.addGameBoardListener(isA(GameBoardListener.class));
		replay(openedBoard);

		final RoomManager roomManager = createStrictMock(RoomManager.class);
		roomManager.addRoomBoardsListener(isA(BoardListener.class));
		expectLastCall().andAnswer(new IAnswer<Object>() {
			public Object answer() throws Throwable {
				boardListener = (BoardListener) getCurrentArguments()[0];
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

		boardListener.boardOpened(room, 13L);

		verify(board, roomManager, rm, openedBoard);
	}
*/
}
