package wisematches.server.standing.statistic.impl;

import org.easymock.Capture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import wisematches.server.gameplaying.board.*;
import wisematches.server.gameplaying.room.RoomManager;
import wisematches.server.gameplaying.room.RoomsManager;
import wisematches.server.gameplaying.room.board.BoardManager;
import wisematches.server.gameplaying.room.board.BoardStateListener;
import wisematches.server.personality.Personality;
import wisematches.server.personality.account.UnknownAccountException;
import wisematches.server.personality.player.computer.guest.GuestPlayer;
import wisematches.server.standing.rating.PlayerRatingManager;
import wisematches.server.standing.rating.RatingChange;
import wisematches.server.standing.statistic.PlayerStatistic;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerStatisticManagerImplTest {
	private BoardStateListener boardStateListener;
	private PlayerStatisticDao playerStatisticDao;
	private PlayerRatingManager playerRatingManager;
	private PlayerStatisticManagerImpl playerStatisticManager;

	public PlayerStatisticManagerImplTest() {
	}

	@Before
	public void init() {
		final Capture<BoardStateListener> capture = new Capture<BoardStateListener>();

		final BoardManager boardManager = createMock(BoardManager.class);
		boardManager.addBoardStateListener(capture(capture));
		boardManager.removeBoardStateListener(capture(capture));
		replay(boardManager);

		final RoomManager roomManager = createMock(RoomManager.class);
		expect(roomManager.getBoardManager()).andReturn(boardManager).anyTimes();
		replay(roomManager);

		final RoomsManager roomsManager = createMock(RoomsManager.class);
		expect(roomsManager.getRoomManagers()).andReturn(Arrays.asList(roomManager)).anyTimes();
		replay(roomsManager);

		playerStatisticDao = createMock(PlayerStatisticDao.class);

		playerRatingManager = createMock(PlayerRatingManager.class);

		playerStatisticManager = new PlayerStatisticManagerImpl();
		playerStatisticManager.setPlayerStatisticDao(playerStatisticDao);
		playerStatisticManager.setPlayerRatingManager(playerRatingManager);
		playerStatisticManager.setRoomsManager(roomsManager);

		boardStateListener = capture.getValue();
	}

	@After
	public void removeUser() throws UnknownAccountException {
		playerStatisticManager.setRoomsManager(null);
	}

	@Test
	public void test_lockUnlock() throws InterruptedException {
		final ExecutorService executorService = Executors.newFixedThreadPool(2);

		final Personality p1_1 = Personality.person(1L);
		final Personality p1_2 = Personality.person(1L);
		final Personality p2 = Personality.person(2L);

		final HibernatePlayerStatistic ps1_1 = new HibernatePlayerStatistic(p1_1);
		final HibernatePlayerStatistic ps1_2 = new HibernatePlayerStatistic(p1_2);
		final HibernatePlayerStatistic ps2 = new HibernatePlayerStatistic(p2);

		expect(playerStatisticDao.loadPlayerStatistic(p1_1)).andReturn(ps1_1);
		expect(playerStatisticDao.loadPlayerStatistic(p1_1)).andReturn(ps1_2);
		expect(playerStatisticDao.loadPlayerStatistic(p1_1)).andReturn(ps2);
		replay(playerStatisticDao);

		playerStatisticManager.lock(p1_1);
		final Future<PlayerStatistic> future = executorService.submit(new Callable<PlayerStatistic>() {
			public PlayerStatistic call() throws Exception {
				return playerStatisticManager.getPlayerStatistic(p1_2);
			}
		});
		final Future<PlayerStatistic> future2 = executorService.submit(new Callable<PlayerStatistic>() {
			public PlayerStatistic call() throws Exception {
				return playerStatisticManager.getPlayerStatistic(p2);
			}
		});
		Thread.sleep(300);
		assertFalse(future.isDone());
		assertTrue(future2.isDone());

		final PlayerStatistic statistic = playerStatisticManager.getPlayerStatistic(p1_2);
		assertNotNull(statistic);
		playerStatisticManager.unlock(p1_1);

		Thread.sleep(300);
		assertTrue(future.isDone());
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
		assertEquals(time - 1000, playerStatisticManager.previousMoveTime(gb).getTime());
		// returns last move time
		assertEquals(time - 15000, playerStatisticManager.previousMoveTime(gb).getTime());
		verify(gb, move1, move2);
	}

	@Test
	public void test_updateTurnsStatistic() {
		final long moveTime = System.currentTimeMillis();

		final PlayerMove move1 = new MakeTurnMove(13L);

		final GameBoard gb = createMock(GameBoard.class);
		expect(gb.getGameMoves())
				.andReturn(Arrays.asList(new GameMove(move1, 0, 0, new Date(moveTime - 1000)), null))
				.andReturn(Arrays.asList(new GameMove(move1, 0, 0, new Date(moveTime)), null))
				.andReturn(Arrays.asList(new GameMove(move1, 0, 0, new Date(moveTime + 3000)), null));
		replay(gb);

		final HibernatePlayerStatistic statistic = new HibernatePlayerStatistic(Personality.person(13L));

		expect(playerStatisticDao.loadPlayerStatistic(Personality.person(13L))).andReturn(statistic).times(3);
		playerStatisticDao.savePlayerStatistic(Personality.person(13L), statistic);
		expectLastCall().times(3);
		replay(playerStatisticDao);

		playerStatisticManager.processPlayerMoved(gb, new GameMove(move1, 10, 1, new Date(moveTime)));
		assertEquals(1, statistic.getTurnsCount());
		assertEquals(moveTime, statistic.getLastMoveTime().getTime());
		assertEquals(1000, statistic.getAverageTurnTime());

		playerStatisticManager.processPlayerMoved(gb, new GameMove(move1, 10, 1, new Date(moveTime + 3000)));
		assertEquals(2, statistic.getTurnsCount());
		assertEquals(moveTime + 3000, statistic.getLastMoveTime().getTime());
		assertEquals((1000 + 3000) / 2, statistic.getAverageTurnTime());

		playerStatisticManager.processPlayerMoved(gb, new GameMove(move1, 10, 1, new Date(moveTime + 8000)));
		assertEquals(3, statistic.getTurnsCount());
		assertEquals(moveTime + 8000, statistic.getLastMoveTime().getTime());
		assertEquals((1000 + 3000 + 5000) / 3, statistic.getAverageTurnTime());

		verify(gb, playerStatisticDao);
	}

	@Test
	public void test_updateRatingInfo() {
		final HibernatePlayerStatistic statistic = new HibernatePlayerStatistic(Personality.person(14L));
		final HibernatePlayerStatisticRating ri = statistic.getAllGamesStatisticRating();

		{
			statistic.incrementLostGames();
			final RatingChange change1 = new RatingChange(13L, 13L, new Date(), (short) 1000, (short) 991, (short) 100);
			final RatingChange change2 = new RatingChange(14L, 13L, new Date(), (short) 1400, (short) 1403, (short) 200);
			final RatingChange change3 = new RatingChange(15L, 13L, new Date(), (short) 1800, (short) 1812, (short) 300);

			final GameMove move1 = new GameMove(new MakeTurnMove(14L), 0, 0, new Date());
			final GameMove move2 = new GameMove(new MakeTurnMove(15L), 0, 0, new Date());
			final GameMove move3 = new GameMove(new MakeTurnMove(13L), 0, 0, new Date());
			final GameMove move4 = new GameMove(new MakeTurnMove(14L), 0, 0, new Date());
			final GameMove move5 = new GameMove(new MakeTurnMove(15L), 0, 0, new Date());
			final GameMove move6 = new GameMove(new MakeTurnMove(13L), 0, 0, new Date());

			final GameBoard board = createMock(GameBoard.class);
			expect(board.getGameMoves()).andReturn(Arrays.asList(move1, move2, move3, move4, move5, move6));
			replay(board);

			playerStatisticManager.updateRatingInfo(board, statistic, ri, Arrays.asList(change1, change2, change3));
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
			statistic.incrementWonGames();
			final RatingChange change1 = new RatingChange(13L, 13L, new Date(), (short) 1000, (short) 1011, (short) 200);
			final RatingChange change2 = new RatingChange(14L, 13L, new Date(), (short) 1400, (short) 1425, (short) 300);
			final RatingChange change3 = new RatingChange(15L, 13L, new Date(), (short) 1800, (short) 1782, (short) 100);

			final GameMove move1 = new GameMove(new MakeTurnMove(14L), 0, 0, new Date());
			final GameMove move2 = new GameMove(new MakeTurnMove(14L), 0, 0, new Date());
			final GameMove move3 = new GameMove(new MakeTurnMove(14L), 0, 0, new Date());
			final GameMove move4 = new GameMove(new MakeTurnMove(14L), 0, 0, new Date());

			final GameBoard board = createMock(GameBoard.class);
			expect(board.getGameMoves()).andReturn(Arrays.asList(move1, move2, move3, move4));
			replay(board);

			playerStatisticManager.updateRatingInfo(board, statistic, ri, Arrays.asList(change1, change2, change3));
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
			statistic.incrementLostGames();
			final RatingChange change1 = new RatingChange(13L, 13L, new Date(), (short) 1000, (short) 1025, (short) 300);
			final RatingChange change2 = new RatingChange(14L, 13L, new Date(), (short) 1400, (short) 1392, (short) 100);
			final RatingChange change3 = new RatingChange(15L, 13L, new Date(), (short) 1800, (short) 1782, (short) 200);

			final GameMove move1 = new GameMove(new MakeTurnMove(14L), 0, 0, new Date());
			final GameMove move2 = new GameMove(new MakeTurnMove(14L), 0, 0, new Date());
			final GameMove move3 = new GameMove(new MakeTurnMove(14L), 0, 0, new Date());
			final GameMove move4 = new GameMove(new MakeTurnMove(14L), 0, 0, new Date());

			final GameBoard board = createMock(GameBoard.class);
			expect(board.getGameMoves()).andReturn(Arrays.asList(move1, move2, move3, move4));
			replay(board);

			playerStatisticManager.updateRatingInfo(board, statistic, ri, Arrays.asList(change1, change2, change3));
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
		final HibernatePlayerStatistic s1 = new HibernatePlayerStatistic(Personality.person(13L));
		final HibernatePlayerStatistic s2 = new HibernatePlayerStatistic(Personality.person(14L));

		expect(playerStatisticDao.loadPlayerStatistic(Personality.person(13L))).andReturn(s1);
		playerStatisticDao.savePlayerStatistic(Personality.person(13L), s1);
		expect(playerStatisticDao.loadPlayerStatistic(Personality.person(14L))).andReturn(s2);
		playerStatisticDao.savePlayerStatistic(Personality.person(14L), s2);
		replay(playerStatisticDao);

		final GamePlayerHand hand1 = new GamePlayerHand(13L, 1, (short) 0);
		final GamePlayerHand hand2 = new GamePlayerHand(14L, 2, (short) 0);
		final GamePlayerHand hand3 = new GamePlayerHand(GuestPlayer.GUEST.getId(), 3, (short) 0); // guest is not rated

		final GameBoard board = createMock(GameBoard.class);
		expect(board.getPlayersHands()).andReturn(Arrays.asList(hand1, hand2, hand3));
		replay(board);

		boardStateListener.gameStarted(board);

		assertEquals(1, s1.getActiveGames());
		assertEquals(1, s2.getActiveGames());

		verify(playerStatisticDao, board);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void test_processGameFinished_Rated() {
		final HibernatePlayerStatistic s1 = new HibernatePlayerStatistic(Personality.person(13L));
		final HibernatePlayerStatistic s2 = new HibernatePlayerStatistic(Personality.person(14L));

		final GamePlayerHand hand1 = new GamePlayerHand(13L, 1, (short) 300);
		final GamePlayerHand hand2 = new GamePlayerHand(14L, 2, (short) 100);
		final GamePlayerHand hand3 = new GamePlayerHand(GuestPlayer.GUEST.getId(), 3, (short) 0); // guest is not rated

		final RatingChange change1 = new RatingChange(13L, 13L, new Date(), (short) 1000, (short) 1025, (short) 300);
		final RatingChange change2 = new RatingChange(14L, 13L, new Date(), (short) 1400, (short) 1392, (short) 100);
		final RatingChange change3 = new RatingChange(GuestPlayer.GUEST.getId(), 13L, new Date(), GuestPlayer.GUEST.getRating(), GuestPlayer.GUEST.getRating(), (short) 0);

		final GameBoard board = createMock(GameBoard.class);
		expect(board.isRatedGame()).andReturn(true);
		expect(board.getPlayersHands()).andReturn(Arrays.asList(hand1, hand2, hand3)).anyTimes();
		expect(board.getGameMoves()).andReturn(Collections.emptyList()).anyTimes();
		replay(board);

		expect(playerStatisticDao.loadPlayerStatistic(Personality.person(13L))).andReturn(s1);
		playerStatisticDao.savePlayerStatistic(Personality.person(13L), s1);
		expect(playerStatisticDao.loadPlayerStatistic(Personality.person(14L))).andReturn(s2);
		playerStatisticDao.savePlayerStatistic(Personality.person(14L), s2);
		replay(playerStatisticDao);

		expect(playerRatingManager.getRatingChanges(board)).andReturn(Arrays.asList(change1, change2, change3));
		replay(playerRatingManager);

		boardStateListener.gameFinished(board, GameResolution.FINISHED, Arrays.asList(hand1));

		assertEquals(1, s1.getWonGames());
		assertEquals(0, s1.getLostGames());
		assertEquals(0, s1.getDrawGames());
		assertEquals(1, s1.getFinishedGames());
		assertEquals(-1, s1.getActiveGames());

		assertEquals(0, s2.getWonGames());
		assertEquals(1, s2.getLostGames());
		assertEquals(0, s2.getDrawGames());
		assertEquals(1, s2.getFinishedGames());
		assertEquals(-1, s2.getActiveGames());

		verify(playerStatisticDao, board);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void test_processGameDraw() {
		final HibernatePlayerStatistic s1 = new HibernatePlayerStatistic(Personality.person(13L));
		final HibernatePlayerStatistic s2 = new HibernatePlayerStatistic(Personality.person(14L));

		final GamePlayerHand hand1 = new GamePlayerHand(13L, 1, (short) 300);
		final GamePlayerHand hand2 = new GamePlayerHand(14L, 2, (short) 100);
		final GamePlayerHand hand3 = new GamePlayerHand(GuestPlayer.GUEST.getId(), 3, (short) 0); // guest is not rated

		final RatingChange change1 = new RatingChange(13L, 13L, new Date(), (short) 1000, (short) 1025, (short) 300);
		final RatingChange change2 = new RatingChange(14L, 13L, new Date(), (short) 1400, (short) 1392, (short) 100);
		final RatingChange change3 = new RatingChange(GuestPlayer.GUEST.getId(), 13L, new Date(), GuestPlayer.GUEST.getRating(), GuestPlayer.GUEST.getRating(), (short) 0);

		final GameBoard board = createMock(GameBoard.class);
		expect(board.isRatedGame()).andReturn(true);
		expect(board.getPlayersHands()).andReturn(Arrays.asList(hand1, hand2, hand3)).anyTimes();
		expect(board.getGameMoves()).andReturn(Collections.emptyList()).anyTimes();
		replay(board);

		expect(playerStatisticDao.loadPlayerStatistic(Personality.person(13L))).andReturn(s1);
		playerStatisticDao.savePlayerStatistic(Personality.person(13L), s1);
		expect(playerStatisticDao.loadPlayerStatistic(Personality.person(14L))).andReturn(s2);
		playerStatisticDao.savePlayerStatistic(Personality.person(14L), s2);
		replay(playerStatisticDao);

		expect(playerRatingManager.getRatingChanges(board)).andReturn(Arrays.asList(change1, change2, change3));
		replay(playerRatingManager);

		boardStateListener.gameFinished(board, GameResolution.FINISHED, Collections.<GamePlayerHand>emptyList());

		assertEquals(0, s1.getWonGames());
		assertEquals(0, s1.getLostGames());
		assertEquals(1, s1.getDrawGames());
		assertEquals(1, s1.getFinishedGames());
		assertEquals(-1, s1.getActiveGames());

		assertEquals(0, s2.getWonGames());
		assertEquals(0, s2.getLostGames());
		assertEquals(1, s2.getDrawGames());
		assertEquals(1, s2.getFinishedGames());
		assertEquals(-1, s2.getActiveGames());

		verify(playerStatisticDao, board);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void test_processGameTimeout() {
		final HibernatePlayerStatistic s1 = new HibernatePlayerStatistic(Personality.person(13L));
		final HibernatePlayerStatistic s2 = new HibernatePlayerStatistic(Personality.person(14L));

		final GamePlayerHand hand1 = new GamePlayerHand(13L, 1, (short) 300);
		final GamePlayerHand hand2 = new GamePlayerHand(14L, 2, (short) 100);
		final GamePlayerHand hand3 = new GamePlayerHand(GuestPlayer.GUEST.getId(), 3, (short) 0); // guest is not rated

		final RatingChange change1 = new RatingChange(13L, 13L, new Date(), (short) 1000, (short) 1025, (short) 300);
		final RatingChange change2 = new RatingChange(14L, 13L, new Date(), (short) 1400, (short) 1392, (short) 100);
		final RatingChange change3 = new RatingChange(GuestPlayer.GUEST.getId(), 13L, new Date(), GuestPlayer.GUEST.getRating(), GuestPlayer.GUEST.getRating(), (short) 0);

		final GameBoard board = createMock(GameBoard.class);
		expect(board.isRatedGame()).andReturn(true);
		expect(board.getPlayersHands()).andReturn(Arrays.asList(hand1, hand2, hand3)).anyTimes();
		expect(board.getGameMoves()).andReturn(Collections.emptyList()).anyTimes();
		replay(board);

		expect(playerStatisticDao.loadPlayerStatistic(Personality.person(13L))).andReturn(s1);
		playerStatisticDao.savePlayerStatistic(Personality.person(13L), s1);
		expect(playerStatisticDao.loadPlayerStatistic(Personality.person(14L))).andReturn(s2);
		playerStatisticDao.savePlayerStatistic(Personality.person(14L), s2);
		replay(playerStatisticDao);

		expect(playerRatingManager.getRatingChanges(board)).andReturn(Arrays.asList(change1, change2, change3));
		replay(playerRatingManager);

		boardStateListener.gameFinished(board, GameResolution.TIMEOUT, Collections.<GamePlayerHand>emptyList());

		assertEquals(0, s1.getWonGames());
		assertEquals(0, s1.getLostGames());
		assertEquals(1, s1.getDrawGames());
		assertEquals(1, s1.getTimeouts());
		assertEquals(1, s1.getFinishedGames());
		assertEquals(-1, s1.getActiveGames());

		assertEquals(0, s2.getWonGames());
		assertEquals(0, s2.getLostGames());
		assertEquals(1, s2.getDrawGames());
		assertEquals(1, s2.getTimeouts());
		assertEquals(1, s2.getFinishedGames());
		assertEquals(-1, s2.getActiveGames());

		verify(playerStatisticDao, board);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void test_processGameFinished_NotRated() {
		final HibernatePlayerStatistic s1 = new HibernatePlayerStatistic(Personality.person(13L));
		final HibernatePlayerStatistic s2 = new HibernatePlayerStatistic(Personality.person(14L));

		final GamePlayerHand hand1 = new GamePlayerHand(13L, 1, (short) 300);
		final GamePlayerHand hand2 = new GamePlayerHand(14L, 2, (short) 100);
		final GamePlayerHand hand3 = new GamePlayerHand(GuestPlayer.GUEST.getId(), 3, (short) 0); // guest is not rated

		final RatingChange change1 = new RatingChange(13L, 13L, new Date(), (short) 1000, (short) 1025, (short) 300);
		final RatingChange change2 = new RatingChange(14L, 13L, new Date(), (short) 1400, (short) 1392, (short) 100);
		final RatingChange change3 = new RatingChange(GuestPlayer.GUEST.getId(), 13L, new Date(), GuestPlayer.GUEST.getRating(), GuestPlayer.GUEST.getRating(), (short) 0);

		final GameBoard board = createMock(GameBoard.class);
		expect(board.isRatedGame()).andReturn(false);
		expect(board.getPlayersHands()).andReturn(Arrays.asList(hand1, hand2, hand3)).anyTimes();
		expect(board.getGameMoves()).andReturn(Collections.emptyList()).anyTimes();
		replay(board);

		expect(playerStatisticDao.loadPlayerStatistic(Personality.person(13L))).andReturn(s1);
		playerStatisticDao.savePlayerStatistic(Personality.person(13L), s1);
		expect(playerStatisticDao.loadPlayerStatistic(Personality.person(14L))).andReturn(s2);
		playerStatisticDao.savePlayerStatistic(Personality.person(14L), s2);
		replay(playerStatisticDao);

		expect(playerRatingManager.getRatingChanges(board)).andReturn(Arrays.asList(change1, change2, change3));
		replay(playerRatingManager);

		boardStateListener.gameFinished(board, GameResolution.TIMEOUT, Collections.<GamePlayerHand>emptyList());

		assertEquals(0, s1.getWonGames());
		assertEquals(0, s1.getLostGames());
		assertEquals(0, s1.getDrawGames());
		assertEquals(0, s1.getTimeouts());
		assertEquals(0, s1.getFinishedGames());
		assertEquals(-1, s1.getActiveGames());

		assertEquals(0, s2.getWonGames());
		assertEquals(0, s2.getLostGames());
		assertEquals(0, s2.getDrawGames());
		assertEquals(0, s2.getTimeouts());
		assertEquals(0, s2.getFinishedGames());
		assertEquals(-1, s2.getActiveGames());

		verify(playerStatisticDao, board);
	}
}
