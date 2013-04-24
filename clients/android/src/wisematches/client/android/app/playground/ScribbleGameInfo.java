package wisematches.client.android.app.playground;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Deprecated
public class ScribbleGameInfo {
	private final long boardId;
	private final String title;
	private char[] elapsedTime;
	private long playerTurn;

	public ScribbleGameInfo(JSONObject obj) throws JSONException {
		this.boardId = obj.getLong("boardId");
		this.title = obj.getString("title");
	}

	public long getBoardId() {
		return boardId;
	}

	public String getTitle() {
		return title;
	}

	public char[] getElapsedTime() {
		return elapsedTime;
	}

	public long getPlayerTurn() {
		return playerTurn;
	}
}
