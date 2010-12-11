package wisematches.client.gwt.app.client.events.board;

import wisematches.server.games.scribble.core.Tile;

/**
 * Indicates that game was started. Contains additional information about game: who has a turn and so on.
 * <p/>
 * This event is used for broadcast and direct sending. This event is sent to each player in game
 * with information about hand tiles and the event is sent to all if hand tiles is not specified.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class GameStartedEvent extends GameBoardEvent {
    private long playerTrun;
    private long startTime;

    private long playerHand;
    private Tile[] playerTiles;

    /**
     * This is GWT Serialization only constructor.
     */
    @Deprecated
    public GameStartedEvent() {
    }

    /**
     * Creates new broadcast event.
     *
     * @param boardId    the board id.
     * @param playerTrun the player who has a turn.
     * @param startTime  the game start time.
     */
    public GameStartedEvent(long boardId, long playerTrun, long startTime) {
        super(boardId);
        this.playerTrun = playerTrun;
        this.startTime = startTime;
        this.playerHand = 0;
        this.playerTiles = null;
    }

    /**
     * Creates direct event that will be sent to {@code playerHand} player only. This event contains
     * information about player hand tile.
     *
     * @param boardId     the board id.
     * @param playerTrun  the player who has a turn.
     * @param startTime   the game start time.
     * @param playerHand  the id of player whose tiles are contained in this event and whose this event should be sent.
     * @param playerTiles the array of player's hand tiles assigned to him after starting.
     * @throws IllegalArgumentException if {@code playerhand} is zero.
     * @throws NullPointerException     if {@code playerTiles} is null.
     */
    public GameStartedEvent(long boardId, long playerTrun, long startTime, long playerHand, Tile[] playerTiles) {
        super(boardId);
        if (playerHand == 0) {
            throw new IllegalArgumentException("Player hand is zero");
        }
        if (playerTiles == null) {
            throw new NullPointerException("Player hand tiles are null");
        }
        this.playerTrun = playerTrun;
        this.startTime = startTime;
        this.playerHand = playerHand;
        this.playerTiles = playerTiles;
    }

    /**
     * Returns player who has a turn.
     *
     * @return the player who has a turn or zero if activate player is not specified yet.
     */
    public long getPlayerTrun() {
        return playerTrun;
    }

    /**
     * Returns time when game was started.
     *
     * @return the time when game was started.
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Returns id of player whose hand tiles associated with this event. If it's broadcast event
     * this method return zero.
     *
     * @return the player whose hand tiles associated or zero if no hand tiles.
     */
    public long getPlayerHand() {
        return playerHand;
    }

    /**
     * Returns hand tiles of player.
     *
     * @return the hand tiles of player or {@code null} if it's broadcast event and there is no associated player.
     */
    public Tile[] getPlayerTiles() {
        return playerTiles;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@code true} if this is direct event and belongs to specified player or if it's broadcast event;
     *         {@code false} if this is direct event but does not belong to specified player.
     */
    @Override
    public boolean isEventForPlayer(long playerId) {
        return playerHand == 0 || playerHand == playerId;
    }
}
