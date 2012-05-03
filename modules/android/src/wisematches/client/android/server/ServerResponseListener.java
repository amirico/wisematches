package wisematches.client.android.server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface ServerResponseListener {
	void onServerResponseSuccess(ServerResponse response);

	void onServerResponseFailure(Exception exception);

	void onServerResponseCancel();
}
