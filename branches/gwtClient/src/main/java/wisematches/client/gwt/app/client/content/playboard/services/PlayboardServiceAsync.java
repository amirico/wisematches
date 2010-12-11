package wisematches.client.gwt.app.client.content.playboard.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import wisematches.client.gwt.app.client.content.playboard.PlayboardItemBean;
import wisematches.client.gwt.app.client.content.playboard.TurnResult;
import wisematches.server.games.scribble.core.Word;

public interface PlayboardServiceAsync {

    /**
     * Loads state of game with specified id.
     *
     * @param boardId the id of board to be loaded.
     * @return the loaded item or <code>null</code> if playboard is unknown.
     */
    void openBoard(long boardId, AsyncCallback<PlayboardItemBean> async);

    /**
     * Indicates that player close board with specified id and it can be closed on server.
     *
     * @param boardId the id of closed board.
     */
    void closeBoard(long boardId, AsyncCallback<Void> async);

    /**
     * Makes turn on specified board and returns tiles that should be added into hand.
     *
     * @param boardId the board id where move should be maden.
     * @param word    the maden word.
     * @return the array of tiles that should be added into hand.
     * @throws wisematches.client.gwt.app.client.content.playboard.PlayerMoveException
     *          if word is incorrect or move can not be
     *          maden by some reasones.
     * @see wisematches.server.core.board.GameBoard#makeMove(wisematches.server.core.board.PlayerMove)
     */
    void makeTurn(long boardId, Word word, AsyncCallback<TurnResult> async);

    /**
     * Pass turn on specified board and return operation result.
     *
     * @param boardId the board if where turn should be passed.
     * @return the pass result.
     * @throws wisematches.client.gwt.app.client.content.playboard.PlayerMoveException
     *          if word is incorrect or move can not be
     *          maden by some reasones.
     * @see wisematches.server.core.board.GameBoard#passTurn()
     */
    void passTurn(long boardId, AsyncCallback<TurnResult> async);

    void exchangeTiles(long boardId, int[] tileIds, AsyncCallback<TurnResult> async);

    /**
     * Resigns game with specified id.
     *
     * @param boardId the board id that should be resigned.
     * @throws wisematches.client.gwt.app.client.content.playboard.PlayerMoveException
     *
     * @see wisematches.server.core.board.GameBoard#close(wisematches.server.core.board.GamePlayerHand)
     */
    void resign(long boardId, AsyncCallback<Void> async);
}
