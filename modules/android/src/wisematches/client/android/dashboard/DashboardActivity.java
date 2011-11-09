package wisematches.client.android.dashboard;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import wisematches.client.android.R;
import wisematches.client.android.WisematchesAndroid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DashboardActivity extends ListActivity {
	private List<BoardItem> boardItemList;

	public DashboardActivity() {
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard_active);

		final ArrayAdapter<BoardItem> adapter = new BoardItemsAdapter(this, new ArrayList<BoardItem>());
		setListAdapter(adapter);

		final HttpGet get = new HttpGet("/playground/scribble/active.ajax");
		try {
			HttpResponse execute = ((WisematchesAndroid) getApplication()).execute(get);

		} catch (IOException e) {
			Toast.makeText(DashboardActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		// Get the item that was clicked
		Object o = this.getListAdapter().getItem(position);
		String keyword = o.toString();
		Toast.makeText(this, "You selected: " + keyword, Toast.LENGTH_LONG).show();
	}
}