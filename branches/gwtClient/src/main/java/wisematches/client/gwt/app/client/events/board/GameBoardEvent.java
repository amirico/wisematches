package wisematches.client.gwt.app.client.events.board;

import wisematches.client.gwt.core.client.events.AbstractEvent;

/**
 * This is base class for all game related events, like game started, finished and so on.
 * <p/>
 * Each {@code GameBoardEvent} contains number of board and it's title. The title is used to show notification
 * messages if board is not loaded yet.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class GameBoardEvent extends AbstractEvent {
    private long boardId;

    /**
     * This is GWT Serialization only constructor.
     */
    @Deprecated
    public GameBoardEvent() {
    }

    public GameBoardEvent(long boardId) {
        this.boardId = boardId;
    }

    public long getBoardId() {
        return boardId;
    }
}
