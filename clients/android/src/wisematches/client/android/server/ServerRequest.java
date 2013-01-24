package wisematches.client.android.server;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.WisematchesApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ServerRequest extends AsyncTask<String, Void, JSONObject> {
	private final Context context;
	private final ServerResponseListener listener;
	private final ProgressDialog progressDialog;

	private static final DefaultListener DEFAULT_LISTENER = new DefaultListener();

	private ServerRequest(Context context, String message, ServerResponseListener listener) {
		this.context = context;
		this.listener = listener;

		if (message != null) {
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage(message);
		} else {
			progressDialog = null;
		}
	}

	@Override
	protected JSONObject doInBackground(String... strings) {
		final HttpPost get = new HttpPost(strings[0]);
		try {
			final HttpResponse response = ((WisematchesApplication) context.getApplicationContext()).execute(get);
			final String content = readContent(response);
			if (!isCancelled()) {
				return new JSONObject(content);
			}
		} catch (Exception e) {
			listener.onServerResponseFailure(e);
		}
		return null;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (progressDialog != null) {
			progressDialog.show();
		}
	}

	@Override
	protected void onPostExecute(JSONObject jsonObject) {
		super.onPostExecute(jsonObject);
		if (listener != null) {
			try {
				listener.onServerResponseSuccess(new ServerResponse(jsonObject));
			} catch (JSONException e) {
				listener.onServerResponseFailure(e);
			}
		}
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		listener.onServerResponseCancel();
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	@Override
	protected void onCancelled(JSONObject jsonObject) {
		super.onCancelled(jsonObject);
		listener.onServerResponseCancel();
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	public static ServerRequest execute(Context context, String uri, String message) {
		return execute(context, uri, message, DEFAULT_LISTENER);
	}

	public static ServerRequest execute(Context context, String uri, String message, ServerResponseListener listener) {
		final ServerRequest request = new ServerRequest(context, message, listener != null ? listener : DEFAULT_LISTENER);
		return (ServerRequest) request.execute(uri);
	}

	private String readContent(HttpResponse response) throws IOException {
		final StringBuilder builder = new StringBuilder();
		final HttpEntity entity = response.getEntity();
		final InputStream content = entity.getContent();
		final BufferedReader reader = new BufferedReader(new InputStreamReader(content));
		String line;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		return builder.toString();
	}

	private static class DefaultListener implements ServerResponseListener {
		private DefaultListener() {
		}

		@Override
		public void onServerResponseSuccess(ServerResponse response) {
		}

		@Override
		public void onServerResponseFailure(Exception exception) {
		}

		@Override
		public void onServerResponseCancel() {
		}
	}
}
