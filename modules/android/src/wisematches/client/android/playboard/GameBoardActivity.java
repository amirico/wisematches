package wisematches.client.android.playboard;

import android.os.Bundle;
import android.util.Log;
import wisematches.client.android.R;
import wisematches.client.android.WMActivity;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */

public class GameBoardActivity extends WMActivity {
	public GameBoardActivity() {
	}

	public void onCreate(Bundle savedInstanceState) {
		final long boardId = getIntent().getLongExtra("BOARD_ID", 0);

		Log.i("WM", "Start board id: " + boardId);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.playboard_board);
		setTitle(getResources().getString(R.string.playboardLabel) + " #" + boardId);
	}
}