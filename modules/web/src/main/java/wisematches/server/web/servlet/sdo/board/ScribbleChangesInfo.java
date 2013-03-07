package wisematches.server.web.servlet.sdo.board;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleChangesInfo {
	private ScribbleCommentInfo[] comments;
	private ScribbleDescriptionInfo board;

	public ScribbleChangesInfo(ScribbleDescriptionInfo board, ScribbleCommentInfo[] comments) {
		this.board = board;
		this.comments = comments;
	}

	public ScribbleDescriptionInfo getBoard() {
		return board;
	}

	public ScribbleCommentInfo[] getComments() {
		return comments;
	}
}
