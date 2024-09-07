package com.rumisystem.zaurus_launcher;

import static com.rumisystem.zaurus_launcher.LIB.MESSAGE_BOX_SHOW;
import static com.rumisystem.zaurus_launcher.Activity.MainActivity.VERSION;
import static com.rumisystem.zaurus_launcher.Activity.MainActivity.appContext;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class CONFIG {
	public static String APP_DIR = appContext.getExternalFilesDir(null).getPath();

	//設定ファイルの初期化
	public static void INIT(){
		//設定ファイルを初期化（なければ作る）
		GEN_CONFIG();

		//インデックスファイルを初期化
		INDEX_DATA.INDEX_INIT();
	}

	private static void GEN_CONFIG(){
		try{
			//ファイルを準備
			File VERSION_FILE = new File(APP_DIR, "VERSION");

			//ファイルが存在するか
			if(VERSION_FILE.exists()){

			} else {
				//書き込む
				BufferedWriter WRITER = new BufferedWriter(new FileWriter(VERSION_FILE));

				//書き込み
				WRITER.write(VERSION);

				//メモリ開放
				WRITER.close();
			}
		}catch (Exception EX){
			MESSAGE_BOX_SHOW(appContext, "エラー", EX.getMessage());
		}
	}
}
