package wisematches.client.android.http;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.CommunicationException;
import wisematches.client.android.CooperationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WiseMatchesServer {
	private final HttpClient client;
	private final CookieStore cookieStore = new BasicCookieStore();
	private final HttpContext localContext = new BasicHttpContext();

	private static final int DEFAULT_TIMEOUT = 3000;
	private static final HttpHost HOST = new HttpHost("10.139.202.145", 8080);
//	private static final HttpHost HOST = new HttpHost("www.wisematches.net");


	public WiseMatchesServer() {
		final HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.USER_AGENT, "Wisematches/1.0");
		HttpClientParams.setRedirecting(params, false);
		HttpConnectionParams.setSoTimeout(params, DEFAULT_TIMEOUT);
		HttpConnectionParams.setConnectionTimeout(params, DEFAULT_TIMEOUT);

		client = new DefaultHttpClient(params);
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
	}

	public String getServerHost() {
		return HOST.getHostName() + (HOST.getPort() == -1 ? "" : ":" + HOST.getPort());
	}

	public void open(String uri, Context context) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + getServerHost() + uri));
		context.startActivity(Intent.createChooser(intent, "Chose browser"));
	}

	public ServerResponse post(String url, Header... headers) throws CommunicationException, CooperationException {
		return post(url, null, null, headers);
	}

	public ServerResponse post(String url, JSONObject data, Header... headers) throws CommunicationException, CooperationException {
		return post(url, null, data, headers);
	}

	public ServerResponse post(String url, HttpParams params, Header... headers) throws CommunicationException, CooperationException {
		return post(url, params, null, headers);
	}

	public ServerResponse post(String url, HttpParams params, JSONObject data, Header... headers) throws CommunicationException, CooperationException {
		try {
			final HttpPost request = new HttpPost(url);

			if (params != null) {
				request.setParams(params);
			}

			if (data != null) {
				request.setEntity(new StringEntity(data.toString(), "UTF-8"));
			}

			if (headers != null) {
				request.setHeaders(headers);
			}

			HttpConnectionParams.setSoTimeout(request.getParams(), DEFAULT_TIMEOUT);
			HttpConnectionParams.setConnectionTimeout(request.getParams(), DEFAULT_TIMEOUT);

			final HttpResponse response = client.execute(HOST, request, localContext);
			final StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != 200) {
				throw new CommunicationException(status.getStatusCode(), status.getReasonPhrase());
			}

			final HttpEntity entity = response.getEntity();
			final InputStream content = entity.getContent();

			String line;
			final StringBuilder builder = new StringBuilder();
			final BufferedReader reader = new BufferedReader(new InputStreamReader(content));
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}

			if (builder.length() == 0) {
				return null;
			}

			final JSONObject r = new JSONObject(builder.toString());
			return new ServerResponse(r.getBoolean("success"), r.optJSONObject("data"));
		} catch (JSONException ex) {
			throw new CooperationException(ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new CommunicationException(503, ex.getMessage());
		}
	}

	public void terminate() {
		cookieStore.clear();
	}
}
