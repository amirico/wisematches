package wisematches.client.android.app.account;

import android.os.Bundle;
import android.webkit.WebView;
import wisematches.client.android.R;
import wisematches.client.android.WiseMatchesApplication;
import wisematches.client.android.app.WiseMatchesActivity;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class RestoreActivity extends WiseMatchesActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_register);

		final WiseMatchesApplication application = getWiseMatches();

		final WebView viewById = (WebView) findViewById(R.id.registerView);
		viewById.loadUrl("http://" + application.getServerHost() + "/account/recovery/request");
	}
}
