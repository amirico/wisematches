package wisematches.client.gwt.app.client.events.board;

import wisematches.client.gwt.app.client.content.playboard.PlayerMoveBean;

/**
 * Indicates that player has made a move. Contains information about move and next player.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class GameTurnEvent extends GameBoardEvent {
    private long nextPlayer;
    private int handTilesCount;
    private PlayerMoveBean playerMoveBean;

    /**
     * This is GWT Serialization only constructor.
     */
    @Deprecated
    public GameTurnEvent() {
    }

    /**
     * Creates new event with specified parameters. This constructor is used to create move that has not next player
     * (last move). In this case {@code nextPlayer} is zero.
     *
     * @param boardId        the board id.
     * @param playerMoveBean the information about made move.
     * @throws IllegalArgumentException if {@code handTilesCount} is negative
     * @throws NullPointerException     if {@code playerMoveBean} if null
     */
    public GameTurnEvent(long boardId, int handTilesCount, PlayerMoveBean playerMoveBean) {
        this(boardId, 0, handTilesCount, playerMoveBean);
    }

    /**
     * Creates new event with specified parameters.
     *
     * @param boardId        the board id.
     * @param nextPlayer     the player whos turn next. If no next player zero should be passed here.
     * @param handTilesCount the number of hand tiles for player who made a move.
     * @param playerMoveBean the information about made move.
     * @throws IllegalArgumentException if {@code handTilesCount} is negative
     * @throws NullPointerException     if {@code playerMoveBean} if null
     */
    public GameTurnEvent(long boardId, long nextPlayer, int handTilesCount, PlayerMoveBean playerMoveBean) {
        super(boardId);
        if (handTilesCount < 0) {
            throw new IllegalArgumentException("Hand tiles count is negative");
        }
        if (playerMoveBean == null) {
            throw new NullPointerException("PlayerMoveBean is null");
        }

        this.nextPlayer = nextPlayer;
        this.handTilesCount = handTilesCount;
        this.playerMoveBean = playerMoveBean;
    }

    /**
     * Returns player whos move next or {@code zero} if no next layer (game was finished, for example).
     *
     * @return the player whos move next or {@code zero} if no next layer (game was finished, for example).
     */
    public long getNextPlayer() {
        return nextPlayer;
    }

    /**
     * Returns id of player who made a move.
     *
     * @return the id of player who made a move.
     */
    public long getPlayerId() {
        return playerMoveBean.getPlayerId();
    }

    /**
     * Returns number of tile in hand after made move.
     *
     * @return the number of tiles in hand after made move.
     */
    public int getHandTilesCount() {
        return handTilesCount;
    }

    /**
     * Returns information about maden move.
     *
     * @return the information about maden move.
     */
    public PlayerMoveBean getPlayerMoveBean() {
        return playerMoveBean;
    }
}
