package wisematches.client.android.app;

import android.app.Activity;
import wisematches.client.android.WiseMatchesApplication;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WiseMatchesActivity extends Activity {
	public WiseMatchesActivity() {
	}

	public WiseMatchesApplication getWiseMatches() {
		return (WiseMatchesApplication) getApplication();
	}
}
