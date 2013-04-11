package wisematches.client.android.view.playground;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import wisematches.client.android.R;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleInfoAdapter extends ArrayAdapter<ScribbleInfo> {
	private final Context context;
	private final int layoutResourceId;
	private final ScribbleInfo data[];

	public ScribbleInfoAdapter(Context context, int layoutResourceId, ScribbleInfo[] data) {
		super(context, layoutResourceId);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		ScribbleInfoHolder holder = null;
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new ScribbleInfoHolder();
			holder.view = (TextView) row.findViewById(R.id.textView);

			row.setTag(holder);
		} else {
			holder = (ScribbleInfoHolder) row.getTag();
		}

		ScribbleInfo weather = data[position];
		holder.view.setText(weather.getTitle());
		return row;
	}

	private static class ScribbleInfoHolder {
		TextView view;
	}
}
