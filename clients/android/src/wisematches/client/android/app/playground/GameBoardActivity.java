package wisematches.client.android.app.playground;

import android.os.Bundle;
import android.view.Display;
import android.view.View;
import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.CommunicationException;
import wisematches.client.android.CooperationException;
import wisematches.client.android.R;
import wisematches.client.android.app.WiseMatchesActivity;
import wisematches.client.android.app.playground.model.ScoreBonus;
import wisematches.client.android.app.playground.model.ScoreEngine;
import wisematches.client.android.app.playground.model.ScribbleBord;
import wisematches.client.android.http.ServerResponse;
import wisematches.client.android.os.ProgressTask;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class GameBoardActivity extends WiseMatchesActivity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playground_board);

		final Bundle extras = getIntent().getExtras();
		final long boardId = 100;// extras.getLong("boardId");

		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();

		ScribbleBord scribbleBord = new ScribbleBord(123, "asdasfdasdf", new ScoreEngine(new ScoreBonus[]{
				new ScoreBonus(0, 3, ScoreBonus.Type.W3),
				new ScoreBonus(0, 6, ScoreBonus.Type.L3),
				new ScoreBonus(1, 2, ScoreBonus.Type.L2),
				new ScoreBonus(1, 5, ScoreBonus.Type.W2),
				new ScoreBonus(2, 1, ScoreBonus.Type.L2),
				new ScoreBonus(2, 4, ScoreBonus.Type.L2),
				new ScoreBonus(3, 0, ScoreBonus.Type.W3),
				new ScoreBonus(3, 3, ScoreBonus.Type.L3),
				new ScoreBonus(3, 7, ScoreBonus.Type.W2),
				new ScoreBonus(4, 2, ScoreBonus.Type.L2),
				new ScoreBonus(4, 6, ScoreBonus.Type.L2),
				new ScoreBonus(5, 1, ScoreBonus.Type.W2),
				new ScoreBonus(5, 5, ScoreBonus.Type.L3),
				new ScoreBonus(6, 0, ScoreBonus.Type.L3),
				new ScoreBonus(6, 4, ScoreBonus.Type.L2),
				new ScoreBonus(7, 3, ScoreBonus.Type.W2)
		}, 30));

		final BoardView viewById = (BoardView) findViewById(R.id.scribbleBoardView);
		viewById.setScribbleBoard(scribbleBord);
		viewById.requestFocus();

		if (true) {
			return;
		}
		ProgressTask<Long, Void, ScribbleBord> a = new ProgressTask<Long, Void, ScribbleBord>("Loading board #" + boardId, this) {
			@Override
			protected ScribbleBord doInBackground(Long... voids) {
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
						return new ScribbleBord(data.getLong("boardId"), "asd", null);
					}
				} catch (JSONException | CooperationException | CommunicationException ex) {
					ex.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(ScribbleBord board) {
				super.onPostExecute(board);
				showBoardInfo(board);
			}
		};
		a.execute(boardId);
	}


	private void showBoardInfo(ScribbleBord board) {

	}
}