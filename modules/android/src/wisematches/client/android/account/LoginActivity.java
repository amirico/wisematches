package wisematches.client.android.account;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import wisematches.client.android.R;
import wisematches.client.android.WisematchesApplication;
import wisematches.client.android.dashboard.DashboardActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity {
	public LoginActivity() {
	}

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
		final String username = preferences.getString("username", null);
		final String password = preferences.getString("password", null);

		if (username != null && password != null) {
			setContentView(R.layout.account_logging);
			doSignIn(username, password, false);
		} else {
			setContentView(R.layout.accout_login);
			final Button signIn = (Button) findViewById(R.id.accountButtonCreate);
			signIn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					ProgressDialog.show(LoginActivity.this, null, "Logging in to WiseMatches. Please wait...", true);
					final EditText email = (EditText) findViewById(R.id.accountFieldEmail);
					final EditText pwd = (EditText) findViewById(R.id.accountFieldPassword);
					final CheckBox rememberMe = (CheckBox) findViewById(R.id.accountFieldRememberMe);

					final String username = email.getText().toString();
					final String password = pwd.getText().toString();

					doSignIn(username, password, rememberMe.isChecked());
				}
			});
		}
	}

	private void doSignIn(final String username, final String password, final boolean rememberMe) {
		new AsyncTask<String, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(String... args) {
				try {
					final WisematchesApplication wisematches = (WisematchesApplication) getApplication();

					final HttpPost req = new HttpPost("/account/loginProcessing");
					final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
					nameValuePairs.add(new BasicNameValuePair("j_username", username));
					nameValuePairs.add(new BasicNameValuePair("j_password", password));
					req.setEntity(new UrlEncodedFormEntity(nameValuePairs));

					final HttpResponse execute = wisematches.execute(req);
					final Header location = execute.getLastHeader("Location");
					if (location == null) {
						Toast.makeText(LoginActivity.this, "Global error in response", Toast.LENGTH_LONG).show();
					} else {
						final String value = location.getValue();
						if (value.contains("account/loginAuth")) { // error
							String errorCode = null;
							int i = value.indexOf("error=");
							if (i >= 0) {
								int i1 = value.indexOf("&", i);
								if (i1 < 0) {
									errorCode = value.substring(i);
								} else {
									errorCode = value.substring(i, i1);
								}
							}

							if (errorCode == null) {
								Toast.makeText(LoginActivity.this, "Unknown auth error", Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(LoginActivity.this, "Auth error: " + errorCode, Toast.LENGTH_SHORT).show();
							}
							return false;
						} else {
							return true;
						}
					}
				} catch (IOException ex) {
					Toast.makeText(LoginActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
				}
				return false;
			}

			@Override
			protected void onPostExecute(Boolean success) {
				if (success) {
					if (rememberMe) {
						final SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
						final SharedPreferences.Editor edit = preferences.edit();
						edit.putString("username", username);
						edit.putString("password", password);
						edit.commit();
					}
					startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
				}
			}
		}.execute(username, password);
	}
}
