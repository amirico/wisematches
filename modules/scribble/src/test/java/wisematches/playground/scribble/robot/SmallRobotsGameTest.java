package wisematches.playground.scribble.robot;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import wisematches.core.*;
import wisematches.core.personality.DefaultVisitor;
import wisematches.playground.*;
import wisematches.playground.scribble.*;

import java.util.Collection;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.*;

/**
 * This is integration test and demonstrates how two robots can play a game...
 * <p/>
 * This test uses real configuration and store game into database. It check that all steps works correct.
 * <p/>
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/properties-config.xml",
		"classpath:/config/database-config.xml",
		"classpath:/config/personality-config.xml",
		"classpath:/config/gameplay-config.xml",
		"classpath:/config/scribble-config.xml"
})
public class SmallRobotsGameTest {
	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private ScribblePlayManager scribbleBoardManager;

	private final Lock gameFinishedLock = new ReentrantLock();
	private final Condition gameFinishedCondition = gameFinishedLock.newCondition();

	private static final Logger log = LoggerFactory.getLogger("wisematches.scribble.SmallRobotsGameTest");

	public SmallRobotsGameTest() {
	}

	@Before
	public void setUp() {
		Session session = sessionFactory.openSession();
		TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
	}

	@After
	public void tearDown() {
		SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory);
		SessionFactoryUtils.closeSession(sessionHolder.getSession());
	}

	@Test
	public void test_makeSmallGame() throws BoardCreationException, InterruptedException {
		final ScribblePlayerHand d = doSmallGame(RobotType.DULL);
		final ScribblePlayerHand t = doSmallGame(RobotType.TRAINEE);
		final ScribblePlayerHand e = doSmallGame(RobotType.EXPERT);

		System.out.println("Dull points: " + d);
		System.out.println("Trainee points: " + t);
		System.out.println("Expert points: " + e);
	}

	private ScribblePlayerHand doSmallGame(final RobotType robotType) throws BoardCreationException, InterruptedException {
		long currentTime = System.currentTimeMillis();
		assertNotNull("No room manager", scribbleBoardManager);
		final Visitor player = new DefaultVisitor(Language.RU);
		final ScribbleSettings settings = new ScribbleSettings("This is robots game", player.getLanguage(), 3, true, true);
		scribbleBoardManager.addBoardListener(new BoardListener() {
			@Override
			public void gameStarted(GameBoard<? extends GameSettings, ? extends GamePlayerHand, ? extends GameMove> b) {
				passGuestTurn(b);
			}

			@Override
			public void gameMoveDone(GameBoard<? extends GameSettings, ? extends GamePlayerHand, ? extends GameMove> b, GameMove move, GameMoveScore moveScore) {
				passGuestTurn(b);
			}

			@Override
			public void gameFinished(GameBoard<? extends GameSettings, ? extends GamePlayerHand, ? extends GameMove> b, GameResolution resolution, Collection<Personality> winners) {
				notifyGameFinished();
			}

			private void passGuestTurn(GameBoard<? extends GameSettings, ? extends GamePlayerHand, ? extends GameMove> b) {
				if (b.getPlayerTurn() == player) {
					ScribbleBoard sb = (ScribbleBoard) b;
					try {
						if (Math.random() < 0.5) {
							final ScribblePlayerHand playerHand = sb.getPlayerHand(player);
							final Tile[] tiles1 = playerHand.getTiles();
							final int length = Math.min(tiles1.length, sb.getBankRemained());
							if (length == 0) {
								sb.passTurn(player);
							} else {
								int[] tiles = new int[length];
								for (int i = 0; i < length; i++) {
									tiles[i] = tiles1[i].getNumber();
								}
								sb.exchangeTiles(player, tiles);
							}
						} else {
							sb.passTurn(player);
						}
					} catch (GameMoveException e) {
						log.error("Something wrong", e);
						notifyGameFinished();
					}
				}
			}
		});

		final ScribbleBoard board = scribbleBoardManager.createBoard(settings, player, robotType);
		assertTrue("Game is not in progress state", board.isActive());

		gameFinishedLock.lock();
		while (board.isActive()) {
			gameFinishedCondition.await();
		}
		gameFinishedLock.unlock();

		log.info("Game was finished at {}ms ", +(System.currentTimeMillis() - currentTime));

		assertTrue("Board is not saved", board.getBoardId() > 0);
		assertFalse("Board is not finished", board.isActive());
		assertTrue("Board has no one move", board.getGameMoves().size() > 0);
		final Personality playerTurn = board.getPlayerTurn();
		if (board.getResolution() == GameResolution.RESIGNED || board.getResolution() == GameResolution.INTERRUPTED) {
			assertNotNull("Board has a player who has a turn: " + playerTurn, playerTurn);
		} else {
			assertNull("Board has a player who has a turn: " + playerTurn, playerTurn);
		}

		final Robot robot = (Robot) board.getPlayers().get(1);
		final ScribblePlayerHand playerHand = board.getPlayerHand(robot);
		assertTrue(playerHand.getPoints() > 0);
		assertTrue(playerHand.getOldRating() == robot.getRating());
		assertTrue(playerHand.getNewRating() > robot.getRating());
		return playerHand;
	}

	private void notifyGameFinished() {
		gameFinishedLock.lock();
		gameFinishedCondition.signalAll();
		gameFinishedLock.unlock();
	}
}