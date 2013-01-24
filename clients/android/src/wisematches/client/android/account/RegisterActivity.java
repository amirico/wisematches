package wisematches.client.android.account;

import android.app.Activity;
import android.os.Bundle;
import wisematches.client.android.R;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class RegisterActivity extends Activity {
	public RegisterActivity() {
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_register);
	}
}