package wisematches.client.android.app;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import wisematches.client.android.R;
import wisematches.client.android.core.Player;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PlayerView extends LinearLayout {
	public PlayerView(Context context) {
		super(context);
	}

	public PlayerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.player, this, true);
	}

	public void setPlayer(Player player) {
		((TextView) findViewById(R.id.playerNickname)).setText(player.getNickname());

		final ImageView state = (ImageView) findViewById(R.id.playerState);
		state.setVisibility(player.isOnline() ? View.VISIBLE : View.GONE);
		invalidate();
	}
}
