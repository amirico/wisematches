package wisematches.client.android.app.account;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import wisematches.client.android.R;
import wisematches.client.android.WiseMatchesApplication;
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
		final WiseMatchesApplication application = getWiseMatches();

		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + application.getServerHost() + "/account/create"));
		startActivity(Intent.createChooser(intent, "Chose browser"));
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