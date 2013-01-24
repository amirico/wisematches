package wisematches.client.android.server;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ServerResponse {
	private final boolean success;
	private final String summary;
	private final JSONObject data;

	ServerResponse(JSONObject object) throws JSONException {
		this(object.getBoolean("success"), object.getString("summary"), object.getJSONObject("data"));
	}

	public ServerResponse(boolean success, String summary, JSONObject data) {
		this.success = success;
		this.summary = summary;
		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getSummary() {
		return summary;
	}

	public JSONObject getData() {
		return data;
	}
}
