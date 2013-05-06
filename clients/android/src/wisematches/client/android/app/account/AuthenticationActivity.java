package wisematches.client.android.app.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import wisematches.client.android.R;
import wisematches.client.android.app.WiseMatchesActivity;
import wisematches.client.android.app.account.auth.Authenticator;

public class AuthenticationActivity extends WiseMatchesActivity {
	private TextView status;
//	private AsyncTask<Void, Void, WiseMatchesApplication.Principal> authTask;

	//	private Thread mAuthThread;
//	private String mAuthtoken;
//	private String mAuthtokenType;
	private AccountManager mAccountManager;

//	private String mUsername;
//	private String mPassword;
//
//	private EditText mUsernameEdit;
//	private EditText mPasswordEdit;

//	private Boolean mConfirmCredentials = Boolean.FALSE;
//	protected boolean mRequestNewAccount = false;

//	private final Handler mHandler = new Handler();

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		status = (TextView) findViewById(R.id.splashFldStatus);
		status.setText("Preparing resources...");

		mAccountManager = AccountManager.get(this);

		final Account[] accountsByType = mAccountManager.getAccountsByType(Authenticator.ACCOUNT_TYPE);
		status.setText("Found accounts: " + accountsByType.length);

		if (accountsByType.length == 0) {
			startActivity(new Intent(AuthenticationActivity.this, LoginActivity.class));
		} else if (accountsByType.length == 1) {
			final String password = mAccountManager.getPassword(accountsByType[0]);

		} else {
			status.setText("I have to do something here? Hm, not implemented");
		}

/*

		final Intent intent = getIntent();
		mUsername = intent.getStringExtra(Authenticator.PARAM_USERNAME);
		mAuthtokenType = intent.getStringExtra(Authenticator.PARAM_AUTHTOKEN_TYPE);

		mRequestNewAccount = mUsername == null;
		mConfirmCredentials = intent.getBooleanExtra(Authenticator.PARAM_CONFIRMCREDENTIALS, false);
*/

//		showDialog(1);

/*
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		setContentView(R.layout.login_activity);
		getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, android.R.drawable.ic_dialog_alert);

		mMessage = (TextView) findViewById(R.id.message);
		mUsernameEdit = (EditText) findViewById(R.id.username_edit);
		mPasswordEdit = (EditText) findViewById(R.id.password_edit);

		mUsernameEdit.setText(mUsername);
		mMessage.setText(getMessage());
*/

/*
		final AccountManager am = AccountManager.get(this);

		final Account[] accounts = am.getAccountsByType("net.wisematches.android.auth");
		status.setText("Fount accounts: " + accounts.length);

		final Bundle options = new Bundle();

		final Account account = new Account("test@test.ri", "net.wisematches.android.auth");

		AccountManagerFuture<Bundle> authToken = am.getAuthToken(account, "net.wisematches.android.auth", options, this, new OnTokenAcquired(), new Handler(new OnError()));
*/

//		doAuth();
	}

/*
	private class OnTokenAcquired implements AccountManagerCallback<Bundle> {
		@Override
		public void run(AccountManagerFuture<Bundle> future) {
			status.setText("RESULT: success");
		}
	}

	private class OnError implements Handler.Callback {
		@Override
		public boolean handleMessage(Message msg) {
			status.setText("ERROR: " + msg.toString());
			return true;
		}
	}
*/

/*
	private void doAuth() {
		authTask = new AsyncTask<Void, Void, WiseMatchesApplication.Principal>() {
			@Override
			protected WiseMatchesApplication.Principal doInBackground(Void... voids) {
				WiseMatchesApplication.Principal player = null;
				try {
					player = getWMApplication().authenticate();
					if (player != null) {
						startActivity(new Intent(EntryPointActivity.this, ActiveGamesActivity.class));
					} else {
						startActivity(new Intent(EntryPointActivity.this, LoginActivity.class));
					}
				} catch (CommunicationException ex) {
					authTask = null;
					if (ex.getStatusCode() == 401) {
						final Intent intent = new Intent(EntryPointActivity.this, LoginActivity.class);
						intent.putExtra("ErrorCode", ex.getStatusReason());
						startActivity(intent);
					} else {
						status.setText("Illegal return code received: " + ex.getStatusReason() + " [" + ex.getStatusCode() + "]. Tap to try again.");
					}
				} catch (CooperationException ex) {
					authTask = null;
					status.setText("Server communication error: " + ex.getMessage() + ". Tap to try again.");
				}
				return player;
			}

			@Override
			protected void onCancelled() {
				startActivity(new Intent(EntryPointActivity.this, LoginActivity.class));
			}
		};

		status.setText("Authenticating. Please wait...");
		authTask.execute();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (authTask != null) {
				authTask.cancel(true);
				authTask = null;
			} else {
				doAuth();
			}
		}
		return true;
	}
*/
}
