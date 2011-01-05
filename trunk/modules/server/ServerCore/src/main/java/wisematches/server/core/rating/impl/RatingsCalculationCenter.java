package wisematches.server.core.rating.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import wisematches.kernel.player.Player;
import wisematches.server.core.account.PlayerManager;
import wisematches.server.core.board.GameBoard;
import wisematches.server.core.board.GamePlayerHand;
import wisematches.server.core.board.GameStateListener;
import wisematches.server.core.rating.PlayerRatingEvent;
import wisematches.server.core.rating.PlayerRatingListener;
import wisematches.server.core.rating.impl.systems.ELORatingSystem;
import wisematches.server.core.room.*;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * This manager calculates rating and updates associated objects. It has a listeners that is invoked when
 * player's rating is updated to notify all interested clients.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RatingsCalculationCenter {
    private RoomsManager roomsManager;
    private PlayerManager playerManager;

    private RatingSystem ratingSystem = new ELORatingSystem();
    private final RoomBoardsListener roomBoardsListener = new TheRoomBoardsListener();

    private final Collection<PlayerRatingListener> listeners = new CopyOnWriteArraySet<PlayerRatingListener>();

    private static final Log log = LogFactory.getLog(RatingsCalculationCenter.class);
    private PlatformTransactionManager transactionManager;

    public RatingsCalculationCenter() {
    }

    public void addRatingsChangeListener(PlayerRatingListener l) {
        listeners.add(l);
    }

    public void removeRatingsChangeListener(PlayerRatingListener l) {
        listeners.remove(l);
    }

    private void updatePlayerRatings(final Room room, final GameBoard board, final GamePlayerHand interrupterPlayer) {
        if (!board.isRatedGame()) { // If game is not rated just ignore it
            return;
        }

        @SuppressWarnings("unchecked")
        final List<GamePlayerHand> hands = board.getPlayersHands();

        final int[] points = new int[hands.size()];
        final Player[] players = new Player[hands.size()];

        for (int i = 0; i < hands.size(); i++) {
            final GamePlayerHand hand = hands.get(i);
            points[i] = hand.getPoints();
            if (interrupterPlayer == hand) {
                points[i] = 0;
            }
            players[i] = playerManager.getPlayer(hand.getPlayerId());
        }

        final int[] oldRatings = new int[players.length];
        final int[] newRatings = ratingSystem.calculateRatings(players, points);

        final TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        try {
            for (int i = 0; i < players.length; i++) {
                final Player player = players[i];
                final GamePlayerHand hand = hands.get(i);

                final int oldRating = player.getRating();
                int newRating = newRatings[i];
                if (newRating < 0) {
                    newRating = 0;
                }
                final int ratingDelta = newRating - oldRating;

                oldRatings[i] = oldRating;

                player.setRating(newRating);
                hand.updateRating(oldRating, ratingDelta);

                playerManager.updatePlayer(player);
            }
            updateGameBoard(room, board);
            transactionManager.commit(status);
        } catch (Throwable th) {
            log.fatal("Player ratings can't be updated", th);
            transactionManager.rollback(status);
        }

        for (int i = 0; i < players.length; i++) {
            firePlayerRaitingChanged(players[i], board, oldRatings[i], newRatings[i]);
        }
    }

    @SuppressWarnings("unchecked")
    private void updateGameBoard(Room room, GameBoard board) {
        final RoomManager roomManager = roomsManager.getRoomManager(room);
        roomManager.updateBoard(board);
    }

    protected void firePlayerRaitingChanged(Player player, GameBoard board, int oldRating, int newRating) {
        final PlayerRatingEvent e = new PlayerRatingEvent(player, board, oldRating, newRating);
        for (PlayerRatingListener listener : listeners) {
            listener.playerRaitingChanged(e);
        }
    }

    public void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    public void setRoomsManager(RoomsManager roomsManager) {
        this.roomsManager = roomsManager;

        final Collection<RoomManager> collection = roomsManager.getRoomManagers();
        for (RoomManager roomManager : collection) {
            roomManager.addRoomBoardsListener(roomBoardsListener);

            final Room type = roomManager.getRoomType();

            @SuppressWarnings("unchecked")
            final Collection<GameBoard> openedBoards = roomManager.getOpenedBoards();
            for (GameBoard openedBoard : openedBoards) {
                openedBoard.addGameStateListener(new TheGameStateListener(type));
            }
        }
    }

    public void setRatingSystem(RatingSystem ratingSystem) {
        this.ratingSystem = ratingSystem;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    private final class TheRoomBoardsListener implements RoomBoardsListener {
        public void boardOpened(Room room, long boardId) {
            try {
                final GameBoard board = roomsManager.getRoomManager(room).openBoard(boardId);
                board.addGameStateListener(new TheGameStateListener(room));
            } catch (BoardLoadingException ex) {
                log.error("Board can't loaded in boardOpened method of RoomBoardsListener", ex);
            }
        }

        public void boardClosed(Room room, long boardId) {
        }
    }

    private final class TheGameStateListener implements GameStateListener {
        private final Room room;

        private TheGameStateListener(Room room) {
            this.room = room;
        }

        public void gameStarted(GameBoard board, GamePlayerHand playerTurn) {
            // Nothing to do
        }

        public void gameFinished(GameBoard board, GamePlayerHand wonPlayer) {
            updatePlayerRatings(room, board, null);
        }

        public void gameDraw(GameBoard board) {
            updatePlayerRatings(room, board, null);
        }

        public void gameInterrupted(GameBoard board, GamePlayerHand interrupterPlayer, boolean byTimeout) {
            updatePlayerRatings(room, board, interrupterPlayer);
        }
    }
}
