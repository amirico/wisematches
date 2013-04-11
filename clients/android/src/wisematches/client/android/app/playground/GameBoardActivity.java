package wisematches.client.android.app.playground;

import android.os.Bundle;
import android.view.View;
import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.CommunicationException;
import wisematches.client.android.CooperationException;
import wisematches.client.android.R;
import wisematches.client.android.app.WiseMatchesActivity;
import wisematches.client.android.http.ServerResponse;
import wisematches.client.android.os.ProgressTask;
import wisematches.client.android.view.playground.ScribbleBordInfo;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class GameBoardActivity extends WiseMatchesActivity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playground_board);

		final Bundle extras = getIntent().getExtras();
		final long boardId = 100;// extras.getLong("boardId");

		final View viewById = findViewById(R.id.scribbleBoardView);
		viewById.requestFocus();

		if (true) {
			return;
		}
		ProgressTask<Long, Void, ScribbleBordInfo> a = new ProgressTask<Long, Void, ScribbleBordInfo>("Loading board #" + boardId, this) {
			@Override
			protected ScribbleBordInfo doInBackground(Long... voids) {
				try {
					final ServerResponse r = getWMServer().post("/playground/scribble/board/load.ajax?b=" + boardId);
					if (r.isSuccess()) {
						final JSONObject data = r.getData();
//
//						final JSONArray games = data.getJSONArray("games");
//						ScribbleGameInfo[] infos = new ScribbleGameInfo[games.length()];
//						for (int i = 0; i < games.length(); i++) {
//							final JSONObject obj = games.getJSONObject(i);
//							infos[i] = new ScribbleGameInfo(obj);
//						}
						return new ScribbleBordInfo(data.getLong("boardId"));
					}
				} catch (JSONException | CooperationException | CommunicationException ex) {
					ex.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(ScribbleBordInfo board) {
				super.onPostExecute(board);
				showBoardInfo(board);
			}
		};
		a.execute(boardId);
	}



	private void showBoardInfo(ScribbleBordInfo board) {

	}
}