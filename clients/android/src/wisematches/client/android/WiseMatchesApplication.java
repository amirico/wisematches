package wisematches.client.android;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.core.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.TimeZone;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WiseMatchesApplication extends Application {
	private Player principal;

	private final HttpClient client;
	private final CookieStore cookieStore = new BasicCookieStore();
	private final HttpContext localContext = new BasicHttpContext();

	private static final int DEFAULT_TIMEOUT = 3000;
	private static final HttpHost HOST = new HttpHost("10.139.202.145", 8080);
//	private static final HttpHost HOST = new HttpHost("www.wisematches.net");

	public WiseMatchesApplication() {
		final HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.USER_AGENT, "Wisematches/1.0");
		HttpClientParams.setRedirecting(params, false);
		HttpConnectionParams.setSoTimeout(params, DEFAULT_TIMEOUT);
		HttpConnectionParams.setConnectionTimeout(params, DEFAULT_TIMEOUT);

		client = new DefaultHttpClient(params);
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
	}

	public Player getPrincipal() {
		return principal;
	}

	public String getServerHost() {
		return HOST.getHostName() + (HOST.getPort() == -1 ? "" : ":" + HOST.getPort());
	}

	public Player authenticate() throws CommunicationException, CooperationException {
		final SharedPreferences preferences = getSharedPreferences("principal", Context.MODE_PRIVATE);
		final String credentials = preferences.getString("credentials", null);
		if (credentials != null) {
			this.principal = authImpl(credentials);
			return principal;
		}
		return null;
	}

	public Player authenticate(String username, String password) throws CommunicationException, CooperationException {
		final String credentials = Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP);
		this.principal = authImpl(credentials);
		return this.principal;
	}


	public JSONObject post(String url, Header... headers) throws CommunicationException, CooperationException {
		return post(url, null, null, headers);
	}

	public JSONObject post(String url, JSONObject data, Header... headers) throws CommunicationException, CooperationException {
		return post(url, null, data, headers);
	}

	public JSONObject post(String url, HttpParams params, Header... headers) throws CommunicationException, CooperationException {
		return post(url, params, null, headers);
	}

	public JSONObject post(String url, HttpParams params, JSONObject data, Header... headers) throws CommunicationException, CooperationException {
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
			return builder.length() > 0 ? new JSONObject(builder.toString()) : null;
		} catch (JSONException ex) {
			throw new CooperationException(ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new CommunicationException(503, ex.getMessage());
		}
	}


	private Player authImpl(String credentials) throws CommunicationException, CooperationException {
		final BasicHeader basicHeader = new BasicHeader("Authorization", "Basic " + credentials);
		final JSONObject person = post("/account/login.ajax", basicHeader);

		try {
			final JSONObject data = person.getJSONObject("data");

			Player player = new Player(
					data.getLong("id"),
					data.getString("nickname"),
					data.getString("language"),
					TimeZone.getTimeZone(data.getString("timeZone")),
					data.getString("type"),
					data.optString("membership", null),
					true);

			final SharedPreferences preferences = getSharedPreferences("principal", Context.MODE_PRIVATE);
			final SharedPreferences.Editor edit = preferences.edit();
			edit.putLong("id", player.getId());
			edit.putString("credentials", credentials);
			edit.commit();
			return player;
		} catch (JSONException ex) {
			throw new CooperationException(ex.getMessage(), ex);
		}
	}
}