package wisematches.client.android.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import wisematches.client.android.AuthenticationException;
import wisematches.client.android.R;
import wisematches.client.android.WMActivity;
import wisematches.client.android.WMApplication;
import wisematches.client.android.activity.account.SignInActivity;
import wisematches.client.android.activity.playground.DashboardActivity;

public class EntryPointActivity extends WMActivity {
	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		final WMApplication application = (WMApplication) getApplication();

		final AuthenticationException authenticationException = application.openSession();
		if (authenticationException != null) {
			final Intent intent = new Intent(this, SignInActivity.class);
			intent.putExtra("authenticationException", authenticationException);
			startActivity(intent);
		} else {
			startActivity(new Intent(this, DashboardActivity.class));
		}
	}
}
