package wisematches.client.android.app.playground;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.CommunicationException;
import wisematches.client.android.CooperationException;
import wisematches.client.android.R;
import wisematches.client.android.app.WiseMatchesActivity;
import wisematches.client.android.app.playground.scribble.ScribbleBoardActivity;
import wisematches.client.android.http.ServerResponse;
import wisematches.client.android.os.ProgressTask;
import wisematches.client.android.view.playground.ScribbleGameInfo;
import wisematches.client.android.view.playground.ScribbleGameInfoAdapter;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DashboardActivity extends WiseMatchesActivity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playground_dashboard);

		// TODO: for testing only
		if (true) {
			openBoard(2459);
			return;
		}

		final ListView listView = (ListView) findViewById(R.id.scribbleViewActive);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg) {
				final ScribbleGameInfo info = (ScribbleGameInfo) adapterView.getItemAtPosition(position);
				openBoard(info.getBoardId());
			}
		});

		ProgressTask<Void, Void, ScribbleGameInfo[]> a = new ProgressTask<Void, Void, ScribbleGameInfo[]>("Loading active games. Please wait...", this) {
			@Override
			protected ScribbleGameInfo[] doInBackground(Void... voids) {
				try {
					final ServerResponse r = getWMServer().post("/playground/scribble/active.ajax");
					if (r.isSuccess()) {
						final JSONObject data = r.getData();


						final JSONArray games = data.getJSONArray("games");
						ScribbleGameInfo[] infos = new ScribbleGameInfo[games.length()];
						for (int i = 0; i < games.length(); i++) {
							final JSONObject obj = games.getJSONObject(i);
							infos[i] = new ScribbleGameInfo(obj);
						}
						return infos;
					}
				} catch (JSONException | CooperationException | CommunicationException ex) {
					ex.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(ScribbleGameInfo[] games) {
				super.onPostExecute(games);

				ScribbleGameInfoAdapter adapter = new ScribbleGameInfoAdapter(DashboardActivity.this, games);
				listView.setAdapter(adapter);
			}
		};
		a.execute();
	}

	private void openBoard(final long boardId) {
		final Intent intent = new Intent(this, ScribbleBoardActivity.class);
		intent.putExtra("boardId", boardId);
		startActivity(intent);
	}
}