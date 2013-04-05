package wisematches.client.android.activity.account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import wisematches.client.android.AuthenticationException;
import wisematches.client.android.R;
import wisematches.client.android.WMActivity;
import wisematches.client.android.WMApplication;
import wisematches.client.android.activity.playground.DashboardActivity;

import java.io.Serializable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class SignInActivity extends WMActivity {
	private TextView errorMessage = null;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_in);

		final ClearErrorListener clearErrorListener = new ClearErrorListener();

		final Button signInButton = (Button) findViewById(R.id.accountButtonCreate);
		errorMessage = (TextView) findViewById(R.id.accountErrorMessage);

		final EditText usernameField = (EditText) findViewById(R.id.accountFieldEmail);
		usernameField.addTextChangedListener(clearErrorListener);

		final EditText passwordField = (EditText) findViewById(R.id.accountFieldPassword);
		passwordField.addTextChangedListener(clearErrorListener);

		final AuthenticationException serializable = (AuthenticationException) getIntent().getExtras().get("authenticationException");
		if (serializable != null) {
			errorMessage.setText(serializable.getMessage());
			errorMessage.setVisibility(View.VISIBLE);
		}

		// TODO: debug variables
		passwordField.setText("test");
		usernameField.setText("test@test.ri");

		signInButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				doSignIn(usernameField.getText().toString(), passwordField.getText().toString());
			}
		});
	}

	private Exception performSignIn(String username, String password) {
		return ((WMApplication) getApplication()).openSession(username, password);
	}

	private void signInFinished(Exception error) {
		Log.i("WM", "signInFinished: " + error);
		if (error == null) {
			startActivity(new Intent(this, DashboardActivity.class));
		} else {
			if (error instanceof SignInFailureException) {
				errorMessage.setText(getResources().getString(((SignInFailureException) error).getErrorCodeString()));
			} else {
				errorMessage.setText(error.getMessage());
			}
			errorMessage.setVisibility(View.VISIBLE);
		}
	}

	private void signInCancelled() {
//		if (activeRequest != null) {
//			activeRequest.abort();
//			activeRequest = null;
//		}
	}

	private void doSignIn(final String username, final String password) {
		clearError();

		new SignInTask().execute(username, password);
	}

	private void clearError() {
		if (errorMessage.getVisibility() == View.VISIBLE) {
			errorMessage.setText("");
			errorMessage.setVisibility(View.GONE);
		}
	}

	private class SignInTask extends AsyncTask<String, Void, Exception> {
		private SignInTask() {
		}

		@Override
		protected Exception doInBackground(String... strings) {
			return performSignIn(strings[0], strings[1]);
		}

		@Override
		protected void onPostExecute(Exception error) {
			signInFinished(error);
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
