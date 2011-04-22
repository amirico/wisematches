package wisematches.server.gameplaying.room;

import wisematches.server.gameplaying.board.GameBoard;
import wisematches.server.gameplaying.board.GameSettings;
import wisematches.server.gameplaying.propose.GameProposal;

/**
 * {@code Room} interface is marker interface that allows identify a room. There is no implementation for
 * this interface and only extension is required because class object is used.
 *
 * @param <P> the game proposal class name.
 * @param <S> the game settings class name.
 * @param <B> the game board
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface Room<S extends GameSettings, P extends GameProposal<S>, B extends GameBoard<S, ?>> {
}