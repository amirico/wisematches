package wisematches.client.gwt.app.client.content.playboard;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerMoveException extends Exception implements IsSerializable {
    private ErrorCode errorCode = ErrorCode.UNKNOWN_ERROR;

    /**
     * GWT serialization constructor
     */
    PlayerMoveException() {
    }

    public PlayerMoveException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public PlayerMoveException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public static enum ErrorCode {
        UNSUITABLE_PLAYER,
        GAME_NOT_READY,
        GAME_FINISHED,
        UNKNOWN_WORD,
        INCORRECT_POSITION,
        FIRST_NOT_IN_CENTER,
        NO_BOARD_TILES,
        NO_HAND_TILES,
        UNKNOWN_TILE,
        TILE_ALREADY_PLACED,
        CELL_ALREADY_BUSY,
        UNKNOWN_ERROR
    }
}
