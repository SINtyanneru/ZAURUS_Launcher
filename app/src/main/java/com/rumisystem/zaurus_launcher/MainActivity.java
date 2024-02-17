package com.rumisystem.zaurus_launcher;

import static com.rumisystem.zaurus_launcher.INDEX_DATA.INDEX_INIT;
import static com.rumisystem.zaurus_launcher.INDEX_DATA.INDEX_LIST;
import static com.rumisystem.zaurus_launcher.LIB.LOAD_INDEX;
import static com.rumisystem.zaurus_launcher.LIB.MESSAGE_BOX_SHOW;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.system.Os;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
	private GridView GRID_VIEW;
	private ArrayList<ApplicationInfo> APP_LIST;
	private PackageManager PACKAGE_MANAGER;
	private static Context appContext;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);

			//タクスバーを削除
			getSupportActionBar().hide();

			setContentView(R.layout.activity_main);

			//コンテキストに自分を入れる
			appContext = this;

			//スマホの名前をセットするやつ
			TextView PHONE_NAME = findViewById(R.id.PHONE_NAME);
			PHONE_NAME.setText(Build.BRAND.toUpperCase());

			INDEX_INIT();
			SELECT_INDEX_DROPMENU_INIT();

			//パッケージマネジャーを生成
			PACKAGE_MANAGER = this.getPackageManager();

			//アプリ一覧を入れる配列を初期化
			APP_LIST = new ArrayList<>();

			//リストビューを追加
			GRID_VIEW = findViewById(R.id.APP_LIST);

			GRID_SELECT_EVENT();

			Button EDIT_BUTTON = findViewById(R.id.EDIT_BUTTON);
			EDIT_BUTTON.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View V) {
					try{
						Intent INTENT = new Intent(MainActivity.this, INDEX_EDIT_Activity.class);
						startActivity(INTENT);
					} catch (Exception EX){
						MESSAGE_BOX_SHOW(appContext, "エラー", EX.getMessage());
					}
				}
			});
		} catch (Exception EX) {
			EX.printStackTrace();
			MESSAGE_BOX_SHOW(appContext, "エラー", EX.getMessage());
		}
	}


	//インデックスを選ぶドロップメニューを初期化
	private void SELECT_INDEX_DROPMENU_INIT(){
		Spinner SELECT_INDEX_DROPMENU = (Spinner) findViewById(R.id.SELECT_INDEX_DROPMENU);

		List<String> ITEMS = new ArrayList<>();

		for(HashMap<String, Object> ROW:INDEX_LIST){
			ITEMS.add(ROW.get("NAME").toString());
		}

		ArrayAdapter<String> ADAPTER = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ITEMS);

		SELECT_INDEX_DROPMENU.setAdapter(ADAPTER);

		SELECT_INDEX_DROPMENU.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> PARENT, View VIEW, int POS, long ID) {
				if(INDEX_LIST.get(POS) != null){
					LOAD_INDEX(appContext, APP_LIST, PACKAGE_MANAGER, GRID_VIEW, INDEX_LIST.get(POS).get("ID").toString());
				} else {
					MESSAGE_BOX_SHOW(appContext, "エラー", "存在しないインデックスは選択されました");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> PARENT) {

			}
		});
	}

	//アプリ一覧のアイコンがタップされたときの処理を設定
	private void GRID_SELECT_EVENT(){
		GRID_VIEW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> PARENT, View VIEW, int POS, long ID) {
				String SELECT_APP_PACKAGE_NAME = APP_LIST.get(POS).packageName.toString();

				try {
					//パッケージがあるか(無ければエラーになる)
					PACKAGE_MANAGER.getPackageInfo(SELECT_APP_PACKAGE_NAME, PackageManager.GET_ACTIVITIES);

					Intent launchIntent = getPackageManager().getLaunchIntentForPackage(SELECT_APP_PACKAGE_NAME);
					//実行可能なインテントがあるか
					if (launchIntent != null) {
						//あるので実行する
						startActivity(launchIntent);
					} else {
						MESSAGE_BOX_SHOW(appContext, "エラー", "実行可能なインテントがありません");
					}
				} catch (PackageManager.NameNotFoundException e) {
					//パッケージがない
					MESSAGE_BOX_SHOW(appContext, "エラー", "パッケージが存在しません");
				} catch (Exception EX){
					MESSAGE_BOX_SHOW(appContext, "エラー", EX.getMessage());
				}
			}
		});
	}
}
