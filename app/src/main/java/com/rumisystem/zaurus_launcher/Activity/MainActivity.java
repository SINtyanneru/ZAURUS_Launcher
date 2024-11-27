package com.rumisystem.zaurus_launcher.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.rumisystem.zaurus_launcher.Activity.MODULE.APP_GET;
import com.rumisystem.zaurus_launcher.Activity.MODULE.AppGridAdapter;
import com.rumisystem.zaurus_launcher.Activity.MODULE.INDEX_Manager;
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

			//変数初期化
			CONTEXT = this;
			PKM = this.getPackageManager();

			//初期化
			INDEX_Manager.Init(PKM);

			//インデックス選択するやつ
			Spinner INDEX_DROPDOWN = findViewById(R.id.index_dropdown);
			//内容をセット
			INDEX_DROPDOWN.setAdapter(new ArrayAdapter<>(
				this,
				android.R.layout.simple_spinner_dropdown_item,
				INDEX_Manager.GetINDEX_LIST()
			));
			//選択時のイベント
			INDEX_DROPDOWN.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> ADAPTER_VIEW, View VIEW, int I, long L) {
					String ID = INDEX_Manager.I_TO_ID(I);
					if (ID != null) {
						LOAD_INDEX(ID);
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> PARENT) {}
			});

			//インデックスを開く
			LOAD_INDEX(INDEX_Manager.GetFaastINDEX_ID());

			//タップ時のイベント
			GridView GRID_VIEW = findViewById(R.id.AppList);
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

	private void LOAD_INDEX(String ID) {
		GridView GRID_VIEW = findViewById(R.id.AppList);

		//インデックスの中身を読み込んで変数に入れる
		APP_LIST = INDEX_Manager.GetINDEX_CONTENTS(ID);

		//アプリ一覧を表示
		AppGridAdapter ADAPTER = new AppGridAdapter(this, APP_LIST, PKM);
		GRID_VIEW.setAdapter(ADAPTER);
	}
}
