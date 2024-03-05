package com.rumisystem.zaurus_launcher;

import static com.rumisystem.zaurus_launcher.MainActivity.appContext;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

public class Cache {
	private static String PATH = appContext.getExternalCacheDir().getPath();

	public static void INIT(){
		try{
			File CACHE_DIR = new File(PATH);

			if(!CACHE_DIR.exists()){
				System.out.println("キャッシュフォルダを作りましたたた");

				CACHE_DIR.mkdir();
			}
		}catch (Exception EX){
			EX.printStackTrace();
		}
	}

	public static Drawable GET_DRAWABLE_CACHE(Drawable DW, String FILE_NAME){
		try{
			File FILE = new File(PATH, FILE_NAME);

			if (!FILE.exists()) {
				System.out.println("キャッシュを作成しました：" + FILE_NAME);
				SAVE_DRAWABLE(DW, FILE_NAME);
			}

			return Drawable.createFromPath(FILE.getPath());
		}catch (Exception EX){
			EX.printStackTrace();
			return null;
		}
	}

	private static void SAVE_DRAWABLE(Drawable DW, String FILE_NAME){
		try{
			Bitmap BITMAP = null;

			//DWがBitMapならそのまま入れる
			if (DW instanceof BitmapDrawable) {
				BITMAP = ((BitmapDrawable) DW).getBitmap();
			} else {
				BITMAP = Bitmap.createBitmap(DW.getIntrinsicWidth(), DW.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
				Canvas CANVAS = new Canvas(BITMAP);
				DW.setBounds(0, 0, CANVAS.getWidth(), CANVAS.getHeight());
				DW.draw(CANVAS);
			}

			File DW_FILE = new File(PATH, FILE_NAME);
			if(!DW_FILE.exists()){
				if(!DW_FILE.createNewFile()){
					System.out.println("ファイルを作れんかった");
				}
			}

			//画像をリサイズする
			Bitmap RESIZE_BITMAP = Bitmap.createScaledBitmap(BITMAP, 64, 64, true);

			FileOutputStream FOS = new FileOutputStream(DW_FILE);
			RESIZE_BITMAP.compress(Bitmap.CompressFormat.PNG, 100, FOS);
			FOS.close();
		}catch (Exception EX){
			EX.printStackTrace();
		}
	}
}
