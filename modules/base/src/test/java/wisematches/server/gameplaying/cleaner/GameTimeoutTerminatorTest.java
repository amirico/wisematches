package wisematches.server.gameplaying.cleaner;

import org.junit.Test;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class GameTimeoutTerminatorTest {

	@Test
	public void commented() {
		throw new UnsupportedOperationException("Test has been commented");
	}
/*
	private static final Room ROOM = Room.valueOf("MOCK");

	private static final long ONE_HOUR = 60 * 60 * 1000;
	private static final long ONE_DAY = 24 * ONE_HOUR;

	@Test
	public void test_setRoomsManager_terminateExpired() throws GameMoveException, InterruptedException, BoardLoadingException {
		final long currentTime = System.currentTimeMillis();

		final GameBoard board1 = createStrictMock(GameBoard.class);
		expect(board1.getGameState()).andReturn(GameState.ACTIVE);
		board1.terminate();
		replay(board1);

		final GameBoard board2 = createStrictMock(GameBoard.class);
		expect(board2.getGameState()).andReturn(GameState.FINISHED);
		replay(board2);

		final Capture<GameTimeoutEvent> event = new Capture<GameTimeoutEvent>();

		final GameTimeoutListener listener = createStrictMock(GameTimeoutListener.class);
		listener.timeIsUp(capture(event));
		replay(listener);

		final BoardsSearchEngine boardsSearchEngine = createStrictMock(BoardsSearchEngine.class);
		expect(boardsSearchEngine.findExpiringBoards()).andReturn(Arrays.asList(
				new ExpiringBoard(1L, 2, new Date(currentTime - 2 * ONE_DAY - 4000)),  // more when 2 days
				new ExpiringBoard(2L, 2, new Date(currentTime - 2 * ONE_DAY + 4000))  // not more when 2 days
		));
		replay(boardsSearchEngine);

		final RoomManager roomManager = createStrictMock(RoomManager.class);
		makeThreadSafe(roomManager, true);
		expect(roomManager.getRoomType()).andReturn(ROOM);
		expect(roomManager.getOpenedBoards()).andReturn(Collections.emptyList());
		roomManager.addRoomBoardsListener(isA(BoardListener.class));
		expect(roomManager.getRoomType()).andReturn(ROOM);
		expect(roomManager.getSearchesEngine()).andReturn(boardsSearchEngine);
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
		verify(board1, board2, listener, boardsSearchEngine, roomManager, roomsManager, transactionTemplate);
	}

	@Test
	public void test_setRoomsManager_scheduleBeforeHour() throws GameMoveException, BoardLoadingException, InterruptedException {
		final long currentTime = System.currentTimeMillis();

		final Capture<GameTimeoutEvent> event = new Capture<GameTimeoutEvent>();

		final GameTimeoutListener listener = createStrictMock(GameTimeoutListener.class);
		listener.timeIsRunningOut(capture(event));
		replay(listener);

		final BoardsSearchEngine boardsSearchEngine = createStrictMock(BoardsSearchEngine.class);
		expect(boardsSearchEngine.findExpiringBoards()).andReturn(Arrays.asList(
				new ExpiringBoard(1L, 2, new Date(currentTime - 2 * ONE_DAY + ONE_HOUR + 4000))
		));
		replay(boardsSearchEngine);

		final RoomManager roomManager = createStrictMock(RoomManager.class);
		makeThreadSafe(roomManager, true);
		expect(roomManager.getRoomType()).andReturn(ROOM);
		expect(roomManager.getOpenedBoards()).andReturn(Collections.emptyList());
		roomManager.addRoomBoardsListener(isA(BoardListener.class));
		expect(roomManager.getRoomType()).andReturn(ROOM);
		expect(roomManager.getSearchesEngine()).andReturn(boardsSearchEngine);
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

		verify(listener, boardsSearchEngine, roomManager, roomsManager);
	}

	@Test
	public void test_setRoomsManager_attachListeners() throws BoardLoadingException {
		final Capture<BoardListener> listener = new Capture<BoardListener>();

		final BoardsSearchEngine boardsSearchEngine = createStrictMock(BoardsSearchEngine.class);
		expect(boardsSearchEngine.findExpiringBoards()).andReturn(Collections.emptyList());
		replay(boardsSearchEngine);

		final GameBoard board2 = createStrictMock(GameBoard.class);
		expect(board2.getGameState()).andReturn(GameState.ACTIVE);
		board2.addGameBoardListener(isA(GameBoardListener.class));
		replay(board2);

		final GameBoard board3 = createStrictMock(GameBoard.class);
		expect(board3.getGameState()).andReturn(GameState.FINISHED);
		replay(board3);

		final GameBoard board4 = createStrictMock(GameBoard.class);
		expect(board4.getGameState()).andReturn(GameState.ACTIVE);
		board4.addGameBoardListener(isA(GameBoardListener.class));
		replay(board4);

		final RoomManager roomManager = createStrictMock(RoomManager.class);
		makeThreadSafe(roomManager, true);
		expect(roomManager.getRoomType()).andReturn(ROOM);
		expect(roomManager.getOpenedBoards()).andReturn(Arrays.asList(board2, board3));
		roomManager.addRoomBoardsListener(capture(listener));
		expect(roomManager.getRoomType()).andReturn(ROOM);
		expect(roomManager.getSearchesEngine()).andReturn(boardsSearchEngine);
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

		verify(board2, board3, board4, boardsSearchEngine, roomManager, roomsManager);
	}
*/
}
