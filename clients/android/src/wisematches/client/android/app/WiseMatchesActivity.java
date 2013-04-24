package wisematches.client.android.app;

import com.actionbarsherlock.app.SherlockActivity;
import wisematches.client.android.app.account.model.Player;
import wisematches.client.android.graphics.BitmapFactory;
import wisematches.client.android.http.WiseMatchesClient;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WiseMatchesActivity extends SherlockActivity {
	public WiseMatchesActivity() {
	}

	public Player getPrincipal() {
		return getWMApplication().getPrincipal();
	}

	public BitmapFactory getBitmapFactory() {
		return getWMApplication().getBitmapFactory();
	}

	public WiseMatchesClient getWiseMatchesClient() {
		return getWMApplication().getWiseMatchesClient();
	}

	public WiseMatchesApplication getWMApplication() {
		return (WiseMatchesApplication) getApplication();
	}
}
