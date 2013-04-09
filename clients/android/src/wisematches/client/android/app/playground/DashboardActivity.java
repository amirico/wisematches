package wisematches.client.android.app.playground;

import android.os.Bundle;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.CommunicationException;
import wisematches.client.android.CooperationException;
import wisematches.client.android.R;
import wisematches.client.android.app.WiseMatchesActivity;
import wisematches.client.android.http.ServerResponse;
import wisematches.client.android.os.ProgressTask;
import wisematches.client.android.view.playground.ScribbleInfo;
import wisematches.client.android.view.playground.ScribbleInfoAdapter;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DashboardActivity extends WiseMatchesActivity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playground_dashboard);

		final ListView listView = (ListView) findViewById(R.id.scribbleViewActive);

		ProgressTask<Void, Void, ScribbleInfo[]> a = new ProgressTask<Void, Void, ScribbleInfo[]>("Loading active games. Please wait...", this) {
			@Override
			protected ScribbleInfo[] doInBackground(Void... voids) {
				try {
					final ServerResponse r = getWMServer().post("/playground/scribble/active.ajax");
					if (r.isSuccess()) {
						final JSONObject data = r.getData();


						final JSONArray games = data.getJSONArray("games");
						ScribbleInfo[] infos = new ScribbleInfo[games.length()];
						for (int i = 0; i < games.length(); i++) {
							final JSONObject obj = games.getJSONObject(i);
							infos[i] = new ScribbleInfo(obj.getLong("boardId"), obj.getString("title"));
						}
						return infos;
					}
				} catch (JSONException ex) {
					ex.printStackTrace();
				} catch (CooperationException ex) {
					ex.printStackTrace();
				} catch (CommunicationException ex) {
					ex.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(ScribbleInfo[] games) {
				super.onPostExecute(games);

				ScribbleInfoAdapter adapter = new ScribbleInfoAdapter(DashboardActivity.this, R.layout.playground_dashboard_item, games);
				listView.setAdapter(adapter);
			}
		};
		a.execute();
	}
}