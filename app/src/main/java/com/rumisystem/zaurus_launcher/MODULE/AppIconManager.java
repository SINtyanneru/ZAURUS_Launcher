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
		return PM.getApplicationIcon(APP);
	}
}
