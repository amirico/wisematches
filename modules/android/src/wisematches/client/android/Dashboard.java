package wisematches.client.android;

import android.app.Activity;
import android.os.Bundle;

public class Dashboard extends Activity {
	public Dashboard() {
	}

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
	}
}