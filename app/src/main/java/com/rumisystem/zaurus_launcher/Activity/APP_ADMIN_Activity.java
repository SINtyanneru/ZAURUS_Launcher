package com.rumisystem.zaurus_launcher.Activity;

import static com.rumisystem.zaurus_launcher.LIB.APPDATA_TO_APINFO;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rumisystem.zaurus_launcher.GATA.APPData.APPData;
import com.rumisystem.zaurus_launcher.LIB;
import com.rumisystem.zaurus_launcher.R;

import java.util.ArrayList;
import java.util.List;

public class APP_ADMIN_Activity extends AppCompatActivity {
	private ListView APP_LIST_VIEW;
	private ArrayAdapter<String> ADAPTER;
	private ArrayList<String> APP_LISTVIEW_LIST;
	private List<APPData> APP_LIST = new ArrayList<>();

	private static Context appContext;

	private APPData SELECTED_APP;

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
						try{
							String PACKAGE_NAME = getPackageManager().getApplicationLabel(APPDATA_TO_APINFO(APP_LIST.get(POS), appContext)).toString();

							SELECTED_APP = APP_LIST.get(POS);

							Toast.makeText(appContext,  "「" + PACKAGE_NAME + "」を選択しました", Toast.LENGTH_SHORT).show();
						} catch (Exception EX) {
							EX.printStackTrace();
							Toast.makeText(appContext, EX.getMessage(), Toast.LENGTH_SHORT).show();
						}
					}
				}
			});

			//一覧を初期化
			ADAPTER.clear();

			for(APPData APP: LIB.GET_ALL_APP(appContext)){
				try{
					String PACKAGE_NAME = getPackageManager().getApplicationLabel(APPDATA_TO_APINFO(APP, this)).toString();
					Intent launchIntent = getPackageManager().getLaunchIntentForPackage(APP.PACKAGE_NAME);

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
