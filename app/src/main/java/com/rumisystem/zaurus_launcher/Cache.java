package com.rumisystem.zaurus_launcher;

import static com.rumisystem.zaurus_launcher.Activity.MainActivity.appContext;
import static com.rumisystem.zaurus_launcher.LIB.APPDATA_TO_APINFO;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.rumisystem.zaurus_launcher.GATA.APPData.APPData;

import java.io.File;
import java.io.FileOutputStream;

public class Cache {
	private static String PATH = appContext.getExternalCacheDir().getPath();

	//キャッシュの初期化
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

	//アプリアイコンのキャッシュ
	public static Drawable GET_DRAWABLE_CACHE(APPData APP, PackageManager PM, Context CONTEXT){
		try{
			//ランチャー独自のアプリではないなら実行
			if (!APP.PACKAGE_NAME.startsWith("rumi_zaurus")) {
				//キャッシュのファイル名は、
				ApplicationInfo APPINFO = APPDATA_TO_APINFO(APP, CONTEXT);
				String FILE_NAME = APPINFO.packageName.replace(".", "_");

				//アプリアイコンのキャッシュ
				File FILE = new File(PATH, FILE_NAME);

				//アプリアイコンのキャッシュはあるか
				if (!FILE.exists()) {
					System.out.println("キャッシュを作成しました：" + FILE_NAME);

					//アプリアイコンを取得
					Drawable DW = PM.getApplicationIcon(APPINFO.packageName);

					//キャッシュを作成
					SAVE_DRAWABLE(DW, FILE_NAME);
				}

				//アプリアイコンを返す
				return Drawable.createFromPath(FILE.getPath());
			} else {
				Bitmap BITMAP = BitmapFactory.decodeResource(CONTEXT.getResources(), R.drawable.ic_launcher_background);
				BitmapDrawable BDW = new BitmapDrawable(CONTEXT.getResources(), BITMAP);
				return BDW;
			}
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
				//SVGをビットマップに変換する
				BITMAP = Bitmap.createBitmap(DW.getIntrinsicWidth(), DW.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
				Canvas CANVAS = new Canvas(BITMAP);
				DW.setBounds(0, 0, CANVAS.getWidth(), CANVAS.getHeight());
				DW.draw(CANVAS);
			}

			//キャッシュのファイルを作成する
			File DW_FILE = new File(PATH, FILE_NAME);
			if(!DW_FILE.exists()){
				if(!DW_FILE.createNewFile()){
					//失敗時の処理
					System.out.println("ファイルを作れんかった");
				}
			}

			//画像をリサイズする
			Bitmap RESIZE_BITMAP = Bitmap.createScaledBitmap(BITMAP, 64, 64, true);

			//キャッシュに書き込む
			FileOutputStream FOS = new FileOutputStream(DW_FILE);
			RESIZE_BITMAP.compress(Bitmap.CompressFormat.PNG, 100, FOS);
			FOS.close();
		}catch (Exception EX){
			EX.printStackTrace();
		}
	}
}
