package wisematches.server.gameplaying.expiration.impl;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import wisematches.server.gameplaying.board.*;
import wisematches.server.gameplaying.expiration.GameExpirationListener;
import wisematches.server.gameplaying.expiration.GameExpirationType;
import wisematches.server.gameplaying.room.MockRoom;
import wisematches.server.gameplaying.room.RoomManager;
import wisematches.server.gameplaying.room.RoomsManager;
import wisematches.server.gameplaying.board.BoardLoadingException;
import wisematches.server.gameplaying.board.BoardManager;
import wisematches.server.gameplaying.board.BoardStateListener;
import wisematches.server.gameplaying.search.BoardLastMoveInfo;
import wisematches.server.gameplaying.search.BoardsSearchEngine;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.easymock.EasyMock.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ExpiredGamesTerminatorTest {
	private BoardsSearchEngine searchesEngine;

	private BoardManager boardManager;
	private RoomManager roomManager;
	private RoomsManager roomsManager;

	private Capture<BoardStateListener> boardStateListener;

	private ThreadPoolTaskScheduler taskScheduler;
	private TransactionTemplate transactionTemplate;

	private ExpiredGamesTerminator gamesTerminator;

	private static final long MILLIS_IN_DAY = 24 * 60 * 60 * 1000;

	public ExpiredGamesTerminatorTest() {
	}

	@Before
	public void setUp() {
		searchesEngine = createMock(BoardsSearchEngine.class);

		boardStateListener = new Capture<BoardStateListener>();

		boardManager = createMock(BoardManager.class);
		boardManager.addBoardStateListener(capture(boardStateListener));

		roomManager = createMock(RoomManager.class);
		expect(roomManager.getRoomType()).andReturn(MockRoom.type);
		expect(roomManager.getSearchesEngine()).andReturn(searchesEngine);
		expect(roomManager.getBoardManager()).andReturn(boardManager);

		roomsManager = createMock(RoomsManager.class);
		expect(roomsManager.getRoomManagers()).andReturn(Arrays.asList(roomManager));

		transactionTemplate = new TransactionTemplate() {
			@Override
			public <T> T execute(TransactionCallback<T> action) throws TransactionException {
				return action.doInTransaction(null);
			}
		};

		taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.afterPropertiesSet();

		gamesTerminator = new ExpiredGamesTerminator();
		gamesTerminator.setTaskScheduler(taskScheduler);
		gamesTerminator.setTransactionTemplate(transactionTemplate);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testTerminatorInitialization() throws InterruptedException, GameMoveException, BoardLoadingException {
		final long time = System.currentTimeMillis();

		expect(searchesEngine.findExpiringBoards()).andReturn(Arrays.asList(
				new BoardLastMoveInfo(12L, 3, new Date(time - MILLIS_IN_DAY * 2 + 200)), // DAY
				new BoardLastMoveInfo(13L, 3, new Date(time - MILLIS_IN_DAY * 2 - MILLIS_IN_DAY / 2 + 200)), // HALF
				new BoardLastMoveInfo(14L, 3, new Date(time - MILLIS_IN_DAY * 3 + MILLIS_IN_DAY / 24 + 200)), // HOUR
				new BoardLastMoveInfo(15L, 3, new Date(time - MILLIS_IN_DAY * 4)), // OUT OF DATE
				new BoardLastMoveInfo(16L, 3, new Date(time - MILLIS_IN_DAY * 4)) // OUT OF DATE FINISHED
		));
		replay(searchesEngine);

		final GameBoard gameBoard = createStrictMock(GameBoard.class);
		expect(gameBoard.isGameActive()).andReturn(false);
		gameBoard.terminate();
		replay(gameBoard);

		final GameBoard gameBoard2 = createStrictMock(GameBoard.class);
		expect(gameBoard2.isGameActive()).andReturn(true);
		expect(gameBoard2.isGameActive()).andReturn(false);
		gameBoard2.terminate();
		replay(gameBoard2);

		expect(boardManager.openBoard(15L)).andReturn(gameBoard);
		expect(boardManager.openBoard(16L)).andReturn(gameBoard2);
		expect(boardManager.openBoard(16L)).andReturn(gameBoard2);
		replay(boardManager);

		replay(roomManager);

		expect(roomsManager.getBoardManager(MockRoom.type)).andReturn(boardManager).times(3);
		replay(roomsManager);

		final GameExpirationListener l = createMock(GameExpirationListener.class);
		l.gameExpiring(12L, MockRoom.type, GameExpirationType.ONE_DAY);
		l.gameExpiring(13L, MockRoom.type, GameExpirationType.HALF_DAY);
		l.gameExpiring(14L, MockRoom.type, GameExpirationType.ONE_HOUR);
		replay(l);

		gamesTerminator.addGameExpirationListener(l);
		gamesTerminator.setRoomsManager(roomsManager);

		Thread.sleep(500);

		verify(searchesEngine, l, gameBoard, gameBoard2, boardManager, roomsManager);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testListeners() throws InterruptedException, GameMoveException, BoardLoadingException {
		final long time = System.currentTimeMillis();

		expect(searchesEngine.findExpiringBoards()).andReturn(Collections.<BoardLastMoveInfo>emptyList());
		replay(searchesEngine);

		final GameSettings gs = createStrictMock(GameSettings.class);
		expect(gs.getDaysPerMove()).andReturn(3).times(2);
		replay(gs);

		final GameBoard gameBoard = createStrictMock(GameBoard.class);
		expect(gameBoard.getBoardId()).andReturn(12L);
		expect(gameBoard.getGameSettings()).andReturn(gs);
		expect(gameBoard.getLastMoveTime()).andReturn(new Date(time - 100)); // one mig ago
		expect(gameBoard.getBoardId()).andReturn(12L);
		expect(gameBoard.getGameSettings()).andReturn(gs);
		expect(gameBoard.getLastMoveTime()).andReturn(new Date(time - MILLIS_IN_DAY * 3 + 200)); // one mig ago
		expect(gameBoard.getBoardId()).andReturn(12L);
		replay(gameBoard);

		replay(boardManager, roomManager, roomsManager);

		final GameExpirationListener l = createMock(GameExpirationListener.class);
		replay(l);

		gamesTerminator.addGameExpirationListener(l);
		gamesTerminator.setRoomsManager(roomsManager);

		boardStateListener.getValue().gameStarted(gameBoard);
		boardStateListener.getValue().gameMoveDone(gameBoard, new GameMove(new MakeTurnMove(12L), 12, 1, new Date()));
		boardStateListener.getValue().gameFinished(gameBoard, GameResolution.FINISHED, Collections.<GamePlayerHand>emptyList());

		Thread.sleep(500);

		verify(searchesEngine, l, gameBoard, boardManager, roomsManager);
	}
}
