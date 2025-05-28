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
import android.util.LruCache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AppIconManager {
	private static final int CacheSize = 4 * 1024 * 1024;//←4MB
	private static LruCache<String, Bitmap> IconCache = new LruCache<>(CacheSize);

	public static Bitmap Get(ApplicationInfo APP, PackageManager PM) throws IOException {
		if (IconCache.get(APP.packageName) != null) {
			return IconCache.get(APP.packageName);
		} else {
			Drawable OriginalIcon = PM.getApplicationIcon(APP);
			Bitmap BMP = Bitmap.createScaledBitmap(DrawableToBitmap(OriginalIcon), 96, 96, true);
			IconCache.put(APP.packageName, BMP);

			return BMP;
		}
	}

	private static Bitmap DrawableToBitmap(Drawable Drw) {
		if (Drw instanceof  BitmapDrawable) {
			return ((BitmapDrawable) Drw).getBitmap();
		}

		Bitmap BMP = Bitmap.createBitmap(
			Drw.getIntrinsicWidth(),
			Drw.getIntrinsicHeight(),
			Bitmap.Config.ARGB_8888
		);

		Canvas CVS = new Canvas(BMP);
		Drw.setBounds(0, 0, CVS.getWidth(), CVS.getHeight());
		Drw.draw(CVS);

		return BMP;
	}
}
