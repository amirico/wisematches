package wisematches.server.player.rating.impl;

import org.easymock.IAnswer;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import wisematches.kernel.player.Player;
import wisematches.server.core.MockPlayer;
import wisematches.server.core.account.PlayerManager;
import wisematches.server.games.board.GameBoard;
import wisematches.server.games.board.GamePlayerHand;
import wisematches.server.games.board.GameStateListener;
import wisematches.server.games.room.*;
import wisematches.server.player.rating.PlayerRatingEvent;
import wisematches.server.player.rating.PlayerRatingListener;
import wisematches.server.player.rating.RatingSystem;

import java.util.Arrays;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RatingsCalculationCenterTest {
	private RoomManager roomManager;
	private PlayerManager playerManager;

	private RatingSystem ratingSystem;
	private RoomBoardsListener boardsListener;
	private GameStateListener gameStateListener;

	private RatingsCalculationCenter calculationCenter;

	private static final Room ROOM = Room.valueOf("MOCK");

	@Before
	public void setUp() {
		final GameBoard openedBoard = createStrictMock(GameBoard.class);
		openedBoard.addGameStateListener(isA(GameStateListener.class));
		replay(openedBoard);

		roomManager = createStrictMock(RoomManager.class);
		roomManager.addRoomBoardsListener(isA(RoomBoardsListener.class));
		expectLastCall().andAnswer(new IAnswer<Object>() {
			public Object answer() throws Throwable {
				boardsListener = (RoomBoardsListener) getCurrentArguments()[0];
				return null;
			}
		});
		expect(roomManager.getRoomType()).andReturn(ROOM);
		expect(roomManager.getOpenedBoards()).andReturn(Arrays.asList(openedBoard));
		replay(roomManager);

		playerManager = createStrictMock(PlayerManager.class);

		ratingSystem = createStrictMock(RatingSystem.class);

		RoomsManager roomsManager = createMock(RoomsManager.class);
		expect(roomsManager.getRoomManagers()).andReturn(Arrays.asList(roomManager));
		expect(roomsManager.getRoomManager(ROOM)).andReturn(roomManager).anyTimes();
		replay(roomsManager);

		calculationCenter = new RatingsCalculationCenter();
		calculationCenter.setPlayerManager(playerManager);
		calculationCenter.setRoomsManager(roomsManager);
		calculationCenter.setRatingSystem(ratingSystem);

		verify(roomManager, openedBoard);
		reset(roomManager);
	}

	@Test
	public void test_ratingCalculation() throws BoardLoadingException {
		final GamePlayerHand hand1 = new GamePlayerHand(1L, 100);
		final GamePlayerHand hand2 = new GamePlayerHand(2L, 200);
		final GamePlayerHand hand3 = new GamePlayerHand(3L, 100);

		final Player p1 = new MockPlayer(1L);
		p1.setRating(1000);

		final Player p2 = new MockPlayer(2L);
		p2.setRating(1500);

		final Player p3 = new MockPlayer(3L);
		p3.setRating(1200);

		final GameBoard board = createStrictMock(GameBoard.class);
		board.addGameStateListener(isA(GameStateListener.class));
		expectLastCall().andAnswer(new IAnswer<Object>() {
			public Object answer() throws Throwable {
				gameStateListener = (GameStateListener) getCurrentArguments()[0];
				return null;
			}
		});
		expect(board.isRatedGame()).andReturn(true);
		expect(board.getPlayersHands()).andReturn(Arrays.asList(hand1, hand2, hand3));
		replay(board);

		expect(playerManager.getPlayer(1L)).andReturn(p1);
		expect(playerManager.getPlayer(2L)).andReturn(p2);
		expect(playerManager.getPlayer(3L)).andReturn(p3);
		playerManager.updatePlayer(p1);
		playerManager.updatePlayer(p2);
		playerManager.updatePlayer(p3);
		replay(playerManager);

		expect(roomManager.openBoard(1L)).andReturn(board);
		roomManager.updateBoard(board);
		replay(roomManager);

		expect(ratingSystem.calculateRatings(aryEq(new Player[]{p1, p2, p3}), aryEq(new int[]{100, 200, 100}))).andReturn(
				new int[]{1050, 1600, 1000});
		replay(ratingSystem);

		final TransactionStatus status = createNiceMock(TransactionStatus.class);

		final PlatformTransactionManager transaction = createStrictMock(PlatformTransactionManager.class);
		expect(transaction.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW))).andReturn(status);
		transaction.commit(status);
		replay(transaction);

		final PlayerRatingListener listener = createStrictMock(PlayerRatingListener.class);
		expectRatingChanged(listener, p1, 1000, 1050);
		expectRatingChanged(listener, p2, 1500, 1600);
		expectRatingChanged(listener, p3, 1200, 1000);
		replay(listener);

		calculationCenter.addRatingsChangeListener(listener);
		calculationCenter.setTransactionManager(transaction);

		boardsListener.boardOpened(ROOM, 1L);
		gameStateListener.gameDraw(board);

		assertEquals(1050, p1.getRating());
		assertEquals(1600, p2.getRating());
		assertEquals(1000, p3.getRating());

		assertEquals(1000, hand1.getPreviousRating());
		assertEquals(1500, hand2.getPreviousRating());
		assertEquals(1200, hand3.getPreviousRating());

		assertEquals(50, hand1.getRatingDelta());
		assertEquals(100, hand2.getRatingDelta());
		assertEquals(-200, hand3.getRatingDelta());

		verify(board, playerManager, roomManager, listener, transaction);
	}

	@Test
	public void test_passNoRatedGame() throws BoardLoadingException {
		final GameBoard board = createStrictMock(GameBoard.class);
		board.addGameStateListener(isA(GameStateListener.class));
		expectLastCall().andAnswer(new IAnswer<Object>() {
			public Object answer() throws Throwable {
				gameStateListener = (GameStateListener) getCurrentArguments()[0];
				return null;
			}
		});

		replay(playerManager);

		expect(board.isRatedGame()).andReturn(false);
		replay(board);

		expect(roomManager.openBoard(1L)).andReturn(board);
		replay(roomManager);

		replay(ratingSystem);

		final PlayerRatingListener listener = createStrictMock(PlayerRatingListener.class);
		replay(listener);

		calculationCenter.addRatingsChangeListener(listener);
		boardsListener.boardOpened(ROOM, 1L);
		gameStateListener.gameDraw(board);

		verify(board, playerManager, roomManager, listener);
	}

	private void expectRatingChanged(PlayerRatingListener listener, final Player p, final int oldR, final int newR) {
		listener.playerRaitingChanged(isA(PlayerRatingEvent.class));
		expectLastCall().andAnswer(new IAnswer<Object>() {
			public Object answer() throws Throwable {
				final PlayerRatingEvent e = (PlayerRatingEvent) getCurrentArguments()[0];
				assertEquals(p, e.getPlayer());
				assertEquals(oldR, e.getOldRating());
				assertEquals(newR, e.getNewRating());
				return null;
			}
		});
	}
}
