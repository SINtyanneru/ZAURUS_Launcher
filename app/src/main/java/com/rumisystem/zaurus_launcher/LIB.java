package com.rumisystem.zaurus_launcher;

import static com.rumisystem.zaurus_launcher.INDEX_DATA.DELETE_INDEX_CONTENTS;
import static com.rumisystem.zaurus_launcher.INDEX_DATA.INDEX_LIST;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.widget.GridView;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.HashMap;
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

	//インデックスを読み込む（再読込含む）
	public static void LOAD_INDEX(Context appContext, ArrayList<ApplicationInfo> APP_LIST, PackageManager PACKAGE_MANAGER, GridView GRID_VIEW, String ID){
		try{
			for(HashMap<String, Object> ROW:INDEX_LIST){
				//指定されたIDとインデックスのIDが一致するか(一致するまで回す)
				if(ROW.get("ID").toString().equals(ID)){
					System.out.println("読み込み：" + ID);

					//アプリ一覧をクリア
					APP_LIST.clear();

					for(String PACKAGE_NAME:(List<String>)ROW.get("CONTENTS")){
						//パッケージが存在するか
						try{
							ApplicationInfo APP = PACKAGE_MANAGER.getApplicationInfo(PACKAGE_NAME, PackageManager.GET_META_DATA);
							APP_LIST.add(APP);
						} catch (PackageManager.NameNotFoundException E) {
							//パッケージがない
							//ので消す
							DELETE_INDEX_CONTENTS(ID, PACKAGE_NAME);
							System.out.println("パッケージが無いので消します");
						}
					}

					GRID_VIEW.setAdapter(new AppIconAdapter(appContext, APP_LIST, PACKAGE_MANAGER));
				}
			}
		} catch (Exception EX){
			EX.printStackTrace();
			MESSAGE_BOX_SHOW(appContext, "エラー：LIB/LOAD_INDEX", EX.getMessage());
		}
	}
}
