package wisematches.server.core.timeouts;

import wisematches.server.core.room.Room;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class GameTimeoutEvent {
    private final Room room;
    private final long boardId;
    private final RemainderType remainderType;

    public GameTimeoutEvent(Room room, long boardId, RemainderType remainderType) {
        this.room = room;
        this.boardId = boardId;
        this.remainderType = remainderType;
    }

    public Room getRoom() {
        return room;
    }

    public long getBoardId() {
        return boardId;
    }

    public RemainderType getRemainderType() {
        return remainderType;
    }

    @Override
    public String toString() {
        return "GameTimeoutEvent{" +
                "room=" + room +
                ", boardId=" + boardId +
                ", remainderType=" + remainderType +
                '}';
    }
}
