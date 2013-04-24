package wisematches.client.android.app.account;

import android.os.Bundle;
import wisematches.client.android.R;
import wisematches.client.android.app.WiseMatchesActivity;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class RegisterActivity extends WiseMatchesActivity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_register);

/*
		final Bundle extras = getIntent().getExtras();
		if (extras != null) {
			final String username = extras.getString("username");
		}
*/
		getWiseMatchesClient().open("/account/create", this);
/*
		final WebView viewById = (WebView) findViewById(R.id.registerView);
		WebSettings settings = viewById.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		viewById.loadUrl("http://" + application.getServerHost() + "/account/create");
*/
	}
}