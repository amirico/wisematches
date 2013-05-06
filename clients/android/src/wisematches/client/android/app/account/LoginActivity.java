package wisematches.client.android.app.account;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import wisematches.client.android.R;
import wisematches.client.android.app.WiseMatchesActivity;
import wisematches.client.android.app.account.auth.Authenticator;
import wisematches.client.android.os.ProgressTask;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class LoginActivity extends WiseMatchesActivity {
	private TextView errorMessage;
	private EditText usernameField;
	private EditText passwordField;

	private AccountAuthenticatorResponse mAccountAuthenticatorResponse = null;

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

		mAccountAuthenticatorResponse =
				getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);

		if (mAccountAuthenticatorResponse != null) {
			mAccountAuthenticatorResponse.onRequestContinued();
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
//							WiseMatchesApplication.Principal authenticate = getWMApplication().authenticate(strings[0], strings[1]);
//							startActivity(new Intent(LoginActivity.this, ActiveGamesActivity.class));

							final String username = strings[0];
							final String password = strings[1];

							final Account account = new Account(username, Authenticator.ACCOUNT_TYPE);

							AccountManager accountManager = AccountManager.get(LoginActivity.this);
							accountManager.addAccountExplicitly(account, password, null);

							final Intent intent = new Intent();
							intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, username);
							intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Authenticator.ACCOUNT_TYPE);

							if (mAccountAuthenticatorResponse != null) {
								// send the result bundle back if set, otherwise send an error.
								mAccountAuthenticatorResponse.onResult(intent.getExtras());
								mAccountAuthenticatorResponse = null;
							}
							setResult(RESULT_OK, intent);
//							finish();

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

		registerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				getWiseMatchesClient().open("/account/create", LoginActivity.this);
//				final Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//				intent.putExtra("username", usernameField.getText().toString());
//				startActivity(intent);
			}
		});

		restorePwdButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				getWiseMatchesClient().open("/account/recovery/request", LoginActivity.this);
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
