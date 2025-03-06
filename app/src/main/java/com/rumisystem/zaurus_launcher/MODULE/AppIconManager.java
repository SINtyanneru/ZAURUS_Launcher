package com.rumisystem.zaurus_launcher.MODULE;

import static com.rumisystem.zaurus_launcher.Activity.MainActivity.CACHE_DIR;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AppIconManager {
	public static Drawable Get(ApplicationInfo APP, PackageManager PM) throws IOException {
		String PackageID = APP.packageName;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
			if (Files.exists(Path.of(CACHE_DIR + "/" + PackageID))) {
				return Load(PackageID);
			} else {
				Drawable D = PM.getApplicationIcon(APP);
				Save(D, PackageID);
				return D;
			}
		} else {
			Drawable D = PM.getApplicationIcon(APP);
			return D;
		}
	}

	private static void Save(Drawable D, String PackageID) throws IOException {
		//変換
		Bitmap BMP = Bitmap.createBitmap(
			D.getIntrinsicWidth(),
			D.getIntrinsicHeight(),
			Bitmap.Config.ARGB_8888
		);
		Canvas CANVAS = new Canvas(BMP);
		D.setBounds(0, 0, CANVAS.getWidth(), CANVAS.getHeight());
		D.draw(CANVAS);

		//保存
		FileOutputStream FOS = new FileOutputStream(new File(CACHE_DIR + "/" + PackageID));
		BMP.compress(Bitmap.CompressFormat.JPEG, 50, FOS);
		FOS.close();
	}

	private static Drawable Load(String PackageID) throws IOException {
		FileInputStream FIS = new FileInputStream(new File(CACHE_DIR + "/" + PackageID));
		Bitmap BMP = BitmapFactory.decodeStream(FIS);
		FIS.close();
		return new BitmapDrawable(null, BMP);
	}
}
