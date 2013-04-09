package wisematches.client.android.view.playground;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleInfo {
	private final long boardId;
	private final String title;

	public ScribbleInfo(long boardId, String title) {
		this.boardId = boardId;
		this.title = title;
	}

	public long getBoardId() {
		return boardId;
	}

	public String getTitle() {
		return title;
	}
}
