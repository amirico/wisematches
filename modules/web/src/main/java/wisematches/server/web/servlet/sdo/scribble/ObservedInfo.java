package wisematches.server.web.servlet.sdo.scribble;

import wisematches.server.web.servlet.sdo.scribble.board.ChangesInfo;
import wisematches.server.web.servlet.sdo.scribble.comment.CommentUpdatesInfo;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ObservedInfo {
	private ChangesInfo boardChanges;
	private CommentUpdatesInfo commentChanges;

	public ObservedInfo(ChangesInfo boardChanges, CommentUpdatesInfo commentChanges) {
		this.boardChanges = boardChanges;
		this.commentChanges = commentChanges;
	}

	public ChangesInfo getBoardChanges() {
		return boardChanges;
	}

	public CommentUpdatesInfo getCommentChanges() {
		return commentChanges;
	}
}
