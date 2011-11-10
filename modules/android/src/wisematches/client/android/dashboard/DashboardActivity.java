package wisematches.client.android.dashboard;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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

		boardItemsAdapter = new BoardItemsAdapter(this, new ArrayList<BoardItem>());
		setListAdapter(boardItemsAdapter);

		ServerRequest.execute(this, "/playground/scribble/active.ajax", "Loading active games...", this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		boardItemsAdapter = null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.dashboard, menu);
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

	@Override
	public void onSuccess(ServerResponse response) {
		if (boardItemsAdapter == null) {
			return;
		}
		try {
			if (response.isSuccess()) {
				JSONArray jsonArray = response.getData().getJSONArray("boards");
				int length = jsonArray.length();
				for (int i = 0; i < length; i++) {
					JSONObject b = jsonArray.getJSONObject(i);
					boardItemsAdapter.add(new BoardItem(b.getLong("id"), b.getString("title"), new PlayerItem[0]));
				}
			}
		} catch (JSONException ex) {
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onFailure(Exception exception) {
		if (boardItemsAdapter == null) {
			return;
		}
		Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
	}

	@Override
	public void onCancel() {
	}
}