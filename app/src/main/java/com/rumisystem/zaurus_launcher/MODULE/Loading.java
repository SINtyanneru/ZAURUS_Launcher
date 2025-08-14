package com.rumisystem.zaurus_launcher.MODULE;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.ProgressBar;

public class Loading {
	private static AlertDialog dialog;

	public static void show(Context ctx) {
		if (dialog != null) {
			close();
		}

		ProgressBar progress_bar = new ProgressBar(ctx, null, android.R.attr.progressBarStyleHorizontal);
		progress_bar.setIndeterminate(true);
		progress_bar.setPadding(50, 50, 50 ,50);

		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle("お待ち下さい。");
		builder.setView(progress_bar);
		builder.setCancelable(false);

		AlertDialog loading_dialog = builder.create();
		loading_dialog.show();
		dialog = loading_dialog;
	}

	public static void close() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			dialog = null;
		}
	}
}
