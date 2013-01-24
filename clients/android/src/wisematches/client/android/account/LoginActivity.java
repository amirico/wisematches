package wisematches.client.android.account;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.*;
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
	private TextView errorMessage = null;

	private HttpPost activeRequest = null;
	private ProgressDialog progressDialog = null;

	public LoginActivity() {
	}

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.accout_login);

		final ClearErrorListener clearErrorListener = new ClearErrorListener();

		final Button signInButton = (Button) findViewById(R.id.accountButtonCreate);
		errorMessage = (TextView) findViewById(R.id.accountErrorMessage);

		final EditText usernameField = (EditText) findViewById(R.id.accountFieldEmail);
		usernameField.addTextChangedListener(clearErrorListener);

		final EditText passwordField = (EditText) findViewById(R.id.accountFieldPassword);
		passwordField.addTextChangedListener(clearErrorListener);

		final CheckBox rememberMeField = (CheckBox) findViewById(R.id.accountFieldRememberMe);

		signInButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				doSignIn(usernameField.getText().toString(), passwordField.getText().toString(), rememberMeField.isChecked());
			}
		});

		final SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
		if (preferences.getBoolean("rememberMe", false)) {
			final String username = preferences.getString("username", null);
			final String password = preferences.getString("password", null);

			usernameField.setText(username);
			passwordField.setText(password);
			rememberMeField.setChecked(true);

			doSignIn(username, password, true);
		}
	}

	private Exception performSignIn(String username, String password) {
		try {
			final WisematchesApplication wisematches = (WisematchesApplication) getApplication();

			activeRequest = new HttpPost("/account/loginProcessing");
			final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("j_username", username));
			nameValuePairs.add(new BasicNameValuePair("j_password", password));
			activeRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			final HttpResponse execute = wisematches.execute(activeRequest);
			if (execute == null) {
				return new LoginFailureException(R.string.account_login_error_system);
			}

			final Header location = execute.getLastHeader("Location");
			if (location == null) {
				return new LoginFailureException(R.string.account_login_error_response);
			} else {
				final String value = location.getValue();
				if (value.contains("account/loginAuth")) { // error
					String errorCode = null;
					int i = value.indexOf("error=");
					if (i >= 0) {
						int i1 = value.indexOf("&", i);
						if (i1 < 0) {
							errorCode = value.substring(i + 6);
						} else {
							errorCode = value.substring(i + 6, i1);
						}
					}

					Log.i("WM", "Error code: " + errorCode);
					if (errorCode == null) {
						return new LoginFailureException(R.string.account_login_error_system);
					} else {
						return new LoginFailureException(R.string.account_login_error_credential);
					}
				} else {
					return null;
				}
			}
		} catch (IOException ex) {
			return ex;
		}
	}

	private void signInFinished(String username, String password, boolean rememberMe, Exception error) {
		Log.i("WM", "signInFinished: " + error);
		if (error == null) {
			final SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
			final SharedPreferences.Editor edit = preferences.edit();
			if (rememberMe) {
				edit.putString("username", username);
				edit.putString("password", password);
				edit.putBoolean("rememberMe", true);
			} else {
				edit.remove("username");
				edit.remove("password");
				edit.remove("rememberMe");
			}
			edit.commit();
			startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
		} else {
			if (error instanceof LoginFailureException) {
				errorMessage.setText(getResources().getString(((LoginFailureException) error).getErrorCodeString()));
			} else {
				errorMessage.setText(error.getMessage());
			}
			errorMessage.setVisibility(View.VISIBLE);

			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}
		activeRequest = null;
	}

	private void signInCancelled() {
		if (activeRequest != null) {
			activeRequest.abort();
			activeRequest = null;
		}
	}

	private void doSignIn(final String username, final String password, final boolean rememberMe) {
		clearError();

		final SignInTask task = new SignInTask(username, password, rememberMe);
		progressDialog = ProgressDialog.show(LoginActivity.this, null, "Logging in to WiseMatches. Please wait...", true, true, new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialogInterface) {
				dialogInterface.dismiss();
				task.cancel(true);
			}
		});
		task.execute((String) null);
	}

	private void clearError() {
		if (errorMessage.getVisibility() == View.VISIBLE) {
			errorMessage.setText("");
			errorMessage.setVisibility(View.GONE);
		}
	}

	private class SignInTask extends AsyncTask<String, Void, Exception> {
		private final String username;
		private final String password;
		private final boolean rememberMe;

		private SignInTask(String username, String password, boolean rememberMe) {
			this.username = username;
			this.password = password;
			this.rememberMe = rememberMe;
		}

		@Override
		protected Exception doInBackground(String... strings) {
			return performSignIn(username, password);
		}

		@Override
		protected void onPostExecute(Exception error) {
			signInFinished(username, password, rememberMe, error);
		}

		@Override
		protected void onCancelled(Exception error) {
			signInCancelled();
		}

		@Override
		protected void onCancelled() {
			signInCancelled();
		}
	}

	private class ClearErrorListener implements TextWatcher {
		private ClearErrorListener() {
		}

		@Override
		public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
		}

		@Override
		public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			clearError();
		}

		@Override
		public void afterTextChanged(Editable editable) {
		}
	}
}
