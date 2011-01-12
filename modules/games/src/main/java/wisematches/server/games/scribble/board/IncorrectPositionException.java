package wisematches.server.games.scribble.board;

import wisematches.server.core.board.IncorrectMoveException;
import wisematches.server.games.scribble.Direction;
import wisematches.server.games.scribble.Position;

/**
 * This exception is indicates that word placed in incorrect position. This exception contains information
 * about word's position.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class IncorrectPositionException extends IncorrectMoveException {
    private final Position position;
    private final Direction direction;
    private final int length;
    private final boolean mustBeInCenter;

    /**
     * Creates new exception with information about incorrect word position.
     *
     * @param position       the word position.
     * @param direction      the direction.
     * @param length         the length of word.
     * @param mustBeInCenter indicates that word must be placed at center cell.
     */
    public IncorrectPositionException(Position position, Direction direction, int length, boolean mustBeInCenter) {
        super(createMessage(position, direction, length, mustBeInCenter));
        this.position = position;
        this.direction = direction;
        this.length = length;
        this.mustBeInCenter = mustBeInCenter;
    }

    public Position getPosition() {
        return position;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getLength() {
        return length;
    }

    public boolean isMustBeInCenter() {
        return mustBeInCenter;
    }

    private static String createMessage(Position position, Direction direction, int length, boolean mustBeInCenter) {
        final StringBuilder res = new StringBuilder();
        res.append("Position of word are incorrect. ");

        if (mustBeInCenter) {
            res.append("First word must be placed at center cell");
        } else {
            res.append("Move is placed outside of board ");
        }
        res.append(" but placed at ");
        res.append(position);
        res.append(" ");
        res.append(direction.name().toLowerCase());
        res.append(" and has length ");
        res.append(length);

        return res.toString();
    }
}