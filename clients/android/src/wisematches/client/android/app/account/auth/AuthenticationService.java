package wisematches.client.android.app.account.auth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class AuthenticationService extends Service {
	private Authenticator mAuthenticator;

	@Override
	public void onCreate() {
		mAuthenticator = new Authenticator(this);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mAuthenticator.getIBinder();
	}

	@Override
	public void onDestroy() {
	}
}
