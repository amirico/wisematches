package wisematches.server.gameplaying.scribble.robot;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;
import wisematches.server.gameplaying.board.GameBoard;
import wisematches.server.gameplaying.board.GamePlayerHand;
import wisematches.server.gameplaying.board.GameState;
import wisematches.server.gameplaying.board.GameStateListener;
import wisematches.server.gameplaying.dictionary.impl.file.FileDictionaryManager;
import wisematches.server.gameplaying.room.BoardCreationException;
import wisematches.server.gameplaying.room.RoomsManager;
import wisematches.server.gameplaying.scribble.board.ScribbleBoard;
import wisematches.server.gameplaying.scribble.board.ScribbleSettings;
import wisematches.server.gameplaying.scribble.room.ScribbleRoomManager;
import wisematches.server.player.robot.RobotPlayer;
import wisematches.server.player.robot.RobotsBrainManager;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This is integration test and demonstrates how two robots can play a game...
 * <p/>
 * This test uses real configuration and store game into database. It check that all steps works correct.
 * <p/>
 * TODO: move this test to integration test
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class SmallRobotsGameTest extends AbstractTransactionalDataSourceSpringContextTests {
	@Autowired
	private RoomsManager roomsManager;
	@Autowired
	private RobotsBrainManager robotsBrainManager;

	private final Lock gameFinishedLock = new ReentrantLock();
	private final Condition gameFinishedCondition = gameFinishedLock.newCondition();

	private static final Log log = LogFactory.getLog("wisematches.scribble.robot.test");

	@Override
	protected String[] getConfigLocations() {
		return new String[]{"classpath:/config/test-game-scribble-config.xml"};
	}

	public void onSetUp() throws Exception {
		super.onSetUp();

		final FileDictionaryManager bean = (FileDictionaryManager) applicationContext.getBean("lexicalDictionary");
		assertNotNull(bean);

		//Change link to dictionary file
		final URL dict = getClass().getResource("/dicts/dictionary_en.properties");
		bean.setDictionariesFolder(new File(dict.getFile()).getParentFile());
		bean.setFilePostfix("properties");
	}

	public void test_makeSmallGame() throws BoardCreationException, InterruptedException {
		long currentTime = System.currentTimeMillis();

		assertNotNull("No room manager", roomsManager);
		assertNotNull("No robots brain manager", robotsBrainManager);

		final RobotPlayer r1 = robotsBrainManager.getPlayer(1L);
		final RobotPlayer r2 = robotsBrainManager.getPlayer(2L);
		final RobotPlayer r3 = robotsBrainManager.getPlayer(3L);

		final ScribbleRoomManager roomManager = (ScribbleRoomManager) roomsManager.getRoomManager(ScribbleRoomManager.ROOM);

		final ScribbleBoard board = roomManager.createBoard(r1, new ScribbleSettings("This is robots game", new Date(), 3, "en"));
		board.addGameStateListener(new GameStateListener() {
			public void gameStarted(GameBoard board, GamePlayerHand playerTurn) {
			}

			public void gameFinished(GameBoard board, GamePlayerHand wonPlayer) {
				notifyGameFInished();
			}

			public void gameDraw(GameBoard board) {
				notifyGameFInished();
			}

			public void gameInterrupted(GameBoard board, GamePlayerHand interrupterPlayer, boolean byTimeout) {
				notifyGameFInished();
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

		final int dullPoints = board.getPlayerHand(1L).getPoints();
		final int stagerPoints = board.getPlayerHand(2L).getPoints();
		final int expertPoints = board.getPlayerHand(3L).getPoints();

		if (log.isDebugEnabled()) {
			log.debug("Moves count: " + board.getGameMoves().size());
			log.debug("Players points: DULL - " + dullPoints +
					", STAGER - " + stagerPoints +
					", EXPERT - " + expertPoints);
			log.debug("Tiles in hands:");
			log.debug("     DULL - " + Arrays.toString(board.getPlayerHand(1L).getTiles()));
			log.debug("     STAGER - " + Arrays.toString(board.getPlayerHand(2L).getTiles()));
			log.debug("     EXPERT - " + Arrays.toString(board.getPlayerHand(3L).getTiles()));
		}

		assertTrue("Dull won a stager???", dullPoints < stagerPoints);
		assertTrue("Stager won a expert???", stagerPoints < expertPoints);
		assertEquals("EXPERT didn't win???", 3L, board.getWonPlayer().getPlayerId());
	}

	public void setRoomsManager(RoomsManager roomsManager) {
		this.roomsManager = roomsManager;
	}

	public void setRobotsBrainManager(RobotsBrainManager robotsBrainManager) {
		this.robotsBrainManager = robotsBrainManager;
	}

	private void notifyGameFInished() {
		gameFinishedLock.lock();
		gameFinishedCondition.signalAll();
		gameFinishedLock.unlock();
	}
}