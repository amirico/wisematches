package wisematches.client.android.app.account;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import wisematches.client.android.R;
import wisematches.client.android.WiseMatchesApplication;
import wisematches.client.android.app.WiseMatchesActivity;
import wisematches.client.android.app.playground.DashboardActivity;
import wisematches.client.android.os.ProgressTask;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class LoginActivity extends WiseMatchesActivity {
	private TextView errorMessage;
	private EditText usernameField;
	private EditText passwordField;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_signin);

		final ClearErrorListener clearErrorListener = new ClearErrorListener();

		final View signInButton = findViewById(R.id.loginBtnSignIn);
		final View registerButton = findViewById(R.id.loginBtnRegister);
		final View restorePwdButton = findViewById(R.id.loginLinkForgot);

		errorMessage = (TextView) findViewById(R.id.loginFldError);

		usernameField = (EditText) findViewById(R.id.loginFldLogin);
		usernameField.addTextChangedListener(clearErrorListener);

		passwordField = (EditText) findViewById(R.id.loginFldPwd);
		passwordField.addTextChangedListener(clearErrorListener);

		final Bundle extras = getIntent().getExtras();
		if (extras != null) {
			setErrorMessage(extras.getString("ErrorCode"));
		}

		setErrorMessage(null);
		initDefaultValues();

		signInButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setErrorMessage(null);

				ProgressTask<String, Void, Exception> task = new ProgressTask<String, Void, Exception>("Login to WiseMatches server. Please wait...", LoginActivity.this) {
					@Override
					protected Exception doInBackground(String... strings) {
						try {
							getWiseMatches().authenticate(strings[0], strings[1]);
							startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
							return null;
						} catch (Exception ex) {
							return ex;
						}
					}

					@Override
					protected void onPostExecute(Exception ex) {
						super.onPostExecute(ex);
						if (ex != null) {
							setErrorMessage(ex.getMessage());
						}
					}
				};
				task.execute(usernameField.getText().toString(), passwordField.getText().toString());
			}
		});

		final WiseMatchesApplication application = getWiseMatches();

		registerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + application.getServerHost() + "/account/create"));
				startActivity(Intent.createChooser(intent, "Chose browser"));
//				final Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//				intent.putExtra("username", usernameField.getText().toString());
//				startActivity(intent);
			}
		});

		restorePwdButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + application.getServerHost() + "/account/recovery/request"));
				startActivity(Intent.createChooser(intent, "Chose browser"));
//				final Intent intent = new Intent(LoginActivity.this, RestoreActivity.class);
//				intent.putExtra("username", usernameField.getText().toString());
//				startActivity(intent);
			}
		});
	}

	private void initDefaultValues() {
		// TODO: incorrect
		usernameField.setText("test@test.ri");
		passwordField.setText("test");
	}

	private void setErrorMessage(String msg) {
		if (msg == null) {
			errorMessage.setText("");
			errorMessage.setVisibility(View.GONE);
		} else {
			errorMessage.setText(msg);
			errorMessage.setVisibility(View.VISIBLE);
		}
	}

	private final class ClearErrorListener implements TextWatcher {
		private ClearErrorListener() {
		}

		@Override
		public void afterTextChanged(Editable editable) {
		}

		@Override
		public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			setErrorMessage(null);
		}

		@Override
		public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
		}
	}
}
