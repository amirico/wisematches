package wisematches.server.gameplaying.room;

import wisematches.server.gameplaying.board.GameBoard;
import wisematches.server.gameplaying.board.GameSettings;

/**
 * {@code Room} interface is marker interface that allows identify a room. There is no implementation for
 * this interface and only extension is required because class object is used.
 *
 * @param <P> the game proposal class name.
 * @param <S> the game settings class name.
 * @param <B> the game board
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface Room<S extends GameSettings, B extends GameBoard<S, ?>> {
}