package wisematches.client.android.dashboard;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import wisematches.client.android.R;

import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class BoardItemsAdapter extends ArrayAdapter<BoardItem> {
	private final Activity context;
	private final List<BoardItem> boardItems;

	public BoardItemsAdapter(Activity context, List<BoardItem> boardItems) {
		super(context, R.layout.dashboard_board_item, boardItems);
		this.context = context;
		this.boardItems = boardItems;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			view = context.getLayoutInflater().inflate(R.layout.dashboard_board_item, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.text = (TextView) view.findViewById(R.id.label);
			viewHolder.number = (TextView) view.findViewById(R.id.number);
			viewHolder.players = (LinearLayout) view.findViewById(R.id.dashboardPlayers);
			view.setTag(viewHolder);
		} else {
			view = convertView;
		}

		final ViewHolder holder = (ViewHolder) view.getTag();

		final BoardItem boardItem = boardItems.get(position);
		holder.text.setText(boardItem.getName());
		holder.number.setText(String.valueOf(boardItem.getId()));

		holder.players.removeAllViews();

		for (int i = 0; i < 2; i++) {
			TextView child = new TextView(context);
			child.setText("Player " + i);
			holder.players.addView(child);
		}
		return view;
	}

	private static class ViewHolder {
		private TextView text;
		private TextView number;
		private LinearLayout players;
	}
}
