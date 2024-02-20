package com.rumisystem.zaurus_launcher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class APP_ADMIN_Activity extends AppCompatActivity {
	private ListView APP_LIST_VIEW;
	private ArrayAdapter<String> ADAPTER;
	private ArrayList<String> APP_LISTVIEW_LIST;
	private List<ApplicationInfo> APP_LIST = new ArrayList<>();

	private static Context appContext;

	private ApplicationInfo SELECTED_APP;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);

			//タクスバーを削除
			getSupportActionBar().hide();
			setContentView(R.layout.app_admin);

			appContext = this;

			//リストビューを追加
			APP_LIST_VIEW = findViewById(R.id.ALL_APP_LIST);
			APP_LISTVIEW_LIST = new ArrayList<>();

			//アダプターの初期化
			ADAPTER = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, APP_LISTVIEW_LIST);
			APP_LIST_VIEW.setAdapter(ADAPTER);

			APP_LIST_VIEW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> PARENT, View SELECTED_ITEM, int POS, long ID) {
					if(APP_LIST.get(POS) != null){
						String PACKAGE_NAME = getPackageManager().getApplicationLabel(APP_LIST.get(POS)).toString();

						SELECTED_APP = APP_LIST.get(POS);

						Toast.makeText(appContext,  "「" + PACKAGE_NAME + "」を選択しました", Toast.LENGTH_SHORT).show();
					}
				}
			});

			//一覧を初期化
			ADAPTER.clear();

			for(ApplicationInfo APP:LIB.GET_ALL_APP(appContext)){
				try{
					String PACKAGE_NAME = getPackageManager().getApplicationLabel(APP).toString();
					Intent launchIntent = getPackageManager().getLaunchIntentForPackage(APP.packageName);

					//実行可能なインテントがあるか、無いということはランチャーから実行できないので除外する
					if (launchIntent != null) {
						//一覧に追加
						APP_LISTVIEW_LIST.add(PACKAGE_NAME);

						//配列にも
						APP_LIST.add(APP);
					}
				} catch (Exception EX){
					//握りつぶす
				}
			}

			ADAPTER.notifyDataSetChanged();
		} catch (Exception EX) {
			EX.printStackTrace();
		}
	}
}
