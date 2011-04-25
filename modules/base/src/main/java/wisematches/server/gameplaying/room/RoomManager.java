package wisematches.server.gameplaying.room;

import wisematches.server.gameplaying.board.BoardManager;
import wisematches.server.gameplaying.board.GameBoard;
import wisematches.server.gameplaying.board.GameSettings;
import wisematches.server.gameplaying.propose.GameProposalManager;
import wisematches.server.gameplaying.search.BoardsSearchEngine;

/**
 * <code>RoomManager</code> allows manage a games in a room.
 * <p/>
 * <code>RoomManager</code> is a user-interface and delegates all work to  <code>GameController</code> but hides it
 * from users.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface RoomManager<S extends GameSettings, B extends GameBoard<S, ?>> {
    /**
     * Returns type of this room.
     *
     * @return the type of this room.
     */
    Room<S, B> getRoomType();

    /**
     * Returns board manager for this room manager.
     *
     * @return the board manager.
     */
    BoardManager<S, B> getBoardManager();

    /**
     * Returns searches engine for this room. Searches engine allow do boards search by some criteria,
     * for example expired boards.
     *
     * @return the search engine.
     */
    BoardsSearchEngine getSearchesEngine();

    /**
     * Returns game proposals manager for this room.
     *
     * @return the games proposals manager.
     */
    GameProposalManager<S> getProposalManager();
}
