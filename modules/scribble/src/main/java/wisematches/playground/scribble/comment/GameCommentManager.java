package wisematches.playground.scribble.comment;

import wisematches.personality.Personality;
import wisematches.playground.GameBoard;

import java.util.List;

/**
 * Scribble comments manager. Allows add/remove and receive comments.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GameCommentManager {
	/**
	 * Adds new comment to specified board by specified personality.
	 *
	 * @param board	   the board
	 * @param personality the personality who wants add a comment.
	 * @param text		the text
	 * @return created game comment
	 */
	GameComment addComment(GameBoard board, Personality personality, String text);

	/**
	 * Removes a comment that should be removed.
	 *
	 * @param board	   the board
	 * @param personality the personality who wants remove the comment.
	 * @param commentId   the comment id.
	 * @return removed game comment or null if no comments were removed.
	 */
	GameComment removeComment(GameBoard board, Personality personality, long commentId);

	/**
	 * Returns comments count for specified board and player.
	 *
	 * @param board	   the board
	 * @param personality the player
	 * @return comments count
	 */
	int getCommentsCount(GameBoard board, Personality personality);

	/**
	 * Returns collection of comments for specified ids. Not exist comments are not included
	 * in result collection.
	 *
	 * @param board	   the board
	 * @param personality the personality who wants load comments.
	 * @param ids		 the list of ids
	 * @return the collection of requested comments.
	 */
	List<GameComment> getComments(GameBoard board, Personality personality, long... ids);

	/**
	 * Returns collection of all comments for specified board what are available for specified personality.
	 *
	 * @param board	   the board
	 * @param personality the personality who wants receive comments.
	 * @return collection of comment states or empty collection if there are no comments.
	 */
	List<GameCommentState> getCommentStates(GameBoard board, Personality personality);


	/**
	 * Marks all comments as read for specified played and board. If all comments should be marked as
	 * read when {@code ids} parameter should be {@code null}.
	 *
	 * @param board	   the board
	 * @param personality the personality who wants mark comments as read.
	 * @param ids		 the ids of comments which should be marked as read.
	 */
	void markRead(GameBoard board, Personality personality, long... ids);

	/**
	 * Marks all comments as unread for specified played and board. If all comments should be marked as
	 * unread when {@code ids} parameter should be {@code null}.
	 *
	 * @param board	   the board
	 * @param personality the personality who wants mark comments as unread.
	 * @param ids		 the ids of comments which should be marked as unread.
	 */
	void markUnread(GameBoard board, Personality personality, long... ids);


	/**
	 * Removes all comments for specified board.
	 *
	 * @param board the board that comments should be removed.
	 */
	void clearComments(GameBoard board);
}