package wisematches.client.gwt.app.client.content.playboard.services;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import wisematches.client.gwt.app.client.content.playboard.PlayboardItemBean;
import wisematches.client.gwt.app.client.content.playboard.PlayerMoveException;
import wisematches.client.gwt.app.client.content.playboard.TurnResult;
import wisematches.server.games.scribble.core.Word;

public interface PlayboardService extends RemoteService {
    /**
     * Loads state of game with specified id.
     *
     * @param boardId the id of board to be loaded.
     * @return the loaded item or <code>null</code> if playboard is unknown.
     */
    PlayboardItemBean openBoard(long boardId);

    /**
     * Indicates that player close board with specified id and it can be closed on server.
     *
     * @param boardId the id of closed board.
     */
    void closeBoard(long boardId);


    /**
     * Makes turn on specified board and returns tiles that should be added into hand.
     *
     * @param boardId the board id where move should be maden.
     * @param word    the maden word.
     * @return the array of tiles that should be added into hand.
     * @throws PlayerMoveException if word is incorrect or move can not be
     *                             maden by some reasones.
     * @see wisematches.server.core.board.GameBoard#makeMove(wisematches.server.core.board.PlayerMove)
     */
    TurnResult makeTurn(long boardId, Word word) throws PlayerMoveException;

    /**
     * Pass turn on specified board and return operation result.
     *
     * @param boardId the board if where turn should be passed.
     * @return the pass result.
     * @throws PlayerMoveException if turn can't be passed or move can not be maden by some reasones.
     * @see wisematches.server.core.board.GameBoard#passTurn()
     */
    TurnResult passTurn(long boardId) throws PlayerMoveException;

    /**
     * Exchange tiles from hand as current turn.
     *
     * @param boardId the board if where turn should be passed.
     * @param tileIds the number of tiles to exchange
     * @return the exchange result.
     * @throws PlayerMoveException  if number of tiles are incorrect or move can not be
     *                              maden by some reasones.
     * @throws NullPointerException is {@code tileIds} is {@code null}
     */
    TurnResult exchangeTiles(long boardId, int[] tileIds) throws PlayerMoveException;

    /**
     * Resigns game with specified id.
     *
     * @param boardId the board id that should be resigned.
     * @throws PlayerMoveException
     * @see wisematches.server.core.board.GameBoard#close(wisematches.server.core.board.GamePlayerHand)
     */
    void resign(long boardId) throws PlayerMoveException;


    /**
     * Utility/Convenience class.
     * Use PlayboardService.App.getInstance() to access static instance of PlayboardServiceAsync
     */
    public static class App {
        private static final PlayboardServiceAsync ourInstance;

        static {
            ourInstance = (PlayboardServiceAsync) GWT.create(PlayboardService.class);
            ((ServiceDefTarget) ourInstance).setServiceEntryPoint("/rpc/PlayboardService");
        }

        public static PlayboardServiceAsync getInstance() {
            return ourInstance;
        }
    }
}