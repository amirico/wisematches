package wisematches.client.gwt.app.client.content.playboard.board;

/**
 * Indicates that position of tile on the board are incorrect.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class IncorrectPositionException extends BoardException {
    public IncorrectPositionException(String message) {
        super(message);
    }

    public IncorrectPositionException(int row, int column, boolean busyCell) {
        this(getErrorMessage(row, column, busyCell));
    }

    private static String getErrorMessage(int row, int column, boolean busyCell) {
        if (busyCell) {
            return "Cell at " + row + " " + column + " is busy";
        } else {
            return "No tile at " + row + " " + column;
        }
    }
}
