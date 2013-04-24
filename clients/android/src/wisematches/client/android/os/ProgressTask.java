package wisematches.client.android.os;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class ProgressTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
	private String title;
	private Context context;
	private ProgressDialog progressDialog = null;

	protected ProgressTask(String title, Context context) {
		this.title = title;
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		// TODO: get title from resources
//		final String string = context.getResources().getString(12);
		progressDialog = ProgressDialog.show(context, null, title, true, true, new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialogInterface) {
				cancel(true);
				dialogInterface.dismiss();
			}
		});
	}

	@Override
	protected void onPostExecute(Result result) {
		super.onPostExecute(result);
		progressDialog.dismiss();
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		progressDialog.dismiss();
	}
}
