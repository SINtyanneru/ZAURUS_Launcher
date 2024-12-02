package com.rumisystem.zaurus_launcher.MODULE;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.rumisystem.zaurus_launcher.R;
import com.rumisystem.zaurus_launcher.TYPE.AppData;

import java.util.ArrayList;
import java.util.List;

public class APP_GET {
	public static List<AppData> GET(PackageManager PKM, Context CONTEXT) {
		List<AppData> APP_LIST = new ArrayList<>();

		//APK管理
		APP_LIST.add(new AppData(
				"rumi_zaurus.apk_admin",
				"APK管理",
				31,
				CONTEXT.getDrawable(R.drawable.apk_admin)
		));

		List<PackageInfo> PACKAGE_LIST = PKM.getInstalledPackages(0);
		for (PackageInfo PKG_INFO:PACKAGE_LIST) {
			ApplicationInfo APP = PKG_INFO.applicationInfo;
			Intent LANCH_INTENT = PKM.getLaunchIntentForPackage(PKG_INFO.packageName);
			//ランチャーから起動できるアクティビティがあるか
			if (LANCH_INTENT != null) {
				APP_LIST.add(new AppData(
						APP.packageName,
						PKM.getApplicationLabel(APP).toString(),
						APP.targetSdkVersion,
						PKM.getApplicationIcon(APP)
				));
			}
		}

		return APP_LIST;
	}
}
