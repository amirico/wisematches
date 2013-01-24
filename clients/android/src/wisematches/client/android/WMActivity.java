package wisematches.client.android;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import wisematches.client.android.dashboard.DashboardActivity;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WMActivity extends Activity {
	public WMActivity() {
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
}
