package wisematches.client.android.app.playground;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import wisematches.client.android.R;
import wisematches.client.android.app.account.view.PlayerView;
import wisematches.client.android.app.playground.scribble.model.ScribblePlayer;

import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
class ScribblePlayerAdapter extends ArrayAdapter<ScribblePlayer> {
	private final Activity context;
	private final List<ScribblePlayer> players;

	public ScribblePlayerAdapter(Activity context, List<ScribblePlayer> players) {
		super(context, R.layout.playground_board_player, players);
		this.context = context;
		this.players = players;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			view = context.getLayoutInflater().inflate(R.layout.playground_board_player, null);

			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.points = (TextView) view.findViewById(R.id.scribbleBoardPlayerPoints);
			viewHolder.playerView = (PlayerView) view.findViewById(R.id.scribbleBoardPlayerView);
			view.setTag(viewHolder);
		} else {
			view = convertView;
		}

		final ViewHolder holder = (ViewHolder) view.getTag();
		final ScribblePlayer scribblePlayer = players.get(position);
		holder.points.setText(String.valueOf(scribblePlayer.getPoints()));
		holder.playerView.setPlayer(scribblePlayer);

		if (position == 0) {
			holder.playerView.setSelected(true);
		}
		return view;
	}

	private static class ViewHolder {
		private TextView points;
		private PlayerView playerView;
	}
}
