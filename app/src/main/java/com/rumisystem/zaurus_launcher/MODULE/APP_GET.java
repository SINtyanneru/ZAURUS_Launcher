package com.rumisystem.zaurus_launcher.MODULE;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.rumisystem.zaurus_launcher.TYPE.AppData;

import java.util.ArrayList;
import java.util.List;

public class APP_GET {
	public static List<AppData> GET(PackageManager PKM) {
		List<AppData> APP_LIST = new ArrayList<>();

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
