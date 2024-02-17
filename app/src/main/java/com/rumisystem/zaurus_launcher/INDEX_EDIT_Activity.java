package com.rumisystem.zaurus_launcher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class INDEX_EDIT_Activity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);

			//タクスバーを削除
			getSupportActionBar().hide();
			//表示する
			setContentView(R.layout.index_edit);

			//パッケージマネジャーを生成
			PackageManager PACKAGE_MANAGER = this.getPackageManager();

			//アプリ一覧を入れる配列を初期化
			List<ApplicationInfo> APP_LIST = new ArrayList<>();

			//リストビューを追加
			GridView GRID_VIEW = findViewById(R.id.ALL_APP_LIST);

			//ListViewのアイテムがタップされたときの処理を設定
			GRID_VIEW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> PARENT, View VIEW, int POS, long ID) {
					ApplicationInfo SELECT_APP_PACKAGE = APP_LIST.get(POS);
					Toast.makeText(INDEX_EDIT_Activity.this,  "「" + PACKAGE_MANAGER.getApplicationLabel(SELECT_APP_PACKAGE).toString() + "」が選択されました", Toast.LENGTH_SHORT).show();
				}
			});

			for(ApplicationInfo APP:GET_ALL_APP(this)){
				Intent launchIntent = getPackageManager().getLaunchIntentForPackage(APP.packageName);

				//実行可能なインテントがあるか、無いということはランチャーから実行できないので除外する
				if (launchIntent != null) {
					APP_LIST.add(APP);
				}
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
