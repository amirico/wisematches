package wisematches.client.android;

import android.app.Application;
import android.app.ProgressDialog;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.TimeZone;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WMApplication extends Application {
	private Player principal;

	private final HttpClient client;
	private final CookieStore cookieStore = new BasicCookieStore();
	private final HttpContext localContext = new BasicHttpContext();

	private ProgressDialog progressDialog = null;

	private static final int DEFAULT_TIMEOUT = 3000;
	private static final HttpHost HOST = new HttpHost("10.139.202.145", 8080);
//	private static final HttpHost HOST = new HttpHost("www.wisematches.net");

	public WMApplication() {
		final HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.USER_AGENT, "Wisematches/1.0");
		HttpClientParams.setRedirecting(params, false);
		HttpConnectionParams.setSoTimeout(params, DEFAULT_TIMEOUT);
		HttpConnectionParams.setConnectionTimeout(params, DEFAULT_TIMEOUT);

		client = new DefaultHttpClient(params);
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}


	public Player getPrincipal() {
		return principal;
	}

	/**
	 * Authenticate based on stored properties
	 */
	public AuthenticationException openSession() {
		final SharedPreferences preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);
		if (preferences.getBoolean("stored", false)) {
			final String username = preferences.getString("username", null);
			final String password = preferences.getString("password", null);
			return openSession(username, password);
		}
		return new AuthenticationException("not.stored");
	}

	/**
	 * Authenticate based on stored properties
	 */
	public AuthenticationException openSession(String username, String password) {
		try {
			final BasicHeader basicHeader = new BasicHeader("Authorization", "Basic " + Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP));
			final JSONObject person = post("/account/login.ajax", basicHeader);

			final JSONObject data = person.getJSONObject("data");

			this.principal = new Player(
					data.getLong("id"),
					data.getString("nickname"),
					data.getString("language"),
					TimeZone.getTimeZone(data.getString("timeZone")),
					data.getString("type"),
					data.optString("membership", null),
					true);

			final SharedPreferences preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);
			final SharedPreferences.Editor edit = preferences.edit();
			edit.putString("username", username);
			edit.putString("password", password);
			edit.putBoolean("stored", Boolean.TRUE);
			edit.commit();

			System.out.println(person);

			// MUST be at the end: principal = new
/*
			if (execute == null) {
				return new Exception("R.string.account_login_error_system");
			}

			final Header header = execute.getLastHeader("Location");
			if (header == null) {
				return new Exception("R.string.account_login_error_response");
			} else {
				final String value = header.getValue();
				if (value.contains("account/loginAuth")) { // error
					String errorCode = null;
					int i = value.indexOf("error=");
					if (i >= 0) {
						int i1 = value.indexOf("&", i);
						if (i1 < 0) {
							errorCode = value.substring(i + 6);
						} else {
							errorCode = value.substring(i + 6, i1);
						}
					}

					Log.i("WM", "Error code: " + errorCode);
					if (errorCode == null) {
						return new Exception("R.string.account_login_error_system");
					} else {
						return new Exception("R.string.account_login_error_credential");
					}
				} else {
					final Header principalId = execute.getLastHeader("PrincipalId");
					if (principalId != null) {
						final long id = Long.parseLong(principalId.getValue());
						final String type = execute.getLastHeader("PrincipalType").getValue();
						final String language = execute.getLastHeader("PrincipalLanguage").getValue();
						final String nickname = execute.getLastHeader("PrincipalNickname").getValue();
						final String membership = execute.getLastHeader("PrincipalMembership").getValue();
						final String timeZone = execute.getLastHeader("PrincipalTimeZone").getValue();


						WMPrincipal p = new WMPrincipal(id, nickname, language, timeZone, type, membership);
						wisematches.auth(p);
					}
					return null;
				}
			}
*/
		} catch (IOException ex) {
			return new AuthenticationException(ex.getMessage());
		} catch (JSONException ex) {
			return new AuthenticationException(ex.getMessage());
		}
		return null;
	}

	public JSONObject post(String url, Header... headers) throws IOException, JSONException {
		return post(url, null, null, headers);
	}

	public JSONObject post(String url, JSONObject data, Header... headers) throws IOException, JSONException {
		return post(url, null, data, headers);
	}

	public JSONObject post(String url, HttpParams params, Header... headers) throws IOException, JSONException {
		return post(url, params, null, headers);
	}

	public JSONObject post(String url, HttpParams params, JSONObject data, Header... headers) throws IOException, JSONException {
		try {
/*
			progressDialog = ProgressDialog.show(getBaseContext(), null, "Logging in to WiseMatches. Please wait...", true, true, new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialogInterface) {
					dialogInterface.dismiss();
				}
			});
*/

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
				throw new IOException("Incorrect status code: " + status.getReasonPhrase());
			}

			final HttpEntity entity = response.getEntity();
			final InputStream content = entity.getContent();

			final BufferedReader reader = new BufferedReader(new InputStreamReader(content));
			String line = null;
			final StringBuilder builder = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			return builder.length() > 0 ? new JSONObject(builder.toString()) : null;
		} catch (Exception ex) {
			System.out.println(ex);
			throw ex;
		} finally {
/*
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
*/
		}
	}
}