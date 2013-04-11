package wisematches.playground.scribble.expiration;

import org.easymock.Capture;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.NullExpression;
import org.hibernate.criterion.Projection;
import org.junit.Before;
import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import wisematches.core.Language;
import wisematches.core.Personality;
import wisematches.core.expiration.ExpirationListener;
import wisematches.playground.BoardListener;
import wisematches.playground.GameBoard;
import wisematches.playground.GameMove;
import wisematches.playground.GameResolution;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribblePlayManager;
import wisematches.playground.scribble.ScribbleSettings;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.easymock.EasyMock.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleExpirationManagerTest {
	private Session session;
	private ScribblePlayManager scribbleBoardManager;
	private ScribbleExpirationManager expirationManager;

	private static final int MILLIS_IN_DAY = 86400000;//24 * 60 * 60 * 1000;

	public ScribbleExpirationManagerTest() {
	}

	@Before
	public void setUp() {
		session = createMock(Session.class);

		scribbleBoardManager = createMock(ScribblePlayManager.class);

		final TransactionTemplate transactionTemplate = new TransactionTemplate() {
			@Override
			public <T> T execute(TransactionCallback<T> action) throws TransactionException {
				return action.doInTransaction(null);
			}
		};

		final ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.afterPropertiesSet();

		final SessionFactory sessionFactory = createMock(SessionFactory.class);
		expect(sessionFactory.getCurrentSession()).andReturn(session).anyTimes();
		replay(sessionFactory);

		expirationManager = new ScribbleExpirationManager();
		expirationManager.setTaskScheduler(taskScheduler);
		expirationManager.setSessionFactory(sessionFactory);
		expirationManager.setTransactionTemplate(transactionTemplate);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testTerminatorInitialization() throws Exception {
		final ScribbleBoard gameBoard = createStrictMock(ScribbleBoard.class);
		expect(gameBoard.isActive()).andReturn(false);
		gameBoard.terminate();
		replay(gameBoard);

		final ScribbleBoard gameBoard2 = createStrictMock(ScribbleBoard.class);
		expect(gameBoard2.isActive()).andReturn(true);
		replay(gameBoard2);

		scribbleBoardManager.addBoardListener(isA(BoardListener.class));
		expect(scribbleBoardManager.openBoard(15L)).andReturn(gameBoard);
		expect(scribbleBoardManager.openBoard(16L)).andReturn(gameBoard2);
		replay(scribbleBoardManager);

		final ExpirationListener<Long, ScribbleExpirationType> l = createMock(ExpirationListener.class);
		l.expirationTriggered(12L, ScribbleExpirationType.ONE_DAY);
		l.expirationTriggered(13L, ScribbleExpirationType.HALF_DAY);
		l.expirationTriggered(14L, ScribbleExpirationType.ONE_HOUR);
		replay(l);

		final long time = System.currentTimeMillis();

		final Criteria criteria = createMock(Criteria.class);
		expect(criteria.add(isA(NullExpression.class))).andReturn(criteria);
		expect(criteria.setProjection(isA(Projection.class))).andReturn(criteria);
		expect(criteria.list()).andReturn(Arrays.asList(
				new Object[]{12L, 3, new Date(time - MILLIS_IN_DAY * 2 + 200)},  // DAY
				new Object[]{13L, 3, new Date(time - MILLIS_IN_DAY * 2 - MILLIS_IN_DAY / 2 + 200)}, // HALF
				new Object[]{14L, 3, new Date(time - MILLIS_IN_DAY * 3 + MILLIS_IN_DAY / 24 + 200)}, // HOUR
				new Object[]{15L, 3, new Date(time - MILLIS_IN_DAY * 4)}, // OUT OF DATE
				new Object[]{16L, 3, new Date(time - MILLIS_IN_DAY * 4)} // OUT OF DATE FINISHED
		));
		replay(criteria);

		expect(session.createCriteria(ScribbleBoard.class)).andReturn(criteria);
		replay(session);

		expirationManager.addExpirationListener(l);
		expirationManager.setBoardManager(scribbleBoardManager);
		expirationManager.afterPropertiesSet();

		Thread.sleep(500);

		verify(session, criteria, l, gameBoard, gameBoard2, scribbleBoardManager);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testListeners() throws Exception {
		final long time = System.currentTimeMillis();

		final Capture<BoardListener> boardStateListener = new Capture<>();

		scribbleBoardManager.addBoardListener(capture(boardStateListener));
		replay(scribbleBoardManager);

		final ExpirationListener<Long, ScribbleExpirationType> l = createMock(ExpirationListener.class);
		replay(l);

		final Criteria criteria = createMock(Criteria.class);
		expect(criteria.add(isA(NullExpression.class))).andReturn(criteria);
		expect(criteria.setProjection(isA(Projection.class))).andReturn(criteria);
		expect(criteria.list()).andReturn(Collections.emptyList());
		replay(criteria);

		expect(session.createCriteria(ScribbleBoard.class)).andReturn(criteria);
		replay(session);

		final ScribbleSettings gs = new ScribbleSettings("mock", Language.RU, 3);

		final GameBoard gameBoard = createStrictMock(GameBoard.class);
		expect(gameBoard.getBoardId()).andReturn(12L);
		expect(gameBoard.getLastMoveTime()).andReturn(new Date(time - 100)); // one mig ago
		expect(gameBoard.getSettings()).andReturn(gs);
		expect(gameBoard.getBoardId()).andReturn(12L);
		expect(gameBoard.getLastMoveTime()).andReturn(new Date(time - MILLIS_IN_DAY * 3 + 200)); // one mig ago
		expect(gameBoard.getSettings()).andReturn(gs);
		expect(gameBoard.getBoardId()).andReturn(12L);
		replay(gameBoard);

		expirationManager.addExpirationListener(l);
		expirationManager.setBoardManager(scribbleBoardManager);
		expirationManager.afterPropertiesSet();

		final GameMove move = createMock(GameMove.class);

		boardStateListener.getValue().gameStarted(gameBoard);
		boardStateListener.getValue().gameMoveDone(gameBoard, move, null);
		boardStateListener.getValue().gameFinished(gameBoard, GameResolution.FINISHED, Collections.<Personality>emptyList());

		Thread.sleep(500);

		verify(session, criteria, l, gameBoard, scribbleBoardManager);
	}
}
