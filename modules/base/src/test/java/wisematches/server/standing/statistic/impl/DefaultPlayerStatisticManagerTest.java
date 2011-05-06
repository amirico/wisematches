package wisematches.server.standing.statistic.impl;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;
import wisematches.server.personality.Personality;
import wisematches.server.personality.account.Account;
import wisematches.server.personality.account.AccountEditor;
import wisematches.server.personality.account.AccountListener;
import wisematches.server.personality.account.AccountManager;
import wisematches.server.personality.player.computer.guest.GuestPlayer;
import wisematches.server.playground.board.*;
import wisematches.server.playground.room.RoomManager;
import wisematches.server.playground.room.RoomsManager;
import wisematches.server.standing.statistic.PlayerStatistic;
import wisematches.server.standing.statistic.statistician.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@SuppressWarnings("unchecked")
public class DefaultPlayerStatisticManagerTest {
	private PlayerStatisticDao statisticDao;

	private GamesStatistician gamesStatistician;
	private MovesStatistician movesStatistician;
	private RatingsStatistician ratingsStatistician;

	private DefaultPlayerStatisticManager statisticManager;

	public DefaultPlayerStatisticManagerTest() {
	}

	@Before
	public void setUp() {
		statisticDao = createMock(PlayerStatisticDao.class);
		gamesStatistician = createMock(GamesStatistician.class);
		movesStatistician = createMock(MovesStatistician.class);
		ratingsStatistician = createMock(RatingsStatistician.class);

		statisticManager = new DefaultPlayerStatisticManager();
		statisticManager.setPlayerStatisticDao(statisticDao);
		statisticManager.setGamesStatistician(gamesStatistician);
		statisticManager.setMovesStatistician(movesStatistician);
		statisticManager.setRatingsStatistician(ratingsStatistician);
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

		expect(statisticDao.loadPlayerStatistic(p1_1)).andReturn(ps1_1);
		expect(statisticDao.loadPlayerStatistic(p1_1)).andReturn(ps1_2);
		expect(statisticDao.loadPlayerStatistic(p1_1)).andReturn(ps2);
		replay(statisticDao);

		statisticManager.lock(p1_1);
		final Future<PlayerStatistic> future = executorService.submit(new Callable<PlayerStatistic>() {
			public PlayerStatistic call() throws Exception {
				return statisticManager.getPlayerStatistic(p1_2);
			}
		});
		final Future<PlayerStatistic> future2 = executorService.submit(new Callable<PlayerStatistic>() {
			public PlayerStatistic call() throws Exception {
				return statisticManager.getPlayerStatistic(p2);
			}
		});
		Thread.sleep(300);
		assertFalse(future.isDone());
		assertTrue(future2.isDone());

		final PlayerStatistic statistic = statisticManager.getPlayerStatistic(p1_2);
		assertNotNull(statistic);
		statisticManager.unlock(p1_1);

		Thread.sleep(300);
		assertTrue(future.isDone());
	}

	@Test
	public void testAccountManager() {
		final Capture<AccountListener> l = new Capture<AccountListener>();
		final Capture<HibernatePlayerStatistic> ps = new Capture<HibernatePlayerStatistic>();

		final AccountManager m = createMock(AccountManager.class);
		m.addAccountListener(capture(l));
		replay(m);

		final Account a = new AccountEditor("asd", "qwe", "wqe").createAccount();

		statisticManager.setAccountManager(m);

		statisticDao.savePlayerStatistic(capture(ps));
		replay(statisticDao);
		l.getValue().accountCreated(a);
		verify(statisticDao);

		reset(statisticDao);
		replay(statisticDao);
		l.getValue().accountUpdated(a, a);
		verify(statisticDao);

		reset(statisticDao);
		expect(statisticDao.loadPlayerStatistic(a)).andReturn(ps.getValue());
		statisticDao.removePlayerStatistic(ps.getValue());
		replay(statisticDao);
		l.getValue().accountRemove(a);
		verify(statisticDao);
	}

	@Test
	public void testRoomsManager() {
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

		statisticManager.setRoomsManager(roomsManager);

		final GameMove move = new GameMove(createMock(PlayerMove.class), 12, 1, new Date());
		final List<GamePlayerHand> wonPlayers = Collections.emptyList();

		final GameBoard board = createMock(GameBoard.class);
		expect(board.getPlayersHands()).andReturn(Arrays.asList(
				new GamePlayerHand(GuestPlayer.GUEST.getId(), (short) 10),
				new GamePlayerHand(13L, (short) 20))).anyTimes();
		expect(board.getWonPlayers()).andReturn(wonPlayers);
		replay(board);

		final GamesStatisticEditor gse = createMock(GamesStatisticEditor.class);
		final MovesStatisticEditor mse = createMock(MovesStatisticEditor.class);
		final RatingsStatisticEditor rse = createMock(RatingsStatisticEditor.class);

		final HibernatePlayerStatistic statistic = createMock(HibernatePlayerStatistic.class);
		expect(statistic.getGamesStatistic()).andReturn(gse).anyTimes();
		expect(statistic.getMovesStatistic()).andReturn(mse).anyTimes();
		expect(statistic.getRatingsStatistic()).andReturn(rse).anyTimes();
		replay(statistic);

		expect(statisticDao.loadPlayerStatistic(GuestPlayer.GUEST)).andReturn(statistic).times(3);
		expect(statisticDao.loadPlayerStatistic(Personality.person(13L))).andReturn(statistic).times(3);
		statisticDao.savePlayerStatistic(statistic);
		expectLastCall().times(4);
		replay(statisticDao);

		gamesStatistician.updateGamesStatistic(board, statistic, gse);
		gamesStatistician.updateGamesStatistic(board, GameResolution.FINISHED, wonPlayers, statistic, gse);
		movesStatistician.updateMovesStatistic(board, move, statistic, mse);
		ratingsStatistician.updateRatingsStatistic(board, GameResolution.FINISHED, wonPlayers, statistic, rse);
		replay(gamesStatistician);

		capture.getValue().gameStarted(board);
		capture.getValue().gameMoveDone(board, move);
		capture.getValue().gameFinished(board, GameResolution.FINISHED, wonPlayers);

		verify(gamesStatistician);
	}
}
