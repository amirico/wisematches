package wisematches.server.gameplaying.scribble.robot;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import wisematches.server.gameplaying.board.GameBoard;
import wisematches.server.gameplaying.board.GamePlayerHand;
import wisematches.server.gameplaying.board.GameState;
import wisematches.server.gameplaying.board.GameStateListener;
import wisematches.server.gameplaying.robot.RobotBrainManager;
import wisematches.server.gameplaying.room.BoardCreationException;
import wisematches.server.gameplaying.room.RoomsManager;
import wisematches.server.gameplaying.scribble.board.ScribbleBoard;
import wisematches.server.gameplaying.scribble.board.ScribbleSettings;
import wisematches.server.gameplaying.scribble.room.ScribbleRoomManager;
import wisematches.server.player.computer.robot.RobotPlayer;

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.*;

/**
 * This is integration test and demonstrates how two robots can play a game...
 * <p/>
 * This test uses real configuration and store game into database. It check that all steps works correct.
 * <p/>
 * TODO: move this test to integration test
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/database-config.xml",
		"classpath:/config/server-base-config.xml",
		"classpath:/config/game-scribble-config.xml",
		"classpath:/config/test-game-modules-config.xml"})
public class SmallRobotsGameTest {
	@Autowired
	private RoomsManager roomsManager;

	@Autowired
	private RobotBrainManager robotBrainManager;

	private final Lock gameFinishedLock = new ReentrantLock();
	private final Condition gameFinishedCondition = gameFinishedLock.newCondition();

	private static final Log log = LogFactory.getLog("wisematches.scribble.robot.test");

	@Test
	public void test_makeSmallGame() throws BoardCreationException, InterruptedException {
		long currentTime = System.currentTimeMillis();

		assertNotNull("No room manager", roomsManager);
		assertNotNull("No robots brain manager", robotBrainManager);

		final RobotPlayer r1 = RobotPlayer.DULL;
		final RobotPlayer r2 = RobotPlayer.TRAINEE;
		final RobotPlayer r3 = RobotPlayer.EXPERT;

		final ScribbleRoomManager roomManager = (ScribbleRoomManager) roomsManager.getRoomManager(ScribbleRoomManager.ROOM);

		final ScribbleBoard board = roomManager.createBoard(r1, new ScribbleSettings("This is robots game", new Date(), 3, "en", 3, 0, Integer.MAX_VALUE, false, true));
		board.addGameStateListener(new GameStateListener() {
			public void gameStarted(GameBoard board, GamePlayerHand playerTurn) {
			}

			public void gameFinished(GameBoard board, GamePlayerHand wonPlayer) {
				notifyGameFinished();
			}

			public void gameDraw(GameBoard board) {
				notifyGameFinished();
			}

			public void gameInterrupted(GameBoard board, GamePlayerHand interrupterPlayer, boolean byTimeout) {
				notifyGameFinished();
			}
		});
		board.addPlayer(r2);
		board.addPlayer(r3);

		assertEquals("Game is not in progress state", GameState.IN_PROGRESS, board.getGameState());

		gameFinishedLock.lock();
		if (board.getGameState() == GameState.IN_PROGRESS) {
			gameFinishedCondition.await();
		}
		gameFinishedLock.unlock();

		log.info("Game was finished at " + (System.currentTimeMillis() - currentTime) + "ms");

		assertTrue("Board is not saved", board.getBoardId() > 0);
		assertEquals("Board is not finished", GameState.FINISHED, board.getGameState());
		assertTrue("Board has no one move", board.getGameMoves().size() > 0);
		assertNull("Board has a player who has a turn", board.getPlayerTrun());

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
		assertEquals("EXPERT didn't win???", r3.getId(), board.getWonPlayer().getPlayerId());
	}

	private void notifyGameFinished() {
		gameFinishedLock.lock();
		gameFinishedCondition.signalAll();
		gameFinishedLock.unlock();
	}
}