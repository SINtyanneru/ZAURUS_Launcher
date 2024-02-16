package com.rumisystem.zaurus_launcher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
	private GridView GRID_VIEW;
	private ArrayList<String> ITEM_LIST;
	private ArrayList<ApplicationInfo> APP_LIST;
	private PackageManager PACKAGE_MANAGER;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);

			//パッケージマネジャーを生成
			PACKAGE_MANAGER = this.getPackageManager();

			//アプリ一覧を入れる配列を初期化
			APP_LIST = new ArrayList<>();

			//リストビューを追加
			GRID_VIEW = findViewById(R.id.APP_LIST);
			ITEM_LIST = new ArrayList<>();

			//ListViewのアイテムがタップされたときの処理を設定
			GRID_VIEW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> PARENT, View VIEW, int POS, long ID) {
					String SELECT_APP_PACKAGE_NAME = APP_LIST.get(POS).packageName.toString();

					try {
						//パッケージがあるか(無ければエラーになる)
						PACKAGE_MANAGER.getPackageInfo(SELECT_APP_PACKAGE_NAME, PackageManager.GET_ACTIVITIES);

						Intent launchIntent = getPackageManager().getLaunchIntentForPackage(SELECT_APP_PACKAGE_NAME);
						if (launchIntent != null) {
							startActivity(launchIntent);
						}
					} catch (PackageManager.NameNotFoundException e) {
						//パッケージがない
					}
				}
			});

			for(ApplicationInfo APP:GET_ALL_APP(this)){
				System.out.println(PACKAGE_MANAGER.getApplicationIcon(APP.packageName));

				APP_LIST.add(APP);
			}

			GRID_VIEW.setAdapter(new AppIconAdapter(this, APP_LIST, PACKAGE_MANAGER));
		} catch (Exception EX) {
			EX.printStackTrace();
		}
	}


	public static List<ApplicationInfo> GET_ALL_APP(Context context) {
		PackageManager packageManager = context.getPackageManager();

		// PackageManagerからインストールされたすべてのアプリケーション情報を取得
		List<ApplicationInfo> apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

		return apps;
	}
}
