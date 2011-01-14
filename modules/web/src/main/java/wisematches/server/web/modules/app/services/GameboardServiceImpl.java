package wisematches.server.web.modules.app.services;

import wisematches.server.web.rpc.GenericSecureRemoteService;

public class GameboardServiceImpl extends GenericSecureRemoteService { //implements GameboardService {
/*    private ScribbleRoomManager roomManager;
    private PlayerManager playerManager;

    private static final GameboardItemBean[] EMTY_BEANS_ARRAY = new GameboardItemBean[0];

    @Transactional(readOnly = true)
    public GameboardItemBean[] loadPlayerGames(long playerId) {
        final Player player = playerManager.getPlayer(playerId);

        final Collection<ScribbleBoard> activeBoards = roomManager.getActiveBoards(player);
        if (activeBoards.size() == 0) {
            return EMTY_BEANS_ARRAY;
        }

        int index = 0;
        final GameboardItemBean[] res = new GameboardItemBean[activeBoards.size()];
        for (ScribbleBoard board : activeBoards) {
            res[index++] = convertGameBoard(board, new GameboardItemBean(), playerManager);
        }
        return res;
    }

    protected static GameboardItemBean convertGameBoard(ScribbleBoard board, GameboardItemBean bean, PlayerManager manager) {
        final ScribbleSettings settings = board.getGameSettings();
        final int daysPerMove = settings.getDaysPerMove();

        bean.setBoardId(board.getBoardId());
        bean.setDaysPerMove(daysPerMove);
        bean.setLocale(settings.getLanguage());

        bean.setStartedTime(board.getStartedTime());
        bean.setFinishedTime(board.getFinishedTime());

        final ScribblePlayerHand playerTurn = board.getPlayerTrun();
        if (playerTurn != null) {
            bean.setPlayerMove(playerTurn.getPlayerId());

            if (daysPerMove != 0) {
                bean.setLastMoveTime(board.getLastMoveTime());
            } else {
                bean.setLastMoveTime(0);
            }
        }

        int index = 0;
        final PlayerInfoBean[] opponents = new PlayerInfoBean[settings.getMaxPlayers()];
        final List<ScribblePlayerHand> playersHands = board.getPlayersHands();
        for (ScribblePlayerHand hand : playersHands) {
            final long playerId = hand.getPlayerId();
            final Player player = manager.getPlayer(playerId);
            opponents[index++] = new PlayerInfoBean(playerId, player.getNickname(), getMemberType(player), hand.getPoints());
        }

        bean.setPlayers(opponents);
        bean.setTitle(settings.getTitle());
        return bean;
    }

    protected static PlayerInfoBean convertPlayer(Player player, GameBoard gameBoard) {
        final long id = player.getId();
        final GamePlayerHand hand = gameBoard.getPlayerHand(id);
        return new PlayerInfoBean(id, player.getNickname(), getMemberType(player), hand.getPoints());
    }

    public void setRoomsManager(RoomsManager roomsManager) {
        roomManager = (ScribbleRoomManager) roomsManager.getRoomManager(ScribbleRoomManager.ROOM);
    }

    public void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }*/
}