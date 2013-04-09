package wisematches.client.android.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;
import wisematches.client.android.CommunicationException;
import wisematches.client.android.CooperationException;
import wisematches.client.android.app.account.LoginActivity;
import wisematches.client.core.Player;
import wisematches.client.android.R;
import wisematches.client.android.app.account.LoginActivity;
import wisematches.client.android.app.playground.DashboardActivity;

public class EntryPointActivity extends WiseMatchesActivity {
	private TextView status;
	private AsyncTask<Void, Void, Player> authTask;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		// TODO: local resources
		status = (TextView) findViewById(R.id.splashFldStatus);
		status.setText("Preparing resources...");

		doAuth();
	}

	private void doAuth() {
		authTask = new AsyncTask<Void, Void, Player>() {
			@Override
			protected Player doInBackground(Void... voids) {
				Player player = null;
				try {
					player = getWiseMatches().authenticate();
					if (player != null) {
						startActivity(new Intent(EntryPointActivity.this, DashboardActivity.class));
					} else {
						startActivity(new Intent(EntryPointActivity.this, LoginActivity.class));
					}
				} catch (CommunicationException ex) {
					authTask = null;
					if (ex.getStatusCode() == 401) {
						final Intent intent = new Intent(EntryPointActivity.this, LoginActivity.class);
						intent.putExtra("ErrorCode", ex.getStatusReason());
						startActivity(intent);
					} else {
						status.setText("Illegal return code received: " + ex.getStatusReason() + " [" + ex.getStatusCode() + "]. Tap to try again.");
					}
				} catch (CooperationException ex) {
					authTask = null;
					status.setText("Server communication error: " + ex.getMessage() + ". Tap to try again.");
				}
				return player;
			}

			@Override
			protected void onCancelled() {
				startActivity(new Intent(EntryPointActivity.this, LoginActivity.class));
			}
		};

		status.setText("Authenticating. Please wait...");
		authTask.execute();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (authTask != null) {
				authTask.cancel(true);
				authTask = null;
			} else {
				doAuth();
			}
		}
		return true;
	}
}
