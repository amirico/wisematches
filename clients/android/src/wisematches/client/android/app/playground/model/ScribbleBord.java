package wisematches.client.android.app.playground.model;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleBord {
	private final long boardId;
	private final String title;
	private final ScoreEngine scoreEngine;

	public ScribbleBord(long boardId, String title, ScoreEngine scoreEngine) {
		this.boardId = boardId;
		this.title = title;
		this.scoreEngine = scoreEngine;
	}

	public ScoreEngine getScoreEngine() {
		return scoreEngine;
	}
}
