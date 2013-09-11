package wisematches.server.web.servlet.sdo.scribble.comment;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class CommentUpdatesInfo {
	private CommentInfo[] comments;

	public CommentUpdatesInfo(CommentInfo[] comments) {
		this.comments = comments;
	}

	public CommentInfo[] getComments() {
		return comments;
	}
}
