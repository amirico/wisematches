package wisematches.server.deprecated.web.modules.app.services;

import wisematches.server.deprecated.web.rpc.GenericSecureRemoteService;

public class PlayboardServiceImpl extends GenericSecureRemoteService {//implements PlayboardService {
/*    private PlayerManager playerManager;
    private RoomManager roomManager;

    private static final Tile[] EMPTY_TILES_ARRAY = new Tile[0];

    private static final Log log = LogFactory.getLog("wisematches.server.web.controlles.playboard");

    public PlayboardServiceImpl() {
    }

    @Transactional(readOnly = true)
    public PlayboardItemBean openBoard(long boardId) {
        final ScribbleBoard board;
        try {
            if (log.isDebugEnabled()) {
                log.debug("Open board: " + boardId);
            }

            board = (ScribbleBoard) roomManager.openBoard(boardId);
            if (board == null) {
                log.warn("Openning board unknown: " + boardId);
                throw new IllegalArgumentException("Specified board is unknown: " + boardId);
            }

            final PlayerOpenBoardsBean sessionBean = getPlayerSessionBean();
            final PlayboardItemBean bean = converBoard(board);
            sessionBean.addOpenedBoard(board);
            return bean;
        } catch (BoardLoadingException e) {
            log.error("Board can't be loaded: " + boardId, e);
            return null;
        }
    }

    @Transactional(readOnly = true)
    public void closeBoard(long boardId) {
        if (log.isDebugEnabled()) {
            log.debug("Cloase board: " + boardId);
        }

        ((PlayerOpenBoardsBean) getPlayerSessionBean()).closeOpenedBoard(boardId);
    }

    @Transactional
    public TurnResult makeTurn(long boardId, Word word) throws PlayerMoveException {
        try {
            final Player player = getPlayer();

            final ScribbleBoard board = getScribbleBoard(boardId);
            final long playerId = player.getId();
            final int points = board.makeMove(new MakeWordMove(playerId, word));
            final Tile[] tiles = board.getPlayerHand(playerId).getTiles();
            return new TurnResult(tiles, points, getNextPlayerTurnId(board));
        } catch (GameMoveException ex) {
            final PlayerMoveException moveException = convertException(ex);
            log.debug("GameMoveException happned: " + ex + " that was converted to " + moveException);
            throw moveException;
        } catch (BoardLoadingException ex) {
            log.error("Board can't be opened", ex);
            throw new IllegalStateException("Board with specified id can't be loaded: " + ex.getMessage());
        }
    }

    @Transactional
    public TurnResult passTurn(long boardId) throws PlayerMoveException {
        try {
            final Player player = getPlayer();

            final ScribbleBoard board = getScribbleBoard(boardId);
            final int points = board.makeMove(new PassTurnMove(player.getId()));
            return new TurnResult(EMPTY_TILES_ARRAY, points, getNextPlayerTurnId(board));
        } catch (GameMoveException e) {
            throw convertException(e);
        } catch (BoardLoadingException ex) {
            log.error("Board can't be opened", ex);
            throw new IllegalStateException("Board with specified id can't be loaded: " + ex.getMessage());
        }
    }

    @Transactional
    public TurnResult exchangeTiles(long boardId, int[] tileIds) throws PlayerMoveException {
        try {
            final Player player = getPlayer();
            final long playerId = player.getId();

            final ScribbleBoard board = getScribbleBoard(boardId);
            final int points = board.makeMove(new ExchangeTilesMove(playerId, tileIds));
            return new TurnResult(board.getPlayerHand(playerId).getTiles(), points, getNextPlayerTurnId(board));
        } catch (GameMoveException e) {
            throw convertException(e);
        } catch (BoardLoadingException ex) {
            log.error("Board can't be opened", ex);
            throw new IllegalStateException("Board with specified id can't be loaded: " + ex.getMessage());
        }
    }

    @Transactional
    public void resign(long boardId) throws PlayerMoveException {
        try {
            final Player player = getPlayer();

            final ScribbleBoard board = (ScribbleBoard) roomManager.openBoard(boardId);
            if (board == null) {
                throw new IllegalArgumentException("Specified board is unknown: " + boardId);
            }
            final ScribblePlayerHand hand = board.getPlayerHand(player.getId());
            if (hand == null) {
                throw new IllegalArgumentException("You are not in specified game: " + boardId);
            }
            board.close(hand);
        } catch (GameMoveException e) {
            throw convertException(e);
        } catch (BoardLoadingException ex) {
            log.error("Board can't be opened", ex);
            throw new IllegalStateException("Board with specified id can't be loaded: " + ex.getMessage());
        }
    }

    *//**
	 * Loads, checks and returns {@code ScribbleBoard}.
	 * <p/>
	 * This metohd check that board exist and that current player can change it.
	 *
	 * @param boardId the board to load
	 * @return the loaded board.
	 * @throws wisematches.server.gameplaying.room.board.BoardLoadingException	 if board can't be loaded.
	 * @throws wisematches.server.gameplaying.board.UnsuitablePlayerException if current player can't change a board.
	 *//*
    private ScribbleBoard getScribbleBoard(long boardId) throws BoardLoadingException, UnsuitablePlayerException {
        final ScribbleBoard board = (ScribbleBoard) roomManager.openBoard(boardId);
        if (board == null) {
            throw new IllegalArgumentException("Specified board is unknown: " + boardId);
        }
        return board;
    }

    private long getNextPlayerTurnId(ScribbleBoard board) {
        long playerTurn = 0;
        final ScribblePlayerHand hand = board.getPlayerTurn();
        if (hand != null) {
            playerTurn = hand.getPlayerId();
        }
        return playerTurn;
    }

    protected PlayerMoveException convertException(GameMoveException exception) {
        try {
            throw exception;
        } catch (UnsuitablePlayerException ex) {
            return new PlayerMoveException(PlayerMoveException.ErrorCode.UNSUITABLE_PLAYER);
        } catch (GameNotReadyException ex) {
            return new PlayerMoveException(PlayerMoveException.ErrorCode.GAME_NOT_READY);
        } catch (GameFinishedException ex) {
            return new PlayerMoveException(PlayerMoveException.ErrorCode.GAME_FINISHED);
        } catch (UnknownWordException ex) {
            return new PlayerMoveException(PlayerMoveException.ErrorCode.UNKNOWN_WORD);
        } catch (IncorrectPositionException ex) {
            if (ex.isMustBeInCenter()) {
                return new PlayerMoveException(PlayerMoveException.ErrorCode.FIRST_NOT_IN_CENTER);
            } else {
                return new PlayerMoveException(PlayerMoveException.ErrorCode.INCORRECT_POSITION);
            }
        } catch (IncorrectTilesException ex) {
            switch (ex.getType()) {
                case CELL_ALREADY_BUSY:
                    return new PlayerMoveException(PlayerMoveException.ErrorCode.CELL_ALREADY_BUSY);
                case NO_BOARD_TILES:
                    return new PlayerMoveException(PlayerMoveException.ErrorCode.NO_BOARD_TILES);
                case NO_HAND_TILES:
                    return new PlayerMoveException(PlayerMoveException.ErrorCode.NO_HAND_TILES);
                case TILE_ALREADY_PLACED:
                    return new PlayerMoveException(PlayerMoveException.ErrorCode.TILE_ALREADY_PLACED);
                case UNKNOWN_TILE:
                    return new PlayerMoveException(PlayerMoveException.ErrorCode.UNKNOWN_TILE);
            }
        } catch (GameMoveException ex) {
            return new PlayerMoveException(PlayerMoveException.ErrorCode.UNKNOWN_ERROR);
        }
        return new PlayerMoveException(PlayerMoveException.ErrorCode.UNKNOWN_ERROR);
    }

    protected PlayboardItemBean converBoard(ScribbleBoard board) {
        final Player player = getPlayer();

        final PlayboardItemBean bean = new PlayboardItemBean();
        GameboardServiceImpl.convertGameBoard(board, bean, playerManager);

        bean.setHandTiles(board.getPlayerHand(player.getId()).getTiles());

        int i = 0;
        final ScribbleSettings settings = board.getGameSettings();
        final List<ScribblePlayerHand> playersHands = board.getPlayersHands();
        int[] tiles = new int[settings.getMaxPlayers()];
        for (ScribblePlayerHand playersHand : playersHands) {
            tiles[i++] = playersHand.getTiles().length;
        }
        bean.setPlayersTilesCount(tiles);

        final List<GameMove> list = board.getGameMoves();
        final List<PlayerMoveBean> words = new ArrayList<PlayerMoveBean>(list.size());
        for (GameMove gameMove : list) {
            words.add(GameBoardEventProducer.convertGameMove(gameMove));
        }
        bean.setPlayersMoves(words);

        final TilesBank.TilesInfo[] tilesInfos = board.getTilesBankInfo();
        final Tile[] bankTiles = new Tile[tilesInfos.length];
        for (int i1 = 0; i1 < tilesInfos.length; i1++) {
            final TilesBank.TilesInfo ti = tilesInfos[i1];
            bankTiles[i1] = new Tile(ti.getCount(), ti.getLetter(), ti.getCost());
        }
        bean.setBankTiles(bankTiles);
        return bean;
    }

    public void setRoomsManager(RoomsManager roomsManager) {
        roomManager = (ScribbleBoardManager) roomsManager.getRoomManager(ScribbleBoardManager.ROOM);
    }

    public void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }*/
}