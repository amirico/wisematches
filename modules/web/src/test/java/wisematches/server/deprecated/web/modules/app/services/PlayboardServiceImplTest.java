package wisematches.server.deprecated.web.modules.app.services;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayboardServiceImplTest {
/*    private ScribbleBoard board;
    private ScribbleSettings settings;
    private ScribblePlayerHand hand13;
    private ScribblePlayerHand hand14;

    private Word word1;
    private Word word2;

    private PlayerManager playerManager;
    private ScribbleBoardManager roomManager;

    private PlayboardServiceImpl service;

    private long startedTime = System.currentTimeMillis() - 100000;
    private long finishedTime = System.currentTimeMillis() + 10000;

    @Before
    public void setUp() {
        roomManager = createStrictMock(ScribbleBoardManager.class);

        playerManager = createStrictMock(PlayerManager.class);
        expect(playerManager.getPlayer(13L)).andReturn(createMockPlayer(13L));
        expect(playerManager.getPlayer(14L)).andReturn(createMockPlayer(14L));
        replay(playerManager);

        final RoomsManager roomsManager = createStrictMock(RoomsManager.class);
        expect(roomsManager.getRoomManager(ScribbleBoardManager.ROOM)).andReturn(roomManager);
        replay(roomsManager);

        service = new PlayboardServiceImpl();
        service.setRoomsManager(roomsManager);
        service.setPlayerManager(playerManager);

        settings = new ScribbleSettings("Title", new Date(), 3, "en", 3, 1000, 3000);

        hand13 = new ScribblePlayerHand(13L, 100, new Tile(10, 'a', 1), new Tile(11, 'a', 1));
        hand14 = new ScribblePlayerHand(14L, 120, new Tile(12, 'a', 1), new Tile(13, 'a', 1), new Tile(14, 'a', 1));

        word1 = new Word(new Position(7, 7), Direction.HORIZONTAL, new Tile(1, 'a', 1), new Tile(2, 'b', 1), new Tile(3, 'c', 1));
        word2 = new Word(new Position(7, 7), Direction.VERTICAL, new Tile(1, 'a', 1), new Tile(4, 'd', 1), new Tile(5, 'e', 1), new Tile(6, 'f', 1));

        final GameMove move1 = new GameMove(new MakeWordMove(14L, word1), 10, 0, System.currentTimeMillis());
        final GameMove move2 = new GameMove(new MakeWordMove(13L, word2), 15, 1, System.currentTimeMillis());
        final GameMove move3 = new GameMove(new PassTurnMove(14L), 0, 2, System.currentTimeMillis());
        final GameMove move4 = new GameMove(new ExchangeTilesMove(13L, new int[0]), 0, 3, System.currentTimeMillis());

        board = createMock(ScribbleBoard.class);
        expect(board.getBoardId()).andReturn(10L);
        expect(board.getGameSettings()).andReturn(settings).times(2);
        expect(board.getStartedTime()).andReturn(startedTime).times(2);
        expect(board.getFinishedTime()).andReturn(finishedTime).times(2);
        expect(board.getPlayerTurn()).andReturn(hand14);
        expect(board.getPlayersHands()).andReturn(Arrays.asList(hand13, hand14)).times(2);
        expect(board.getLastMoveTime()).andReturn(12323131312321L); // 3 minutes
        expect(board.getGameMoves()).andReturn(Arrays.<GameMove>asList(move1, move2, move3, move4));
        expect(board.getPlayerHand(13L)).andReturn(hand13);
        expect(board.getBankCapacity()).andReturn(18);
        expect(board.getTilesBankInfo()).andReturn(new TilesBank.TilesInfo[]{new TilesBank.TilesInfo('a', 12, 1), new TilesBank.TilesInfo('b', 4, 2)});
    }


    @After
    public void thearDown() {
        RemoteServiceContextAccessor.destroy();
    }

    @Test
    public void test_convertBoard() {
        RemoteServiceContextAccessor.expectPlayer(createMockPlayer(13L));

        replay(board);

        final PlayboardItemBean bean = service.converBoard(board);
        assertEquals(10L, bean.getBoardId());
        assertEquals("Title", bean.getTitle());
        assertEquals(3, bean.getDaysPerMove());
        assertEquals("en", bean.getLocale());

        assertEquals(3, bean.getPlayers().length);
        assertEquals(13L, bean.getPlayers()[0].getPlayerId());
        assertEquals(14L, bean.getPlayers()[1].getPlayerId());
        assertNull(bean.getPlayers()[2]);

        assertEquals(3, bean.getPlayersTilesCount().length);
        assertEquals(2, bean.getPlayersTilesCount()[0]);
        assertEquals(3, bean.getPlayersTilesCount()[1]);
        assertEquals(0, bean.getPlayersTilesCount()[2]);

        assertEquals(14L, bean.getPlayerMove());
        assertEquals(startedTime, bean.getStartedTime());
        assertEquals(finishedTime, bean.getFinishedTime());
        assertEquals(PlayboardItemBean.GameState.FINISHED, bean.getGameResolution());
        assertEquals(12323131312321L, bean.getLastMoveTime());

        assertArrayEquals(hand13.getTiles(), bean.getHandTiles());
        assertEquals(5, bean.getTilesInHands());

        List<PlayerMoveBean> words = bean.getPlayersMoves();
        assertEquals(4, words.size());

        assertEquals(14, words.get(0).getPlayerId());
        assertEquals(word1, words.get(0).getWord());
        assertEquals(PlayerMoveBean.Type.MOVED, words.get(0).getMoveType());

        assertEquals(13, words.get(1).getPlayerId());
        assertEquals(word2, words.get(1).getWord());
        assertEquals(PlayerMoveBean.Type.MOVED, words.get(1).getMoveType());

        assertEquals(14, words.get(2).getPlayerId());
        assertNull(words.get(2).getWord());
        assertEquals(PlayerMoveBean.Type.PASSED, words.get(2).getMoveType());

        assertEquals(13, words.get(3).getPlayerId());
        assertNull(words.get(3).getWord());
        assertEquals(PlayerMoveBean.Type.EXCHANGE, words.get(3).getMoveType());

        assertEquals(16, bean.getBankCapacity());

        final Tile[] bankTiles = bean.getBankTiles();
        assertEquals(2, bankTiles.length);
        assertEquals('a', bankTiles[0].getLetter());
        assertEquals(12, bankTiles[0].getNumber());
        assertEquals(1, bankTiles[0].getCost());

        assertEquals('b', bankTiles[1].getLetter());
        assertEquals(4, bankTiles[1].getNumber());
        assertEquals(2, bankTiles[1].getCost());

        verify(playerManager);
    }

    @Test
    public void test_openBoard_Error1() throws BoardLoadingException {
        expect(roomManager.openBoard(13L)).andReturn(null);
        replay(roomManager);

        try {
            service.openBoard(13L);
            fail("Exception must be here: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            ;
        }
    }

    @Test
    public void test_openBoard_Error2() throws BoardLoadingException {
        expect(roomManager.openBoard(13L)).andThrow(new BoardLoadingException("testManager"));
        replay(roomManager);

        assertNull(service.openBoard(13L));
    }

    @Test
    public void test_openBoard() throws BoardLoadingException {
        replay(board);

        expect(roomManager.openBoard(13L)).andReturn(board);
        replay(roomManager);

        PlayerOpenBoardsBean bean = RemoteServiceContextAccessor.expectPlayerSessionBean(PlayerOpenBoardsBean.class);
        expect(bean.getPlayer()).andReturn(createMockPlayer(13L));
        bean.addOpenedBoard(board);
        replay(bean);

        assertNotNull(service.openBoard(13L));

        verify(bean, roomManager, bean);
    }

    @Test
    public void test_closeBoard() throws BoardLoadingException {
        test_openBoard(); // First open a board

        PlayerOpenBoardsBean bean = RemoteServiceContextAccessor.expectPlayerSessionBean(PlayerOpenBoardsBean.class);
        expect(bean.closeOpenedBoard(14L)).andReturn(null);
        expect(bean.closeOpenedBoard(14L)).andReturn(board);
        replay(bean);

        reset(board, roomManager);
        replay(board, roomManager);
        service.closeBoard(14L); //resign unknown game
        verify(board, roomManager);

        reset(board, roomManager);
        replay(board, roomManager);
        service.closeBoard(14L); //resign unknown game

        verify(board, roomManager, bean);
    }

    @Test
    public void test_convertException() {
        assertEquals(PlayerMoveException.ErrorCode.UNSUITABLE_PLAYER,
                service.convertException(new UnsuitablePlayerException("test", createMockPlayer(18L))).getErrorCode());

        assertEquals(PlayerMoveException.ErrorCode.GAME_NOT_READY,
                service.convertException(new GameNotReadyException("")).getErrorCode());

        assertEquals(PlayerMoveException.ErrorCode.GAME_FINISHED,
                service.convertException(new GameFinishedException(GameState.FINISHED)).getErrorCode());

        assertEquals(PlayerMoveException.ErrorCode.UNKNOWN_WORD,
                service.convertException(new UnknownWordException("testManager")).getErrorCode());

        assertEquals(PlayerMoveException.ErrorCode.CELL_ALREADY_BUSY,
                service.convertException(new IncorrectTilesException(new Tile(1, 'a', 1), new Tile(2, 'b', 2), new Position(7, 7))).getErrorCode());

        assertEquals(PlayerMoveException.ErrorCode.INCORRECT_POSITION,
                service.convertException(new IncorrectPositionException(new Position(7, 7), Direction.VERTICAL, 1, false)).getErrorCode());

        assertEquals(PlayerMoveException.ErrorCode.FIRST_NOT_IN_CENTER,
                service.convertException(new IncorrectPositionException(new Position(7, 7), Direction.VERTICAL, 1, true)).getErrorCode());

        assertEquals(PlayerMoveException.ErrorCode.NO_BOARD_TILES,
                service.convertException(new IncorrectTilesException(true)).getErrorCode());

        assertEquals(PlayerMoveException.ErrorCode.NO_HAND_TILES,
                service.convertException(new IncorrectTilesException(false)).getErrorCode());

        assertEquals(PlayerMoveException.ErrorCode.TILE_ALREADY_PLACED,
                service.convertException(new IncorrectTilesException(new Tile(1, 'a', 1), new Position(1, 1), new Position(2, 2))).getErrorCode());

        assertEquals(PlayerMoveException.ErrorCode.UNKNOWN_TILE,
                service.convertException(new IncorrectTilesException(new Tile(1, 'a', 1))).getErrorCode());

        assertEquals(PlayerMoveException.ErrorCode.UNKNOWN_ERROR,
                service.convertException(new GameMoveException("")).getErrorCode());
    }

    @Test
    public void test_makeTurn() throws BoardLoadingException, PlayerMoveException, GameMoveException {
        reset(board);

        expect(roomManager.openBoard(14L)).andReturn(null);
        expect(roomManager.openBoard(14L)).andReturn(board).times(2);
        replay(roomManager);

        Player player = createMock(Player.class);
        expect(player.getId()).andReturn(14L).times(2);
        replay(player);

        final PlayerSessionBean bean = RemoteServiceContextAccessor.expectPlayerSessionBean();
        expect(bean.getPlayer()).andReturn(player).times(3);
        replay(bean);

        try {
            service.makeTurn(14L, word1);
            fail("Exception must be here: IllegalArgumentException - unknown board");
        } catch (IllegalArgumentException ex) {
            ;
        }

        reset(board);
        expect(board.makeMove(isA(MakeWordMove.class))).andThrow(new GameMoveException(""));
        expect(board.makeMove(isA(MakeWordMove.class))).andReturn(98);
        expect(board.getPlayerHand(14L)).andReturn(new ScribblePlayerHand(14L, new Tile(1, 'a', 1), new Tile(2, 'a', 1)));
        expect(board.getPlayerTurn()).andReturn(new ScribblePlayerHand(18L, new Tile[0]));
        replay(board);

        try {
            service.makeTurn(14L, word1);
            fail("Exception must be here: PlayerMoveException unknown error");
        } catch (PlayerMoveException ex) {
            assertEquals(PlayerMoveException.ErrorCode.UNKNOWN_ERROR, ex.getErrorCode());
        }

        final TurnResult result = service.makeTurn(14L, word1);
        assertEquals(2, result.getHandTiles().length);
        assertEquals(98, result.getPoints());
        assertEquals(18L, result.getNextPlayerTurn());

        verify(roomManager, board);
    }

    @Test
    public void test_passTurn() throws BoardLoadingException, PlayerMoveException, GameMoveException {
        reset(board);

        expect(roomManager.openBoard(14L)).andReturn(null);
        expect(roomManager.openBoard(14L)).andReturn(board).times(2);
        replay(roomManager);

        Player player = createMock(Player.class);
        expect(player.getId()).andReturn(14L).times(2);
        replay(player);

        final PlayerSessionBean bean = RemoteServiceContextAccessor.expectPlayerSessionBean();
        expect(bean.getPlayer()).andReturn(player).times(3);
        replay(bean);

        try {
            service.passTurn(14L);
            fail("Exception must be here: IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            ;
        }

        reset(board);
        expect(board.makeMove(new PassTurnMove(14L))).andThrow(new GameMoveException(""));
        expect(board.makeMove(new PassTurnMove(14L))).andReturn(12);
        expect(board.getPlayerTurn()).andReturn(new ScribblePlayerHand(18L));
        replay(board);

        try {
            service.passTurn(14L);
            fail("Exception must be here: PlayerMoveException unknown error");
        } catch (PlayerMoveException ex) {
            assertEquals(PlayerMoveException.ErrorCode.UNKNOWN_ERROR, ex.getErrorCode());
        }

        final TurnResult r = service.passTurn(14L);
        assertEquals(18L, r.getNextPlayerTurn());
        assertEquals(12, r.getPoints());
        assertEquals(0, r.getHandTiles().length);

        verify(roomManager, board);
    }

    @Test
    public void test_exchangeTiles() throws BoardLoadingException, PlayerMoveException, GameMoveException {
        reset(board);

        expect(roomManager.openBoard(14L)).andReturn(null);
        expect(roomManager.openBoard(14L)).andReturn(board).times(2);
        replay(roomManager);

        Player player = createMock(Player.class);
        expect(player.getId()).andReturn(14L).times(2);
        replay(player);

        final PlayerSessionBean bean = RemoteServiceContextAccessor.expectPlayerSessionBean();
        expect(bean.getPlayer()).andReturn(player).times(3);
        replay(bean);

        try {
            service.passTurn(14L);
            fail("Exception must be here: IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            ;
        }

        final int[] tiles = new int[3];

        reset(board);
        expect(board.makeMove(new ExchangeTilesMove(14L, tiles))).andThrow(new GameMoveException(""));
        expect(board.makeMove(new ExchangeTilesMove(14L, tiles))).andReturn(-2);
        expect(board.getPlayerTurn()).andReturn(new ScribblePlayerHand(18L));
        expect(board.getPlayerHand(14L)).andReturn(new ScribblePlayerHand(14L, new Tile(1, 'a', 1), new Tile(2, 'a', 1)));
        replay(board);

        try {
            service.exchangeTiles(14L, tiles);
            fail("Exception must be here: PlayerMoveException unknown error");
        } catch (PlayerMoveException ex) {
            assertEquals(PlayerMoveException.ErrorCode.UNKNOWN_ERROR, ex.getErrorCode());
        }

        final TurnResult r = service.exchangeTiles(14L, tiles);
        assertEquals(18L, r.getNextPlayerTurn());
        assertEquals(-2, r.getPoints());
        assertEquals(2, r.getHandTiles().length);

        verify(roomManager, board);
    }

    @Test
    public void test_resign() throws BoardLoadingException, PlayerMoveException, GameMoveException {
        reset(board);

        expect(roomManager.openBoard(14L)).andReturn(null);
        expect(roomManager.openBoard(14L)).andReturn(board).times(2);
        replay(roomManager);

        final Player player = createMock(Player.class);
        expect(player.getId()).andReturn(13L).times(3);
        replay(player);

        final PlayerSessionBean bean = RemoteServiceContextAccessor.expectPlayerSessionBean();
        expect(bean.getPlayer()).andReturn(player).times(3);
        replay(bean);

        try {
            service.resign(14L);
            fail("Exception must be here: IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            ;
        }

        final ScribblePlayerHand hand = new ScribblePlayerHand(13L);

        reset(board);
        expect(board.getPlayerHand(13L)).andReturn(hand);
        board.resign(hand);
        expectLastCall().andThrow(new GameMoveException(""));
        expect(board.getPlayerHand(13L)).andReturn(hand);
        board.resign(hand);
        replay(board);

        try {
            service.resign(14L);
            fail("Exception must be here: PlayerMoveException unknown error");
        } catch (PlayerMoveException ex) {
            assertEquals(PlayerMoveException.ErrorCode.UNKNOWN_ERROR, ex.getErrorCode());
        }

        service.resign(14L);
        verify(roomManager, board);
    }

    private Player createMockPlayer(long id) {
        Player player = createMock(Player.class);
        expect(player.getId()).andReturn(id).anyTimes();
        expect(player.getNickname()).andReturn("u").anyTimes();
        expect(player.getRating()).andReturn(12).anyTimes();
        replay(player);
        return player;
    }*/
}