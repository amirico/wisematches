package wisematches.client.android.app.account;

import android.os.Bundle;
import wisematches.client.android.R;
import wisematches.client.android.app.WiseMatchesActivity;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class RestoreActivity extends WiseMatchesActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_register);

		getWMServer().open("/account/recovery/request", this);
	}
}
