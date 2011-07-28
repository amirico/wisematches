package wisematches.server.web.services.comment;

import wisematches.personality.player.Player;
import wisematches.playground.scribble.ScribbleBoard;

/**
 * Scribble comments manager. Allows add/remove and receive comments.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface ScribbleCommentManager {
    /**
     * Adds new comment to specified board by specified player.
     *
     * @param player the player who wants add a comment.
     * @param board  the board
     * @param text   the text
     * @return create scribble comment
     */
    ScribbleComment addComment(Player player, ScribbleBoard board, String text);

    /**
     * Removes a comment that should be removed.
     *
     * @param player    the player who wants remove the comment.
     * @param board     the board
     * @param commentId the comment id.
     */
    void removeComment(Player player, ScribbleBoard board, long commentId);

    /**
     * Returns array of comment ids for specified board ordered by creation date.
     *
     * @param board the board who's comments should be returned.
     * @return the array of comment ids for specified board ordered by creation date.
     */
    long[] getBoardComments(ScribbleBoard board);

    /**
     * Returns a comment by specified player for specified board
     *
     * @param commentId the comment id
     * @return the comment
     */
    ScribbleComment getComment(long commentId);

    /**
     * Removes all comments for specified board.
     *
     * @param board the board that comments should be removed.
     */
    void clearComments(ScribbleBoard board);
}
