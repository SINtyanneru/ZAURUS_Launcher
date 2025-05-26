package com.rumisystem.zaurus_launcher.MODULE;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.rumisystem.zaurus_launcher.R;
import com.rumisystem.zaurus_launcher.TYPE.AppData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppGet {
	public static List<AppData> AllGet(PackageManager PKM, Context CONTEXT) throws IOException {
		List<AppData> APP_LIST = new ArrayList<>();

		//APK管理
		APP_LIST.add(new AppData(
			"rumi_zaurus.apk_admin",
			"APK管理",
			31,
			CONTEXT.getDrawable(R.drawable.apk_admin)
		));

		System.out.println("アプリを全ロード中・・・");
		long StartTime = System.nanoTime();
		List<PackageInfo> PACKAGE_LIST = PKM.getInstalledPackages(0);

		Collections.sort(PACKAGE_LIST, (P1, P2)->{
			String Label1 = PKM.getApplicationLabel(P1.applicationInfo).toString();
			String Label2 = PKM.getApplicationLabel(P2.applicationInfo).toString();
			return Label1.compareToIgnoreCase(Label2);
		});

		for (PackageInfo PKG_INFO:PACKAGE_LIST) {
			ApplicationInfo APP = PKG_INFO.applicationInfo;
			Intent LANCH_INTENT = PKM.getLaunchIntentForPackage(PKG_INFO.packageName);
			//ランチャーから起動できるアクティビティがあるか
			if (LANCH_INTENT != null) {
				APP_LIST.add(new AppData(
						APP.packageName,
						PKM.getApplicationLabel(APP).toString(),
						APP.targetSdkVersion,
						AppIconManager.Get(APP, PKM)
				));
			}
		}
		System.out.println("アプリを全ロードした");
		System.out.println("掛かった時間:" + ((System.nanoTime() - StartTime) / 1_000_000.0));

		return APP_LIST;
	}

	public static AppData Get(String PackageName, PackageManager PKM, Context CONTEXT) throws PackageManager.NameNotFoundException {
		switch (PackageName) {
			case "rumi_zaurus.apk_admin": {
				return new AppData(
					"rumi_zaurus.apk_admin",
					"APK管理",
					31,
					CONTEXT.getDrawable(R.drawable.apk_admin)
				);
			}

			default: {
				PackageInfo PKG = PKM.getPackageInfo(PackageName, 0);
				ApplicationInfo APP = PKG.applicationInfo;
				return new AppData(PackageName, PKM.getApplicationLabel(APP).toString(), APP.targetSdkVersion, PKM.getApplicationIcon(APP));
			}
		}
	}
}
