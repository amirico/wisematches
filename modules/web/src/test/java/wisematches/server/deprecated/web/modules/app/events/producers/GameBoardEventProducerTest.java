package wisematches.server.deprecated.web.modules.app.events.producers;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class GameBoardEventProducerTest {
/*
    private GameMoveListener gameMoveListener;
    private GameBoardListener gameStateListener;
    private GamePlayersListener gamePlayersListener;

    private ScribbleBoard board;

    private GameBoardEventProducer producer;

    private final Queue<Event> notifiedEvents = new LinkedList<Event>();

    @Before
    public void setUp() {
        board = createStrictMock(ScribbleBoard.class);

        final EventNotificator eventNotificator = createMock(EventNotificator.class);
        eventNotificator.fireEvent(isA(Event.class));
        expectLastCall().andAnswer(new IAnswer<Object>() {
            public Object answer() throws Throwable {
                notifiedEvents.add((Event) getCurrentArguments()[0]);
                return null;
            }
        }).anyTimes();
        replay(eventNotificator);

        PlayerManager playerManager = createMock(PlayerManager.class);
        expect(playerManager.getPlayer(anyLong())).andAnswer(new IAnswer<Player>() {
            public Player answer() throws Throwable {
                return createMockPlayer((Long) getCurrentArguments()[0]);
            }
        }).anyTimes();
        replay(playerManager);

        producer = new GameBoardEventProducer();
        producer.activateProducer(eventNotificator);
        producer.setPlayerManager(playerManager);
    }

    @Test
    public void test_convertScribbleBoard() {
        final Player player = createMockPlayer(13L);

        final PlayerManager manager = createStrictMock(PlayerManager.class);
        expect(manager.getPlayer(13L)).andReturn(player);
        replay(manager);

        final ScribbleSettings settings = new ScribbleSettings("Title", new Date(), 3, "en", 3, 1000, 3000);

        final ScribblePlayerHand hand = new ScribblePlayerHand(13L);

        final ScribbleBoard board = createMock(ScribbleBoard.class);
        expect(board.getBoardId()).andReturn(14L);
        expect(board.getGameSettings()).andReturn(settings);
        expect(board.getPlayersHands()).andReturn(Arrays.asList(hand));
        replay(board);

        final DashboardItemBean bean = GameBoardEventProducer.convertDashboard(board, manager);
        assertEquals(14L, bean.getBoardId());
        assertEquals(settings.getTitle(), bean.getTitle());
        assertEquals(settings.getLanguage(), bean.getLocale());
        assertEquals(settings.getDaysPerMove(), bean.getDaysPerMove());
        assertEquals(settings.getMaxRating(), bean.getMaxRating());
        assertEquals(settings.getMinRating(), bean.getMinRating());

        final PlayerInfoBean[] opponents = bean.getPlayers();
        assertEquals(3, opponents.length);
        assertNotNull(opponents[0]);
        assertEquals(13L, opponents[0].getPlayerId());
        assertEquals(0, opponents[0].getCurrentRating());

        assertNull(opponents[1]);
        assertNull(opponents[2]);

        verify(player, manager, board);
    }

    @Test
    public void test_convertGameMove() {
        final Word word = new Word(new Position(4, 5), Direction.VERTICAL, new Tile(1, 'a', 1), new Tile(2, 'b', 2));

        final PlayerMoveBean bean1 = GameBoardEventProducer.convertGameMove(new GameMove(new MakeWordMove(13L, word), 120, 1, 3000000L));
        assertEquals(1, bean1.getMoveNumber());
        assertEquals(13L, bean1.getPlayerId());
        assertEquals(120, bean1.getPoints());
        assertEquals(PlayerMoveBean.Type.MOVED, bean1.getMoveType());
        assertSame(word, bean1.getWord());

        final PlayerMoveBean bean2 = GameBoardEventProducer.convertGameMove(new GameMove(new ExchangeTilesMove(13L, new int[0]), 120, 1, 3000000L));
        assertEquals(1, bean2.getMoveNumber());
        assertEquals(13L, bean2.getPlayerId());
        assertEquals(120, bean2.getPoints());
        assertEquals(PlayerMoveBean.Type.EXCHANGE, bean2.getMoveType());
        assertNull(bean2.getWord());

        final PlayerMoveBean bean3 = GameBoardEventProducer.convertGameMove(new GameMove(new PassTurnMove(13L), 120, 1, 3000000L));
        assertEquals(1, bean3.getMoveNumber());
        assertEquals(13L, bean3.getPlayerId());
        assertEquals(120, bean3.getPoints());
        assertEquals(PlayerMoveBean.Type.PASSED, bean3.getMoveType());
        assertNull(bean3.getWord());
    }

    @Test
    public void test_setRoomManagerFacade() throws BoardLoadingException {
        final RoomManagerFacade roomManagerFacade = createStrictMock(RoomManagerFacade.class);
        roomManagerFacade.addGameBoardListener(isA(GameBoardListener.class));
        expectLastCall().andAnswer(new IAnswer<Object>() {
            public Object answer() throws Throwable {
                gameStateListener = (GameBoardListener) getCurrentArguments()[0];
                return null;
            }
        });
        roomManagerFacade.addGameMoveListener(isA(GameMoveListener.class));
        expectLastCall().andAnswer(new IAnswer<Object>() {
            public Object answer() throws Throwable {
                gameMoveListener = (GameMoveListener) getCurrentArguments()[0];
                return null;
            }
        });
        roomManagerFacade.addGamePlayersListener(isA(GamePlayersListener.class));
        expectLastCall().andAnswer(new IAnswer<Object>() {
            public Object answer() throws Throwable {
                gamePlayersListener = (GamePlayersListener) getCurrentArguments()[0];
                return null;
            }
        });
        replay(roomManagerFacade);

        producer.setRoomManagerFacade(roomManagerFacade);

        verify(roomManagerFacade);
    }

    @Test
    public void test_listenPlayerAdded() throws Exception {
        test_setRoomManagerFacade();

        final ScribblePlayerHand h1 = new ScribblePlayerHand(13L);
        final ScribblePlayerHand h2 = new ScribblePlayerHand(14L);

        reset(board);
        expect(board.getPlayersHands()).andReturn(Arrays.asList(h1)); //test number of players
        expect(board.getGameSettings()).andReturn(new ScribbleSettings("This is title", new Date(), 3, "de", 3, 1234, 4321, false));
        expect(board.getPlayersHands()).andReturn(Arrays.asList(h1));
        expect(board.getBoardId()).andReturn(123L);

        expect(board.getPlayersHands()).andReturn(Arrays.asList(h1, h2));  //test number of players
        expect(board.getBoardId()).andReturn(123L);
        replay(board);

        gamePlayersListener.playerAdded(board, createMockPlayer(13L));
        gamePlayersListener.playerAdded(board, createMockPlayer(14L));
        assertEquals(2, notifiedEvents.size());

        final GameCreatedEvent e1 = (GameCreatedEvent) notifiedEvents.poll();
        assertEquals(123L, e1.getBoardId());

        final DashboardItemBean b = e1.getGameSettingsBean();
        assertEquals(123L, b.getBoardId());
        assertEquals(3, b.getDaysPerMove());
        assertEquals("de", b.getLocale());
        assertEquals(1234, b.getMinRating());
        assertEquals(4321, b.getMaxRating());
        assertEquals("This is title", b.getTitle());
        assertEquals(3, b.getPlayers().length);
        assertEquals(13L, b.getPlayers()[0].getPlayerId());
        assertNull(b.getPlayers()[1]);
        assertNull(b.getPlayers()[2]);

        final GamePlayersEvent e2 = (GamePlayersEvent) notifiedEvents.poll();
        assertEquals(GamePlayersEvent.Action.ADDED, e2.getAction());
        assertEquals(14L, e2.getPlayerInfoBean().getPlayerId());
        assertEquals(123L, e2.getBoardId());

        verify(board);
    }

    @Test
    public void test_listenPlayerRemoved() throws Exception {
        test_setRoomManagerFacade();

        reset(board);
        expect(board.getBoardId()).andReturn(123L).times(2);
        replay(board);

        gamePlayersListener.playerRemoved(board, createMockPlayer(14L));
        gamePlayersListener.playerRemoved(board, createMockPlayer(13L));
        assertEquals(2, notifiedEvents.size());

        final GamePlayersEvent event = (GamePlayersEvent) notifiedEvents.poll();
        assertEquals(GamePlayersEvent.Action.REMOVED, event.getAction());
        assertEquals(123L, event.getBoardId());
        assertEquals(14L, event.getPlayerInfoBean().getPlayerId());

        final GamePlayersEvent event2 = (GamePlayersEvent) notifiedEvents.poll();
        assertEquals(123L, event.getBoardId());
        assertEquals(GamePlayersEvent.Action.REMOVED, event2.getAction());
        assertEquals(13L, event2.getPlayerInfoBean().getPlayerId());

        verify(board);
    }

    @Test
    public void test_listenGameStarted() throws Exception {
        test_setRoomManagerFacade();

        final Tile[] tiles1 = new Tile[2];
        final Tile[] tiles2 = new Tile[4];

        final ScribblePlayerHand h1 = new ScribblePlayerHand(13L, tiles1);
        final ScribblePlayerHand h2 = new ScribblePlayerHand(14L, tiles2);

        reset(board);
        expect(board.getPlayersHands()).andReturn(Arrays.asList(h1, h2));
        expect(board.getBoardId()).andReturn(123L);
        expect(board.getStartedTime()).andReturn(30000000L);
        replay(board);

        // Player added
        gameStateListener.gameStarted(board, new ScribblePlayerHand(14L));

        assertEquals(3, notifiedEvents.size());
        final GameStartedEvent e1 = (GameStartedEvent) notifiedEvents.poll();
        assertEquals(123L, e1.getBoardId());
        assertEquals(30000000L, e1.getStartTime());
        assertEquals(14L, e1.getPlayerTurn());
        assertEquals(13L, e1.getPlayerHand());
        assertSame(tiles1, e1.getPlayerTiles());

        final GameStartedEvent e2 = (GameStartedEvent) notifiedEvents.poll();
        assertEquals(123L, e2.getBoardId());
        assertEquals(30000000L, e2.getStartTime());
        assertEquals(14L, e2.getPlayerTurn());
        assertEquals(14L, e2.getPlayerHand());
        assertSame(tiles2, e2.getPlayerTiles());

        final GameStartedEvent e3 = (GameStartedEvent) notifiedEvents.poll();
        assertEquals(123L, e3.getBoardId());
        assertEquals(30000000L, e3.getStartTime());
        assertEquals(14L, e3.getPlayerTurn());
        assertEquals(0, e3.getPlayerHand());
        assertNull(e3.getPlayerTiles());

        verify(board);
    }

    @Test
    public void tst_listenGameFinishedDrawInterrupted() throws Exception {
        test_setRoomManagerFacade();

        reset(board);
        for (int i = 0; i < 4; i++) {
            expect(board.getPlayersHands()).andReturn(Arrays.asList(new ScribblePlayerHand(13L, 10 + 10 * i), new ScribblePlayerHand(14L, 20 + 10 * i)));
            expect(board.getBoardId()).andReturn(123L);
            expect(board.isRatedGame()).andReturn(true);
            expect(board.getFinishedTime()).andReturn(400000L);
        }
        replay(board);

        gameStateListener.gameFinished(board, new ScribblePlayerHand(13L));
        gameStateListener.gameDrew(board);
        gameStateListener.gameInterrupted(board, new ScribblePlayerHand(13L), false);
        gameStateListener.gameInterrupted(board, new ScribblePlayerHand(13L), true);

        assertEquals(4, notifiedEvents.size());
        final GameFinishedEvent.Type[] types = new GameFinishedEvent.Type[]{
                GameFinishedEvent.Type.FINISHED,
                GameFinishedEvent.Type.FINISHED,
                GameFinishedEvent.Type.RESIGNED,
                GameFinishedEvent.Type.TIMEDOUT,
        };
        for (int i = 0; i < 4; i++) {
            final GameFinishedEvent event = (GameFinishedEvent) notifiedEvents.poll();
            assertEquals(123L, event.getBoardId());
            assertEquals(400000L, event.getFinishedTime());
            if (i != 1) {
                assertEquals(13L, event.getPlayerId());
            } else {
                assertEquals(0L, event.getPlayerId()); // passed
            }
            assertTrue(event.isRated());
            assertArrayEquals(new Object[]{
                    new GameFinishedEvent.FinalPoint(13L, 10 + 10 * i), new GameFinishedEvent.FinalPoint(14L, 20 + 10 * i)},
                    event.getFinalPoints());
            assertEquals(types[i], event.getType());
        }
        verify(board);
    }

    @Test
    public void test_listenPlayerMoved() throws Exception {
        test_setRoomManagerFacade();

        reset(board);
        expect(board.getBoardId()).andReturn(123L);
        replay(board);

        final Word word = new Word(new Position(3, 9), Direction.VERTICAL, new Tile(1, 'a', 1), new Tile(2, 'b', 2));
        final PlayerMove move = new MakeWordMove(13L, word);
        gameMoveListener.gameMoveMade(
                new GameMoveEvent(board,
                        new ScribblePlayerHand(13L, new Tile(3, 'c', 3)),
                        new GameMove(move, 1, 2, 3),
                        new ScribblePlayerHand(14L))
        );

        assertEquals(1, notifiedEvents.size());
        final GameTurnEvent event = (GameTurnEvent) notifiedEvents.poll();
        assertEquals(123L, event.getBoardId());
        assertEquals(13L, event.getPlayerId());
        assertEquals(1, event.getHandTilesCount());
        assertEquals(14L, event.getNextPlayer());

        final PlayerMoveBean bean = event.getPlayerMoveBean();
        assertEquals(word, bean.getWord());
        assertEquals(1, bean.getPoints());
        assertEquals(2, bean.getMoveNumber());
        assertEquals(13L, bean.getPlayerId());
        assertEquals(PlayerMoveBean.Type.MOVED, bean.getMoveType());

        verify(board);
    }

    private Player createMockPlayer(long id) {
        final Player player = createMock(Player.class);
        expect(player.getId()).andReturn(id).anyTimes();
        expect(player.getNickname()).andReturn("test-" + id).anyTimes();
        expect(player.getRating()).andReturn(100 + (int) id).anyTimes();
        replay(player);
        return player;
    }
*/
}
