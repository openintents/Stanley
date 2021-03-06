package fr.xgouchet.packageexplorer.model;

import java.io.File;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import fr.xgouchet.packageexplorer.common.ManifestUtils;

public class AsyncManifestExporter extends AsyncTask<PackageInfo, Void, File> {

	public static interface ManifestExporterListener {
		public void onManifestExported(File file);

		public void onExportError(Exception exception);
	}

	public AsyncManifestExporter(final Activity activity,
			final ManifestExporterListener listener) {
		mActivity = activity;
		mListener = listener;
	}

	@Override
	protected File doInBackground(final PackageInfo... params) {
		File dest = null;

		try {
			dest = ManifestUtils.exportManifest(params[0], mActivity);
		} catch (Exception e) {
			mException = e;
		}

		return dest;
	}

	@Override
	protected void onPostExecute(final File result) {
		super.onPostExecute(result);

		if (mException == null) {
			mListener.onManifestExported(result);
		} else {
			mListener.onExportError(mException);
		}

	}

	private Exception mException;
	private final Activity mActivity;
	private final ManifestExporterListener mListener;
}
