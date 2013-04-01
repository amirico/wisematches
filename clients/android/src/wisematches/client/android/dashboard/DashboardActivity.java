package wisematches.client.android.dashboard;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.Membership;
import wisematches.client.android.R;
import wisematches.client.android.playboard.GameBoardActivity;
import wisematches.client.android.server.ServerRequest;
import wisematches.client.android.server.ServerResponse;
import wisematches.client.android.server.ServerResponseListener;

import java.util.ArrayList;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DashboardActivity extends ListActivity implements ServerResponseListener {
	private ArrayAdapter<BoardItem> boardItemsAdapter;

	public DashboardActivity() {
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard_active);

		getActionBar().setDisplayShowHomeEnabled(false);

		boardItemsAdapter = new BoardItemsAdapter(this, new ArrayList<BoardItem>());
		setListAdapter(boardItemsAdapter);

		refreshItems();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		boardItemsAdapter = null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.dashboard_active, menu);
		return true;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		final BoardItem item = (BoardItem) this.getListAdapter().getItem(position);

		final Intent intent = new Intent(getApplicationContext(), GameBoardActivity.class);
		intent.putExtra("BOARD_ID", item.getId());
		startActivity(intent);
	}

	private void refreshItems() {
		ServerRequest.execute(this, "/playground/scribble/active.ajax", "Loading active games...", this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				return true;
			case R.id.menu_game_create:
				Toast.makeText(this, "Not implemented", Toast.LENGTH_LONG);
				return true;
			case R.id.menu_game_join:
				Toast.makeText(this, "Not implemented", Toast.LENGTH_LONG);
				return true;
			case R.id.menu_refresh:
				refreshItems();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onServerResponseSuccess(ServerResponse response) {
		if (boardItemsAdapter == null) {
			return;
		}
		try {
			if (response.isSuccess()) {
				boardItemsAdapter.clear();
				JSONArray jsonArray = response.getData().getJSONArray("boards");
				int length = jsonArray.length();
				for (int i = 0; i < length; i++) {
					final JSONObject b = jsonArray.getJSONObject(i);
					final JSONObject settings = b.getJSONObject("settings");
					final JSONArray p = b.getJSONArray("playersInfo");
					final PlayerItem[] players = new PlayerItem[p.length()];
					for (int j = 0; j < players.length; j++) {
						JSONObject po = p.getJSONObject(j);
						final long id = po.getLong("id");
						final String nickname = po.getString("nickname");
						final Membership membership = Membership.valueOf(po.getString("membership"));
						final boolean online = po.getBoolean("online");
						final int points = po.getInt("points");
						players[j] = new PlayerItem(id, nickname, membership, online, points);
					}

					final long playerTurn = b.getLong("playerTurn");
					final String elapsedTime = b.getString("elapsedTime");

					boardItemsAdapter.add(new BoardItem(
							b.getLong("id"),
							settings.getString("language"),
							settings.getString("title"),
							settings.getInt("daysPerMove"),
							elapsedTime,
							playerTurn,
							players));
				}
			}
		} catch (JSONException ex) {
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onServerResponseFailure(Exception exception) {
		if (boardItemsAdapter == null) {
			return;
		}
		Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
	}

	@Override
	public void onServerResponseCancel() {
	}
}