package wisematches.server.web.servlet.sdo.scribble;

import wisematches.server.web.servlet.sdo.scribble.board.BoardUpdatesInfo;
import wisematches.server.web.servlet.sdo.scribble.comment.CommentUpdatesInfo;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ObservedInfo {
	private BoardUpdatesInfo boardChanges;
	private CommentUpdatesInfo commentChanges;

	public ObservedInfo(BoardUpdatesInfo boardChanges, CommentUpdatesInfo commentChanges) {
		this.boardChanges = boardChanges;
		this.commentChanges = commentChanges;
	}

	public BoardUpdatesInfo getBoardChanges() {
		return boardChanges;
	}

	public CommentUpdatesInfo getCommentChanges() {
		return commentChanges;
	}
}
