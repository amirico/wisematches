package wisematches.client.android.http;

import org.json.JSONObject;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ClientResponse {
	private final boolean success;
	private final JSONObject data;

	public ClientResponse(boolean success, JSONObject data) {
		this.data = data;
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}

	public JSONObject getData() {
		return data;
	}
}
