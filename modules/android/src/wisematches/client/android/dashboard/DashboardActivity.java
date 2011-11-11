package wisematches.client.android.dashboard;

import android.app.ListActivity;
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

		// Get the item that was clicked
		Object o = this.getListAdapter().getItem(position);
		String keyword = o.toString();
		Toast.makeText(this, "You selected: " + keyword, Toast.LENGTH_LONG).show();
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

					final JSONArray p = b.getJSONArray("playersInfo");
					final PlayerItem[] players = new PlayerItem[p.length()];
					for (int j = 0; j < players.length; j++) {
						JSONObject po = p.getJSONObject(j);
						final long id = po.getLong("id");
						final String nickname = po.getString("nickname");
						final int points = po.getInt("points");
						players[j] = new PlayerItem(id, nickname, Membership.BASIC, true, points);
					}
					boardItemsAdapter.add(new BoardItem(b.getLong("id"), b.getString("title"), players));
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