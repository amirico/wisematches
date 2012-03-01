package wisematches.playground.expiration.impl;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import wisematches.playground.*;
import wisematches.playground.expiration.GameExpirationDescriptor;
import wisematches.playground.expiration.GameExpirationListener;
import wisematches.playground.expiration.GameExpirationType;
import wisematches.playground.search.SearchManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.easymock.EasyMock.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ExpiredGamesTerminatorTest {
	private BoardManager boardManager;
	private SearchManager<GameExpirationDescriptor, ?> searchManager;

	private Capture<BoardStateListener> boardStateListener;

	private ThreadPoolTaskScheduler taskScheduler;
	private TransactionTemplate transactionTemplate;

	private ExpiredGamesTerminator gamesTerminator;

	private static final long MILLIS_IN_DAY = 24 * 60 * 60 * 1000;

	public ExpiredGamesTerminatorTest() {
	}

	@Before
	@SuppressWarnings("unchecked")
	public void setUp() {
		searchManager = createMock(SearchManager.class);

		boardStateListener = new Capture<BoardStateListener>();

		boardManager = createMock(BoardManager.class);
		boardManager.addBoardStateListener(capture(boardStateListener));

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
		gamesTerminator.setSearchManager(searchManager);
		gamesTerminator.setTransactionTemplate(transactionTemplate);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testTerminatorInitialization() throws InterruptedException, GameMoveException, BoardLoadingException {
		final long time = System.currentTimeMillis();

		List<GameExpirationDescriptor> mockGameExpirationDescriptors = Arrays.<GameExpirationDescriptor>asList(
				new GameExpirationDescriptor(12L, 3, new Date(time - MILLIS_IN_DAY * 2 + 200)), // DAY
				new GameExpirationDescriptor(13L, 3, new Date(time - MILLIS_IN_DAY * 2 - MILLIS_IN_DAY / 2 + 200)), // HALF
				new GameExpirationDescriptor(14L, 3, new Date(time - MILLIS_IN_DAY * 3 + MILLIS_IN_DAY / 24 + 200)), // HOUR
				new GameExpirationDescriptor(15L, 3, new Date(time - MILLIS_IN_DAY * 4)), // OUT OF DATE
				new GameExpirationDescriptor(16L, 3, new Date(time - MILLIS_IN_DAY * 4)) // OUT OF DATE FINISHED
		);
		expect(searchManager.searchEntities(null, null, null, null, null)).andReturn(mockGameExpirationDescriptors);
		replay(searchManager);

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

		final GameExpirationListener l = createMock(GameExpirationListener.class);
		l.gameExpiring(12L, GameExpirationType.ONE_DAY);
		l.gameExpiring(13L, GameExpirationType.HALF_DAY);
		l.gameExpiring(14L, GameExpirationType.ONE_HOUR);
		replay(l);

		gamesTerminator.addGameExpirationListener(l);
		gamesTerminator.setBoardManager(boardManager);

		Thread.sleep(500);

		verify(searchManager, l, gameBoard, gameBoard2, boardManager);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testListeners() throws InterruptedException, GameMoveException, BoardLoadingException {
		final long time = System.currentTimeMillis();

		expect(searchManager.searchEntities(null, null, null, null, null)).andReturn(Collections.<GameExpirationDescriptor>emptyList());
		replay(searchManager);

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

		replay(boardManager);

		final GameExpirationListener l = createMock(GameExpirationListener.class);
		replay(l);

		gamesTerminator.addGameExpirationListener(l);
		gamesTerminator.setBoardManager(boardManager);

		boardStateListener.getValue().gameStarted(gameBoard);
		boardStateListener.getValue().gameMoveDone(gameBoard, new GameMove(new MakeTurnMove(12L), 12, 1, new Date()), null);
		boardStateListener.getValue().gameFinished(gameBoard, GameResolution.FINISHED, Collections.<GamePlayerHand>emptyList());

		Thread.sleep(500);

		verify(searchManager, l, gameBoard, boardManager);
	}
}
