package com.rumisystem.zaurus_launcher.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.rumisystem.zaurus_launcher.Activity.MODULE.AppGridAdapter;
import com.rumisystem.zaurus_launcher.Activity.TYPE.AppData;
import com.rumisystem.zaurus_launcher.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
	private Context CONTEXT;
	private List<AppData> APP_LIST = new ArrayList<>();
	private PackageManager PKM;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);

			//タクスバーを削除
			getSupportActionBar().hide();

			//アクティビティを表示
			setContentView(R.layout.activity_main);

			CONTEXT = this;
			PKM = this.getPackageManager();

			List<PackageInfo> PACKAGE_LIST = PKM.getInstalledPackages(0);
			for (PackageInfo PKG_INFO:PACKAGE_LIST) {
				ApplicationInfo APP = PKG_INFO.applicationInfo;
				Intent LANCH_INTENT = getPackageManager().getLaunchIntentForPackage(PKG_INFO.packageName);
				//ランチャーから起動できるアクティビティがあるか
				if (LANCH_INTENT != null) {
					APP_LIST.add(new AppData(
						APP.packageName,
						PKM.getApplicationLabel(APP).toString(),
						APP.targetSdkVersion,
						PKM.getApplicationIcon(APP)
					));
				}
			}

			GridView GRID_VIEW = findViewById(R.id.AppList);

			//アプリ一覧をセット
			AppGridAdapter ADAPTER = new AppGridAdapter(this, APP_LIST, PKM);
			GRID_VIEW.setAdapter(ADAPTER);

			//タップ時のイベント
			GRID_VIEW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> ADAPTER_VIEW, View VIEW, int I, long L) {
					try {
						APP_LIST.get(I).Run(CONTEXT, PKM);
					} catch (Exception EX) {
						EX.printStackTrace();
					}
				}
			});
		} catch (Exception EX) {
			EX.printStackTrace();
		}
	}
}
