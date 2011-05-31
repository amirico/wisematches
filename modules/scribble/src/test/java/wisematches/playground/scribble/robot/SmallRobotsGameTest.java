package wisematches.playground.scribble.robot;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import wisematches.personality.Personality;
import wisematches.personality.player.computer.robot.RobotPlayer;
import wisematches.playground.*;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribbleBoardManager;
import wisematches.playground.scribble.ScribbleSettings;

import java.util.Arrays;
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
		"classpath:/config/database-junit-config.xml",
		"classpath:/config/scribble-junit-config.xml"})
public class SmallRobotsGameTest {
	@Autowired
	private ScribbleBoardManager scribbleBoardManager;

	private final Lock gameFinishedLock = new ReentrantLock();
	private final Condition gameFinishedCondition = gameFinishedLock.newCondition();

	private static final Log log = LogFactory.getLog("wisematches.scribble.robot.test");

	public SmallRobotsGameTest() {
	}

	@Test
	public void test_makeSmallGame() throws BoardCreationException, InterruptedException {
		long currentTime = System.currentTimeMillis();

		assertNotNull("No room manager", scribbleBoardManager);

		final RobotPlayer r1 = RobotPlayer.DULL;
		final RobotPlayer r2 = RobotPlayer.TRAINEE;
		final RobotPlayer r3 = RobotPlayer.EXPERT;

		final ScribbleBoard board = scribbleBoardManager.createBoard(
				new ScribbleSettings("This is robots game", "en", 3, false, true),
				Arrays.<Personality>asList(r1, r2, r3));
		board.addGameBoardListener(new GameBoardListener() {
			@Override
			public void gameMoveDone(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameMove move) {
			}

			@Override
			public void gameFinished(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameResolution resolution, Collection<? extends GamePlayerHand> wonPlayers) {
				notifyGameFinished();
			}
		});
		assertTrue("Game is not in progress state", board.isGameActive());

		gameFinishedLock.lock();
		while (board.isGameActive()) {
			gameFinishedCondition.await();
		}
		gameFinishedLock.unlock();

		log.info("Game was finished at " + (System.currentTimeMillis() - currentTime) + "ms");

		assertTrue("Board is not saved", board.getBoardId() > 0);
		assertFalse("Board is not finished", board.isGameActive());
		assertTrue("Board has no one move", board.getGameMoves().size() > 0);
		if (board.getGameResolution() == GameResolution.STALEMATE) {
			assertNull("Board has a player who has a turn", board.getPlayerTurn());
		} else {
			assertNotNull("Board has a player who has a turn", board.getPlayerTurn());
		}

		final int dullPoints = board.getPlayerHand(r1.getId()).getPoints();
		final int stagerPoints = board.getPlayerHand(r2.getId()).getPoints();
		final int expertPoints = board.getPlayerHand(r3.getId()).getPoints();

		if (log.isDebugEnabled()) {
			log.debug("Moves count: " + board.getGameMoves().size());
			log.debug("Players points: DULL - " + dullPoints +
					", TRAINEE - " + stagerPoints +
					", EXPERT - " + expertPoints);
			log.debug("Tiles in hands:");
			log.debug("     DULL - " + Arrays.toString(board.getPlayerHand(r1.getId()).getTiles()));
			log.debug("     TRAINEE - " + Arrays.toString(board.getPlayerHand(r2.getId()).getTiles()));
			log.debug("     EXPERT - " + Arrays.toString(board.getPlayerHand(r3.getId()).getTiles()));
		}

		assertTrue("Dull won a stager???", dullPoints < stagerPoints);
		assertTrue("Stager won a expert???", stagerPoints < expertPoints);
		assertEquals("More that one winner???", 1, board.getWonPlayers().size());
		assertEquals("EXPERT didn't win???", r3.getId(), board.getWonPlayers().get(0).getPlayerId());
	}

	private void notifyGameFinished() {
		gameFinishedLock.lock();
		gameFinishedCondition.signalAll();
		gameFinishedLock.unlock();
	}
}