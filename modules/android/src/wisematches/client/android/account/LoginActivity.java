package wisematches.client.android.account;

import android.app.Activity;
import android.content.Intent;
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
import wisematches.client.android.WisematchesAndroid;
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
		final WisematchesAndroid wisematches = (WisematchesAndroid) getApplication();

		setContentView(R.layout.accout_login);
/*
		startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
		if (true) {
			return;
		}
*/

		final Button signIn = (Button) findViewById(R.id.accountButtonCreate);
		signIn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final EditText email = (EditText) findViewById(R.id.accountFieldEmail);
				final EditText password = (EditText) findViewById(R.id.accountFieldPassword);
				final CheckBox rememberMe = (CheckBox) findViewById(R.id.accountFieldRememberMe);

				try {
					final HttpPost req = new HttpPost("/account/loginProcessing");
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
					nameValuePairs.add(new BasicNameValuePair("j_username", email.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("j_password", password.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("rememberMe", String.valueOf(rememberMe.isChecked())));
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
						} else { //success
							startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
						}
					}
				} catch (IOException ex) {
					Toast.makeText(LoginActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
				}
			}
		});
	}
}
