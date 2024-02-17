package com.rumisystem.zaurus_launcher;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AlertDialog;

import java.util.List;

public class LIB {
	public static void MESSAGE_BOX_SHOW(Context appContext, String TITLE, String TEXT){
		new AlertDialog.Builder(appContext)
				.setTitle(TITLE)
				.setMessage(TEXT)
				.setPositiveButton("おｋ", null)
				.show();
	}

	public static List<ApplicationInfo> GET_ALL_APP(Context context) {
		PackageManager packageManager = context.getPackageManager();

		// PackageManagerからインストールされたすべてのアプリケーション情報を取得
		List<ApplicationInfo> apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

		return apps;
	}
}
