package wisematches.server.web.modules.app.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;
import wisematches.kernel.player.Player;
import wisematches.server.core.account.PlayerManager;
import wisematches.server.core.board.TooManyPlayersException;
import wisematches.server.core.guest.GuestPlayer;
import wisematches.server.core.robot.RobotPlayer;
import wisematches.server.core.robot.RobotType;
import wisematches.server.core.room.BoardCreationException;
import wisematches.server.core.room.BoardLoadingException;
import wisematches.server.core.room.RoomsManager;
import wisematches.server.games.scribble.board.ScribbleBoard;
import wisematches.server.games.scribble.board.ScribbleRoomManager;
import wisematches.server.games.scribble.board.ScribbleSettings;
import wisematches.server.web.modules.app.events.producers.GameBoardEventProducer;
import wisematches.server.web.rpc.GenericSecureRemoteService;

import java.util.Collection;
import java.util.Date;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class DashboardServiceImpl extends GenericSecureRemoteService {//implements DashboardService {
/*    private PlayerManager playerManager;
    private ScribbleRoomManager roomManager;

    private static final DashboardItemBean[] EMPTY_BEANS_ARRAY = new DashboardItemBean[0];

    private static final Log log = LogFactory.getLog("wisematches.server.web.service.dashboard");

    public DashboardServiceImpl() {
    }

    @Transactional
    public Long createBoard(DashboardItemBean bean) {
        if (log.isDebugEnabled()) {
            log.debug("Create new board: " + bean);
        }

        final PlayerInfoBean[] playerInfoBeans = bean.getPlayers();
        final ScribbleSettings settings = new ScribbleSettings(bean.getTitle(), new Date(),
                playerInfoBeans.length + 1, bean.getLocale(), bean.getDaysPerMove(),
                bean.getMinRating(), bean.getMaxRating());


        final Player player = getPlayer();
        if (player instanceof GuestPlayer) {
            assertGuestPlayer(playerInfoBeans);
        }

        try {
            final ScribbleBoard board = roomManager.createBoard(player, settings);
            for (PlayerInfoBean playerInfoBean : playerInfoBeans) {
                if (playerInfoBean != null) {
                    final Player player1 = playerManager.getPlayer(playerInfoBean.getPlayerId());
                    if (player1 != null) {
                        board.addPlayer(player1);
                    } else {
                        log.error("Unknown player with id: " + playerInfoBean.getPlayerId());
                    }
                }
            }
            return board.getBoardId();
        } catch (BoardCreationException e) {
            log.error("Board can't be created", e);
            return null;
        }
    }

    @Transactional
    public JoinResult joinToBoard(long boardId) {
        final Player player = getPlayer();
        if (player instanceof GuestPlayer) {
            throw new GuestRestrictionException("Guest can't join to any boards");
        }

        try {
            final ScribbleBoard board = roomManager.openBoard(boardId);
            board.addPlayer(player);
            return JoinResult.SUCCESS;
        } catch (BoardLoadingException e) {
            return JoinResult.UNKNOWN_GAME;
        } catch (TooManyPlayersException e) {
            return JoinResult.TO_MANY_PLAYERS;
        } catch (IllegalArgumentException e) {
            return JoinResult.ALREDY_IN_GAME;
        } catch (IllegalStateException e) {
            log.error("Player " + player.getId() + " can't be joined to board: " + boardId, e);
            return JoinResult.INTERNAL_ERROR;
        }
    }

    @Transactional(readOnly = true)
    public DashboardItemBean[] getWaitingDashboardItems() {
        if (log.isDebugEnabled()) {
            log.debug("Loading waiting games");
        }

        final Collection<ScribbleBoard> waitingBoards = roomManager.getWaitingBoards();
        if (log.isDebugEnabled()) {
            log.debug("Waiting games count: " + waitingBoards.size());
        }

        if (waitingBoards.size() == 0) {
            return EMPTY_BEANS_ARRAY;
        }

        int index = 0;
        final DashboardItemBean[] beans = new DashboardItemBean[waitingBoards.size()];
        for (ScribbleBoard board : waitingBoards) {
            final DashboardItemBean itemBean = GameBoardEventProducer.convertDashboard(board, playerManager);
            if (log.isDebugEnabled()) {
                log.debug("Waiting board " + index + ": " + itemBean);
            }
            beans[index++] = itemBean;
        }
        return beans;
    }

    protected void assertGuestPlayer(PlayerInfoBean[] playerInfoBeans) {
        if (playerInfoBeans.length != 1) {
            throw new GuestRestrictionException("Guest can play only with one opponent");
        }

        final PlayerInfoBean opponent = playerInfoBeans[0];
        if (opponent == null) {
            throw new GuestRestrictionException("Guest can't wait other players");
        }

        final Player player1 = playerManager.getPlayer(opponent.getPlayerId());
        if (player1 == null || !(player1 instanceof RobotPlayer)) {
            throw new GuestRestrictionException("Guest player can play only with robot Dull");
        }

        final RobotPlayer robot = (RobotPlayer) player1;
        if (robot.getRobotType() != RobotType.DULL) {
            throw new GuestRestrictionException("Guest player can play only with robot Dull");
        }
    }

    public void setRoomsManager(RoomsManager roomsManager) {
        roomManager = (ScribbleRoomManager) roomsManager.getRoomManager(ScribbleRoomManager.ROOM);
    }

    public void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }*/
}