package com.rumisystem.zaurus_launcher;

import static com.rumisystem.zaurus_launcher.INDEX_DATA.DELETE_INDEX_CONTENTS;
import static com.rumisystem.zaurus_launcher.INDEX_DATA.INDEX_LIST;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.widget.GridView;

import androidx.appcompat.app.AlertDialog;

import com.rumisystem.zaurus_launcher.GATA.APPData.APPData;
import com.rumisystem.zaurus_launcher.GATA.AppIconAdapter;

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

	public static List<APPData> GET_ALL_APP(Context context) {
		List<APPData> APP_LIST = new ArrayList<>();

		//すべてのアプリを取得
		List<ApplicationInfo> APINFO_LIST = context.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
		//取得したアプリを全て
		for (ApplicationInfo APINFO:APINFO_LIST) {
			APP_LIST.add(APINFO_TO_APPDATA(APINFO));
		}

		return APP_LIST;
	}

	/**
	 * APPDataをApplicationInfoへ変換する
	 * @param APPDATA
	 * @param context
	 * @return ApplicationInfo
	 * @throws PackageManager.NameNotFoundException
	 */
	public static ApplicationInfo APPDATA_TO_APINFO(APPData APPDATA, Context context) throws PackageManager.NameNotFoundException {
		return context.getPackageManager().getApplicationInfo(APPDATA.PACKAGE_NAME, 0);
	}

	/**
	 * ApplicationInfoをAPPDataへ変換する
	 * @param APINFO
	 * @return APPData
	 */
	public static APPData APINFO_TO_APPDATA(ApplicationInfo APINFO) {
		APPData APP_DATA = new APPData();

		APP_DATA.NAME = APINFO.name;
		APP_DATA.PACKAGE_NAME = APINFO.packageName;
		APP_DATA.API_LEVEL = APINFO.targetSdkVersion;

		return APP_DATA;
	}

	//インデックスを読み込む（再読込含む）
	public static void LOAD_INDEX(Context appContext, ArrayList<APPData> APP_LIST, PackageManager PACKAGE_MANAGER, GridView GRID_VIEW, String ID){
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
							APP_LIST.add(APINFO_TO_APPDATA(APP));
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
