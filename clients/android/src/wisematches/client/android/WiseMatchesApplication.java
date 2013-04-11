package wisematches.client.android;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.http.ServerResponse;
import wisematches.client.android.http.WiseMatchesServer;
import wisematches.client.android.view.PlayerInfo;

import java.util.TimeZone;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WiseMatchesApplication extends Application {
	private PlayerInfo principal;
	private WiseMatchesServer wiseMatchesServer;

	public WiseMatchesApplication() {
	}

	public PlayerInfo getPrincipal() {
		return principal;
	}

	public WiseMatchesServer getWMServer() {
		return wiseMatchesServer;
	}


	public PlayerInfo authenticate() throws CommunicationException, CooperationException {
		final SharedPreferences preferences = getSharedPreferences("principal", Context.MODE_PRIVATE);
		final String credentials = preferences.getString("credentials", null);
		if (credentials != null) {
			this.principal = authImpl(credentials);
			return principal;
		}
		return null;
	}

	public PlayerInfo authenticate(String username, String password) throws CommunicationException, CooperationException {
		final String credentials = Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP);
		this.principal = authImpl(credentials);
		return this.principal;
	}


	@Override
	public void onCreate() {
		super.onCreate();
		wiseMatchesServer = new WiseMatchesServer();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		principal = null;
		wiseMatchesServer.terminate();
	}


	private PlayerInfo authImpl(String credentials) throws CommunicationException, CooperationException {
		final BasicHeader basicHeader = new BasicHeader("Authorization", "Basic " + credentials);
		final ServerResponse r = wiseMatchesServer.post("/account/login.ajax", basicHeader);

		try {
			final JSONObject data = r.getData();
			PlayerInfo player = new PlayerInfo(
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