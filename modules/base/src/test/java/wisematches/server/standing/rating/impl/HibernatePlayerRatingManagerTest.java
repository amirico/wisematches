package wisematches.server.standing.rating.impl;

import org.easymock.Capture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.SimpleJdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;
import wisematches.server.gameplaying.board.GameBoard;
import wisematches.server.gameplaying.board.GamePlayerHand;
import wisematches.server.gameplaying.board.GameResolution;
import wisematches.server.gameplaying.room.RoomManager;
import wisematches.server.gameplaying.room.RoomsManager;
import wisematches.server.gameplaying.room.board.BoardManager;
import wisematches.server.gameplaying.room.board.BoardStateListener;
import wisematches.server.personality.account.*;
import wisematches.server.personality.player.computer.robot.RobotPlayer;
import wisematches.server.standing.rating.PlayerRatingListener;
import wisematches.server.standing.rating.RatingChange;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/test-server-base-config.xml",
		"classpath:/config/database-config.xml",
		"classpath:/config/server-base-config.xml"
})
public class HibernatePlayerRatingManagerTest {
	private Account account;
	private BoardStateListener boardStateListener;

	@Autowired
	private AccountManager accountManager;
	@Autowired
	private HibernatePlayerRatingManager playerRatingManager;

	public HibernatePlayerRatingManagerTest() {
	}

	@Before
	public void createUser() throws InadmissibleUsernameException, DuplicateAccountException {
		final UUID uuid = UUID.randomUUID();
		AccountEditor editor = new AccountEditor(uuid.toString(), "HibernatePlayerRatingManagerTest", "");
		account = accountManager.createAccount(editor.createAccount());

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

		playerRatingManager.setRoomsManager(roomsManager);
		playerRatingManager.setRatingSystem(new RatingSystem() {
			@Override
			public short[] calculateRatings(short[] ratings, short[] points) {
				final short[] res = new short[ratings.length];
				for (int i = 0; i < ratings.length; i++) {
					res[i] = (short) (ratings[i] + 3);
				}
				return res;
			}
		});

		boardStateListener = capture.getValue();
	}

	@After
	public void removeUser() throws UnknownAccountException {
		accountManager.removeAccount(account);

		playerRatingManager.setRoomsManager(null);
	}

	@Test
	public void test_getRating() {
		assertEquals(1200, playerRatingManager.getRating(account));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void test_updateRating() {
		final GameBoard b = createMock(GameBoard.class);
		expect(b.getBoardId()).andReturn(12L).anyTimes();
		expect(b.isRatedGame()).andReturn(true);
		expect(b.getPlayersHands()).andReturn(Arrays.asList(
				new GamePlayerHand(RobotPlayer.DULL.getId(), (short) 100), // robot
				new GamePlayerHand(account.getId(), (short) 200))); // player
		replay(b);

		final PlayerRatingListener l = createMock(PlayerRatingListener.class);
		l.playerRatingChanged(account, b, (short) 1200, (short) 1203);
		replay(l);

		playerRatingManager.addRatingsChangeListener(l);

		assertTrue(1200 == playerRatingManager.getRating(account));
		boardStateListener.gameFinished(b, GameResolution.FINISHED, Collections.<GamePlayerHand>emptyList());
		assertFalse(1200 == playerRatingManager.getRating(account));

		playerRatingManager.removeRatingsChangeListener(l);

		verify(l);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void test_getRatingChanges_personality() {
		final GameBoard b = createMock(GameBoard.class);
		expect(b.getBoardId()).andReturn(22L).times(2).andReturn(23L).times(2).andReturn(24L).times(2);
		expect(b.isRatedGame()).andReturn(true).times(3);
		expect(b.getPlayersHands()).andReturn(Arrays.asList(
				new GamePlayerHand(RobotPlayer.DULL.getId(), (short) 100), // robot
				new GamePlayerHand(account.getId(), (short) 200))).times(3); // player
		replay(b);

		boardStateListener.gameFinished(b, GameResolution.FINISHED, Collections.<GamePlayerHand>emptyList());
		boardStateListener.gameFinished(b, GameResolution.FINISHED, Collections.<GamePlayerHand>emptyList());
		boardStateListener.gameFinished(b, GameResolution.FINISHED, Collections.<GamePlayerHand>emptyList());

		final Object[] ratingChanges = playerRatingManager.getRatingChanges(account).toArray();
		System.out.println(Arrays.toString(ratingChanges));
		assertEquals(3, ratingChanges.length);
		assertRatingChange((RatingChange) ratingChanges[0], account.getId(), 22L, 1200, 1203);
		assertRatingChange((RatingChange) ratingChanges[1], account.getId(), 23L, 1203, 1206);
		assertRatingChange((RatingChange) ratingChanges[2], account.getId(), 24L, 1206, 1209);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void test_getRatingChanges_board() {
		final GameBoard b = createMock(GameBoard.class);
		expect(b.getBoardId()).andReturn(12L).times(3);
		expect(b.isRatedGame()).andReturn(true);
		expect(b.getPlayersHands()).andReturn(Arrays.asList(
				new GamePlayerHand(RobotPlayer.DULL.getId(), (short) 100), // robot
				new GamePlayerHand(account.getId(), (short) 200))); // player
		replay(b);

		boardStateListener.gameFinished(b, GameResolution.FINISHED, Collections.<GamePlayerHand>emptyList());

		final Object[] ratingChanges = playerRatingManager.getRatingChanges(b).toArray();
		assertEquals(1, ratingChanges.length); // no rating change for a robot
		assertRatingChange((RatingChange) ratingChanges[0], account.getId(), 12L, 1200, 1203);
	}

	private void assertRatingChange(RatingChange r1, long pid, long bid, int oldR, int newR) {
		assertEquals(pid, r1.getPlayerId());
		assertEquals(bid, r1.getGameBoardId());
		assertEquals(oldR, r1.getOldRating());
		assertEquals(newR, r1.getNewRating());
	}

	@Test
	public void test_getPosition() {
		System.out.println("Player's position: " + playerRatingManager.getPosition(account));
	}
}
