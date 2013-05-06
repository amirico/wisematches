package wisematches.client.android.app.playground;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import wisematches.client.android.R;
import wisematches.client.android.app.account.model.Player;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ActiveGamesAdapter extends ArrayAdapter<ScribbleGameInfo> {
	private final Activity context;
	private final ScribbleGameInfo[] boardItems;

	public ActiveGamesAdapter(Activity context, ScribbleGameInfo[] boardItems) {
		super(context, R.layout.playground_dashboard_item, boardItems);
		this.context = context;
		this.boardItems = boardItems;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			view = context.getLayoutInflater().inflate(R.layout.playground_dashboard_item, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.text = (TextView) view.findViewById(R.id.boardTitle);
			viewHolder.number = (TextView) view.findViewById(R.id.boardNumber);
			viewHolder.elapsedTime = (TextView) view.findViewById(R.id.boardElapsedTime);
			viewHolder.players = (TableLayout) view.findViewById(R.id.dashboardPlayers);
			view.setTag(viewHolder);
		} else {
			view = convertView;
		}

		final ViewHolder holder = (ViewHolder) view.getTag();

		final ScribbleGameInfo boardItem = boardItems[position];
		holder.text.setText(boardItem.getTitle());
		holder.number.setText(String.valueOf(boardItem.getBoardId()));
//		holder.elapsedTime.setText(String.valueOf(boardItem.getElapsedTime()));
		holder.players.removeAllViews();

		final Player[] players = null;// boardItem.getPlayers();
		if (players != null) {
			for (Player playerItem : players) {
				final TableRow row = new TableRow(context);
				if (playerItem.getId() == boardItem.getPlayerTurn()) {
					row.setBackgroundResource(R.drawable.ui_state_online);
				}

				final View player = context.getLayoutInflater().inflate(R.layout.player, null);
				((TextView) player.findViewById(R.id.playerNickname)).setText(playerItem.getNickname());
				row.addView(player);

				final ImageView state = (ImageView) player.findViewById(R.id.playerState);
				state.setVisibility(playerItem.isOnline() ? View.VISIBLE : View.GONE);

				final TextView p = new TextView(context);
				p.setText(String.valueOf(100)); //playerItem.getPoints()
				p.setPadding(5, 0, 0, 0);
				row.addView(p);

				holder.players.addView(row);
			}
		}
		return view;
	}

	private static class ViewHolder {
		private TextView text;
		private TextView number;
		private TextView elapsedTime;
		private TableLayout players;
	}
}
