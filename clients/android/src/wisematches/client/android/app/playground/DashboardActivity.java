package wisematches.client.android.app.playground;

import android.os.Bundle;
import android.widget.TextView;
import wisematches.client.android.R;
import wisematches.client.android.app.WiseMatchesActivity;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DashboardActivity extends WiseMatchesActivity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playground_dashboard);

		final TextView text = (TextView) findViewById(R.id.gamesActiveFldTest);
		text.setText(getWiseMatches().getPrincipal().toString());
	}
}