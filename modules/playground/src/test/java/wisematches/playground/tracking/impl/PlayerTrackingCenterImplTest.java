package wisematches.playground.tracking.impl;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PlayerTrackingCenterImplTest {
/*
	private Account account;
	private BoardStateListener boardStateListener;

	@Autowired
	private AccountManager accountManager;
	@Autowired
	private HibernatePlayerRatingManager playerRatingManager;

	public PlayerTrackingCenterImplTest() {
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
	public void testGetRatingCurve() throws Exception {
		long end = System.currentTimeMillis();
		long start = end - 31536000000L; //365 * 24 * 60 * 60 * 1000; // - one year

		RatingCurve ratingCurve = playerRatingManager.getRatingCurve(Personality.person(1002), 10, new Date(start), new Date(end));
		System.out.println(ratingCurve);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void test_getRatingChanges_personality() {
*/
/*
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
*//*

//		final Object[] ratingChanges = playerRatingManager.getRatingChanges(Personality.person(1002), new Date(), RatingPeriod.YEAR, RatingBatching.MONTH).toArray();
//		System.out.println(Arrays.toString(ratingChanges));
//		assertEquals(3, ratingChanges.length);
//		assertRatingChange((RatingChange) ratingChanges[0], account.getId(), 22L, 1200, 1203);
//		assertRatingChange((RatingChange) ratingChanges[1], account.getId(), 23L, 1203, 1206);
//		assertRatingChange((RatingChange) ratingChanges[2], account.getId(), 24L, 1206, 1209);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void test_getRatingChanges_board() {
		final GameBoard b = createMock(GameBoard.class);
		expect(b.getBoardId()).andReturn(12L).times(4);
		expect(b.isRatedGame()).andReturn(true);
		expect(b.getFinishedTime()).andReturn(new Date());
		expect(b.getPlayersHands()).andReturn(Arrays.asList(
				new GamePlayerHand(RobotPlayer.DULL.getId(), (short) 100), // robot
				new GamePlayerHand(account.getId(), (short) 200))).times(2); // player
		replay(b);

		boardStateListener.gameFinished(b, GameResolution.FINISHED, Collections.<GamePlayerHand>emptyList());

		final Collection<RatingChange> ratingChanges = playerRatingManager.getRatingChanges(b);
		assertEquals(2, ratingChanges.size());

		final RatingChange c1 = (RatingChange) ratingChanges.toArray()[1];
		assertRatingChange(c1, RobotPlayer.DULL.getId(), 12L, RobotPlayer.DULL.getRating(), RobotPlayer.DULL.getRating());

		final RatingChange c2 = (RatingChange) ratingChanges.toArray()[0];
		assertRatingChange(c2, account.getId(), 12L, 1200, 1203);
	}

	private void assertRatingChange(RatingChange r1, long pid, long bid, int oldR, int newR) {
		assertEquals(pid, r1.getPlayerId());
		assertEquals(bid, r1.getBoardId());
		assertEquals(oldR, r1.getOldRating());
		assertEquals(newR, r1.getNewRating());
	}

	@Test
	public void test_getPosition() {
		System.out.println("Player's position: " + playerRatingManager.getPosition(account));
	}
*/

/*
	private PlayerStatisticDao statisticDao;

	private GamesStatistician gamesStatistician;
	private MovesStatistician movesStatistician;
	private RatingsStatistician ratingsStatistician;

	private PlayerStatisticManagerImpl statisticManager;

	public DefaultPlayerStatisticManagerTest() {
	}

	@Before
	public void setUp() {
		statisticDao = createMock(PlayerStatisticDao.class);
		gamesStatistician = createMock(GamesStatistician.class);
		movesStatistician = createMock(MovesStatistician.class);
		ratingsStatistician = createMock(RatingsStatistician.class);

		statisticManager = new PlayerStatisticManagerImpl();
		statisticManager.setPlayerStatisticDao(statisticDao);
		statisticManager.setGamesStatistician(gamesStatistician);
		statisticManager.setMovesStatistician(movesStatistician);
		statisticManager.setRatingsStatistician(ratingsStatistician);
	}

	@Test
	public void test_lockUnlock() throws InterruptedException {
		final ExecutorService executorService = Executors.newFixedThreadPool(2);

		final Personality p1_1 = Personality.person(1L);
		final Personality p1_2 = Personality.person(1L);
		final Personality p2 = Personality.person(2L);

		final HibernatePlayerStatistic ps1_1 = new HibernatePlayerStatistic(p1_1);
		final HibernatePlayerStatistic ps1_2 = new HibernatePlayerStatistic(p1_2);
		final HibernatePlayerStatistic ps2 = new HibernatePlayerStatistic(p2);

		expect(statisticDao.loadPlayerStatistic(p1_1)).andReturn(ps1_1);
		expect(statisticDao.loadPlayerStatistic(p1_1)).andReturn(ps1_2);
		expect(statisticDao.loadPlayerStatistic(p1_1)).andReturn(ps2);
		replay(statisticDao);

		statisticManager.lock(p1_1);
		final Future<PlayerStatistic> future = executorService.submit(new Callable<PlayerStatistic>() {
			public PlayerStatistic call() throws Exception {
				return statisticManager.getPlayerStatistic(p1_2);
			}
		});
		final Future<PlayerStatistic> future2 = executorService.submit(new Callable<PlayerStatistic>() {
			public PlayerStatistic call() throws Exception {
				return statisticManager.getPlayerStatistic(p2);
			}
		});
		Thread.sleep(300);
		assertFalse(future.isDone());
		assertTrue(future2.isDone());

		final PlayerStatistic statistic = statisticManager.getPlayerStatistic(p1_2);
		assertNotNull(statistic);
		statisticManager.unlock(p1_1);

		Thread.sleep(300);
		assertTrue(future.isDone());
	}

	@Test
	public void testAccountManager() {
		final Capture<AccountListener> l = new Capture<AccountListener>();
		final Capture<HibernatePlayerStatistic> ps = new Capture<HibernatePlayerStatistic>();

		final AccountManager m = createMock(AccountManager.class);
		m.addAccountListener(capture(l));
		replay(m);

		final Account a = new AccountEditor("asd", "qwe", "wqe").createAccount();

		statisticManager.setAccountManager(m);

		statisticDao.savePlayerStatistic(capture(ps));
		replay(statisticDao);
		l.getValue().accountCreated(a);
		verify(statisticDao);

		reset(statisticDao);
		replay(statisticDao);
		l.getValue().accountUpdated(a, a);
		verify(statisticDao);

		reset(statisticDao);
		expect(statisticDao.loadPlayerStatistic(a)).andReturn(ps.getValue());
		statisticDao.removePlayerStatistic(ps.getValue());
		replay(statisticDao);
		l.getValue().accountRemove(a);
		verify(statisticDao);
	}

	@Test
	public void testRoomsManager() {
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

		statisticManager.setRoomsManager(roomsManager);

		final GameMove move = new GameMove(createMock(PlayerMove.class), 12, 1, new Date());
		final List<GamePlayerHand> wonPlayers = Collections.emptyList();

		final GameBoard board = createMock(GameBoard.class);
		expect(board.getPlayersHands()).andReturn(Arrays.asList(
				new GamePlayerHand(GuestPlayer.GUEST.getId(), (short) 10),
				new GamePlayerHand(13L, (short) 20))).anyTimes();
		expect(board.getWonPlayers()).andReturn(wonPlayers);
		replay(board);

		final GamesStatisticEditor gse = createMock(GamesStatisticEditor.class);
		final MovesStatisticEditor mse = createMock(MovesStatisticEditor.class);
		final RatingsStatisticEditor rse = createMock(RatingsStatisticEditor.class);

		final HibernatePlayerStatistic statistic = createMock(HibernatePlayerStatistic.class);
		expect(statistic.getGamesStatistic()).andReturn(gse).anyTimes();
		expect(statistic.getMovesStatistic()).andReturn(mse).anyTimes();
		expect(statistic.getRatingsStatistic()).andReturn(rse).anyTimes();
		replay(statistic);

		expect(statisticDao.loadPlayerStatistic(GuestPlayer.GUEST)).andReturn(statistic).times(3);
		expect(statisticDao.loadPlayerStatistic(Personality.person(13L))).andReturn(statistic).times(3);
		statisticDao.savePlayerStatistic(statistic);
		expectLastCall().times(4);
		replay(statisticDao);

		gamesStatistician.updateGamesStatistic(board, statistic, gse);
		gamesStatistician.updateGamesStatistic(board, GameResolution.FINISHED, wonPlayers, statistic, gse);
		movesStatistician.updateMovesStatistic(board, move, statistic, mse);
		ratingsStatistician.updateRatingsStatistic(board, GameResolution.FINISHED, wonPlayers, statistic, rse);
		replay(gamesStatistician);

		capture.getValue().gameStarted(board);
		capture.getValue().gameMoveDone(board, move);
		capture.getValue().gameFinished(board, GameResolution.FINISHED, wonPlayers);

		verify(gamesStatistician);
	}
*/
}
