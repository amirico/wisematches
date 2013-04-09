package wisematches.client.android.app;

import android.app.Activity;
import wisematches.client.android.WiseMatchesApplication;
import wisematches.client.android.http.WiseMatchesServer;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WiseMatchesActivity extends Activity {
	public WiseMatchesActivity() {
	}

	public WiseMatchesServer getWMServer() {
		return getWMApplication().getWMServer();
	}

	public WiseMatchesApplication getWMApplication() {
		return (WiseMatchesApplication) getApplication();
	}
}
