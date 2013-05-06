package wisematches.client.android.app.account.auth;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import wisematches.client.android.app.account.AuthenticationActivity;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class Authenticator extends AbstractAccountAuthenticator {
	private final Context mContext;

	public static final String ACCOUNT_TYPE = "net.wisematches.android.auth";
	public static final String AUTH_TOKEN_TYPE = "net.wisematches.android.auth";

	public static final String PARAM_CONFIRMCREDENTIALS = "confirmCredentials";
	public static final String PARAM_PASSWORD = "password";
	public static final String PARAM_USERNAME = "username";
	public static final String PARAM_AUTHTOKEN_TYPE = "authtokenType";

	public Authenticator(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	public Bundle addAccount(AccountAuthenticatorResponse response,
							 String accountType, String authTokenType, String[] requiredFeatures, Bundle options) {
		final Intent intent = new Intent(mContext, AuthenticationActivity.class);
		intent.putExtra(PARAM_AUTHTOKEN_TYPE, authTokenType);
		intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

		final Bundle bundle = new Bundle();
		bundle.putParcelable(AccountManager.KEY_INTENT, intent);
		return bundle;
	}

	@Override
	public Bundle confirmCredentials(AccountAuthenticatorResponse response,
									 Account account, Bundle options) {
		if (options != null && options.containsKey(AccountManager.KEY_PASSWORD)) {
			final String password = options.getString(AccountManager.KEY_PASSWORD);
			final boolean verified = onlineConfirmPassword(account.name, password);

			final Bundle result = new Bundle();
			result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, verified);
			return result;
		}

		final Intent intent = new Intent(mContext, AuthenticationActivity.class);
		intent.putExtra(PARAM_USERNAME, account.name);
		intent.putExtra(PARAM_CONFIRMCREDENTIALS, true);
		intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

		final Bundle bundle = new Bundle();
		bundle.putParcelable(AccountManager.KEY_INTENT, intent);
		return bundle;
	}

	@Override
	public Bundle editProperties(AccountAuthenticatorResponse response,
								 String accountType) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Bundle getAuthToken(AccountAuthenticatorResponse response,
							   Account account, String authTokenType, Bundle loginOptions) {
		if (!authTokenType.equals(AUTH_TOKEN_TYPE)) {
			final Bundle result = new Bundle();
			result.putString(AccountManager.KEY_ERROR_MESSAGE,
					"invalid authTokenType");
			return result;
		}
		final AccountManager am = AccountManager.get(mContext);
		final String password = am.getPassword(account);
		if (password != null) {
			final boolean verified = onlineConfirmPassword(account.name, password);
			if (verified) {
				final Bundle result = new Bundle();
				result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
				result.putString(AccountManager.KEY_ACCOUNT_TYPE, ACCOUNT_TYPE);
				result.putString(AccountManager.KEY_AUTHTOKEN, password);
				return result;
			}
		}

		// the password was missing or incorrect, return an Intent to an
		// Activity that will prompt the user for the password.
		final Intent intent = new Intent(mContext, AuthenticationActivity.class);
		intent.putExtra(PARAM_USERNAME, account.name);
		intent.putExtra(PARAM_AUTHTOKEN_TYPE, authTokenType);
		intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

		final Bundle bundle = new Bundle();
		bundle.putParcelable(AccountManager.KEY_INTENT, intent);
		return bundle;
	}

	@Override
	public String getAuthTokenLabel(String authTokenType) {
		return null;
	}

	@Override
	public Bundle hasFeatures(AccountAuthenticatorResponse response,
							  Account account, String[] features) {
		final Bundle result = new Bundle();
		result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
		return result;
	}

	/**
	 * Validates user's password on the server
	 */
	private boolean onlineConfirmPassword(String username, String password) {
		return true;
//		return NetworkUtilities.authenticate(username, password, null, null);
	}

	@Override
	public Bundle updateCredentials(AccountAuthenticatorResponse response,
									Account account, String authTokenType, Bundle loginOptions) {
		final Intent intent = new Intent(mContext, AuthenticationActivity.class);
		intent.putExtra(PARAM_USERNAME, account.name);
		intent.putExtra(PARAM_AUTHTOKEN_TYPE, authTokenType);
		intent.putExtra(PARAM_CONFIRMCREDENTIALS, false);

		final Bundle bundle = new Bundle();
		bundle.putParcelable(AccountManager.KEY_INTENT, intent);
		return bundle;
	}
}
