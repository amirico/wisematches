package wisematches.server.games.cleaner;

import org.easymock.Capture;
import org.easymock.IAnswer;
import org.junit.Test;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import wisematches.server.games.board.*;
import wisematches.server.games.room.*;

import java.util.Arrays;
import java.util.Collections;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class GameTimeoutTerminatorTest {
	private static final Room ROOM = Room.valueOf("MOCK");

	private static final long ONE_HOUR = 60 * 60 * 1000;
	private static final long ONE_DAY = 24 * ONE_HOUR;

	@Test
	public void test_setRoomsManager_terminateExpired() throws GameMoveException, InterruptedException, BoardLoadingException {
		final long currentTime = System.currentTimeMillis();

		final GameBoard board1 = createStrictMock(GameBoard.class);
		expect(board1.getGameState()).andReturn(GameState.IN_PROGRESS);
		board1.terminate();
		replay(board1);

		final GameBoard board2 = createStrictMock(GameBoard.class);
		expect(board2.getGameState()).andReturn(GameState.FINISHED);
		replay(board2);

		final Capture<GameTimeoutEvent> event = new Capture<GameTimeoutEvent>();

		final GameTimeoutListener listener = createStrictMock(GameTimeoutListener.class);
		listener.timeIsUp(capture(event));
		replay(listener);

		final SearchesEngine searchesEngine = createStrictMock(SearchesEngine.class);
		expect(searchesEngine.findExpiringBoards()).andReturn(Arrays.asList(
				new ExpiringBoardInfo(1L, 2, currentTime - 2 * ONE_DAY - 4000),  // more when 2 days
				new ExpiringBoardInfo(2L, 2, currentTime - 2 * ONE_DAY + 4000)  // not more when 2 days
		));
		replay(searchesEngine);

		final RoomManager roomManager = createStrictMock(RoomManager.class);
		makeThreadSafe(roomManager, true);
		expect(roomManager.getRoomType()).andReturn(ROOM);
		expect(roomManager.getOpenedBoards()).andReturn(Collections.emptyList());
		roomManager.addRoomBoardsListener(isA(RoomBoardsListener.class));
		expect(roomManager.getRoomType()).andReturn(ROOM);
		expect(roomManager.getSearchesEngine()).andReturn(searchesEngine);
		expect(roomManager.openBoard(1L)).andReturn(board1);
		expect(roomManager.openBoard(2L)).andReturn(board2);
		replay(roomManager);

		final RoomsManager roomsManager = createMock(RoomsManager.class);
		makeThreadSafe(roomsManager, true);
		expect(roomsManager.getRoomManagers()).andReturn(Arrays.asList(roomManager)).anyTimes();
		expect(roomsManager.getRoomManager(ROOM)).andReturn(roomManager).anyTimes();
		replay(roomsManager);

		final TransactionTemplate transactionTemplate = createStrictMock(TransactionTemplate.class);
		expect(transactionTemplate.execute(isA(TransactionCallback.class))).andAnswer(new IAnswer<Object>() {
			@Override
			public Object answer() throws Throwable {
				final TransactionCallback callback = (TransactionCallback) getCurrentArguments()[0];
				return callback.doInTransaction(null);
			}
		}).times(2);
		replay(transactionTemplate);

		final GameTimeoutTerminator terminator = new GameTimeoutTerminator();
		terminator.addGameTimeoutListener(listener);
		terminator.setTransactionTemplate(transactionTemplate);
		terminator.setRoomsManager(roomsManager);

		Thread.sleep(200); //Wait while terminator thread started
		assertEquals(1L, event.getValue().getBoardId());
		assertEquals(RemainderType.TIME_IS_UP, event.getValue().getRemainderType());

		Thread.sleep(4200); // second board should be removed but no events because already finished
		verify(board1, board2, listener, searchesEngine, roomManager, roomsManager, transactionTemplate);
	}

	@Test
	public void test_setRoomsManager_scheduleBeforeHour() throws GameMoveException, BoardLoadingException, InterruptedException {
		final long currentTime = System.currentTimeMillis();

		final Capture<GameTimeoutEvent> event = new Capture<GameTimeoutEvent>();

		final GameTimeoutListener listener = createStrictMock(GameTimeoutListener.class);
		listener.timeIsRunningOut(capture(event));
		replay(listener);

		final SearchesEngine searchesEngine = createStrictMock(SearchesEngine.class);
		expect(searchesEngine.findExpiringBoards()).andReturn(Arrays.asList(
				new ExpiringBoardInfo(1L, 2, currentTime - 2 * ONE_DAY + ONE_HOUR + 4000)
		));
		replay(searchesEngine);

		final RoomManager roomManager = createStrictMock(RoomManager.class);
		makeThreadSafe(roomManager, true);
		expect(roomManager.getRoomType()).andReturn(ROOM);
		expect(roomManager.getOpenedBoards()).andReturn(Collections.emptyList());
		roomManager.addRoomBoardsListener(isA(RoomBoardsListener.class));
		expect(roomManager.getRoomType()).andReturn(ROOM);
		expect(roomManager.getSearchesEngine()).andReturn(searchesEngine);
		replay(roomManager);

		final RoomsManager roomsManager = createMock(RoomsManager.class);
		makeThreadSafe(roomsManager, true);
		expect(roomsManager.getRoomManagers()).andReturn(Arrays.asList(roomManager)).anyTimes();
		expect(roomsManager.getRoomManager(ROOM)).andReturn(roomManager).anyTimes();
		replay(roomsManager);

		final GameTimeoutTerminator terminator = new GameTimeoutTerminator();
		terminator.setTransactionTemplate(createNiceMock(TransactionTemplate.class));
		terminator.addGameTimeoutListener(listener);
		terminator.setRoomsManager(roomsManager);

		Thread.sleep(4200); //Wait while terminator thread started
		assertEquals(1L, event.getValue().getBoardId());
		assertEquals(RemainderType.ONE_HOUR, event.getValue().getRemainderType());

		verify(listener, searchesEngine, roomManager, roomsManager);
	}

	@Test
	public void test_setRoomsManager_attachListeners() throws BoardLoadingException {
		final Capture<RoomBoardsListener> listener = new Capture<RoomBoardsListener>();

		final SearchesEngine searchesEngine = createStrictMock(SearchesEngine.class);
		expect(searchesEngine.findExpiringBoards()).andReturn(Collections.emptyList());
		replay(searchesEngine);

		final GameBoard board1 = createStrictMock(GameBoard.class);
		expect(board1.getGameState()).andReturn(GameState.WAITING);
		board1.addGameMoveListener(isA(GameMoveListener.class));
		board1.addGameStateListener(isA(GameStateListener.class));
		replay(board1);

		final GameBoard board2 = createStrictMock(GameBoard.class);
		expect(board2.getGameState()).andReturn(GameState.IN_PROGRESS);
		board2.addGameMoveListener(isA(GameMoveListener.class));
		board2.addGameStateListener(isA(GameStateListener.class));
		replay(board2);

		final GameBoard board3 = createStrictMock(GameBoard.class);
		expect(board3.getGameState()).andReturn(GameState.FINISHED);
		replay(board3);

		final GameBoard board4 = createStrictMock(GameBoard.class);
		expect(board4.getGameState()).andReturn(GameState.IN_PROGRESS);
		board4.addGameMoveListener(isA(GameMoveListener.class));
		board4.addGameStateListener(isA(GameStateListener.class));
		replay(board4);

		final RoomManager roomManager = createStrictMock(RoomManager.class);
		makeThreadSafe(roomManager, true);
		expect(roomManager.getRoomType()).andReturn(ROOM);
		expect(roomManager.getOpenedBoards()).andReturn(Arrays.asList(board1, board2, board3));
		roomManager.addRoomBoardsListener(capture(listener));
		expect(roomManager.getRoomType()).andReturn(ROOM);
		expect(roomManager.getSearchesEngine()).andReturn(searchesEngine);
		expect(roomManager.openBoard(4L)).andReturn(board4);
		replay(roomManager);

		final RoomsManager roomsManager = createMock(RoomsManager.class);
		makeThreadSafe(roomsManager, true);
		expect(roomsManager.getRoomManagers()).andReturn(Arrays.asList(roomManager)).anyTimes();
		expect(roomsManager.getRoomManager(ROOM)).andReturn(roomManager).anyTimes();
		replay(roomsManager);

		final GameTimeoutTerminator terminator = new GameTimeoutTerminator();
		terminator.setRoomsManager(roomsManager);
		terminator.setTransactionTemplate(createNiceMock(TransactionTemplate.class));

		listener.getValue().boardOpened(ROOM, 4L);

		verify(board1, board2, board3, board4, searchesEngine, roomManager, roomsManager);
	}
}
