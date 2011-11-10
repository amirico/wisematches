package wisematches.server.web.controllers.playground.scribble.form;

import wisematches.playground.scribble.ScribbleBoard;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleInfoForm {
	private long id;
	private String title;

	public ScribbleInfoForm(ScribbleBoard board) {
		this(board.getBoardId(), board.getGameSettings().getTitle());
	}

	public ScribbleInfoForm(long id, String title) {
		this.id = id;
		this.title = title;
	}

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}
}
