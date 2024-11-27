package com.rumisystem.zaurus_launcher.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.rumisystem.zaurus_launcher.MODULE.APP_GET;
import com.rumisystem.zaurus_launcher.MODULE.AppGridAdapter;
import com.rumisystem.zaurus_launcher.MODULE.INDEX_Manager;
import com.rumisystem.zaurus_launcher.R;
import com.rumisystem.zaurus_launcher.TYPE.AppData;

import java.util.List;

public class INDEX_EditActivity extends AppCompatActivity {
	private Context CONTEXT;
	private String INDEX_ID;
	private List<AppData> INDEX_APP_LIST;
	private List<AppData> ALL_APP_LIST;
	private PackageManager PKM;
	private int INDEX_SELECT = 0;
	private int ALL_SELECT = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);

			//タクスバーを削除
			getSupportActionBar().hide();

			//アクティビティを表示
			setContentView(R.layout.index_edit);

			//変数初期化
			PKM = this.getPackageManager();
			CONTEXT = this;

			//編集するインデックスのIDを入れる
			Intent EDIT_INTENT = this.getIntent();
			if (EDIT_INTENT != null) {
				//もし編集するインデックスが指定されているなら、それを入れる
				INDEX_ID = EDIT_INTENT.getStringExtra("INDEX_ID");
			} else {
				//最初のインデックスのIDを入れる
				INDEX_ID = INDEX_Manager.GetFaastINDEX_ID();
			}

			//読み込み
			LOAD_INDEX();

			EVENT();
		} catch (Exception EX) {
			EX.printStackTrace();
		}
	}

	private void EVENT() {
		Button APPLY_BTN = findViewById(R.id.indexedit_setting_apply);
		APPLY_BTN.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//保存
				INDEX_Manager.SAVE();

				//メインアクティビティを開く
				Intent INTENT = new Intent(CONTEXT, MainActivity.class);
				startActivity(INTENT);
				finish();

				/*
				//再起動
				Intent INTENT = PKM.getLaunchIntentForPackage(CONTEXT.getPackageName());
				if (INTENT != null) {
					INTENT.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(INTENT);
				}

				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(0);
				*/
			}
		});

		//追加ボタン
		Button ADD_BTN = findViewById(R.id.add_btn);
		ADD_BTN.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View VIEW) {
				INDEX_APP_LIST.add(ALL_APP_LIST.get(ALL_SELECT));
				ALL_APP_LIST.remove(ALL_SELECT);

				//リフレッシュ
				VIEW_RIFLESH();
			}
		});

		//削除ボタン
		Button DELETE_BTN = findViewById(R.id.remove_btn);
		DELETE_BTN.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View VIEW) {
				ALL_APP_LIST.add(INDEX_APP_LIST.get(INDEX_SELECT));
				INDEX_APP_LIST.remove(INDEX_SELECT);

				//リフレッシュ
				VIEW_RIFLESH();
			}
		});

		//インデックス選択
		GridView INDEX_GRID_VIEW = findViewById(R.id.INDEX_AppList);
		INDEX_GRID_VIEW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> ADAPTER_VIEW, View VIEW, int I, long L) {
				//選択を変更
				INDEX_SELECT = I;
			}
		});

		//アプリ一覧択
		GridView ALL_GRID_VIEW = findViewById(R.id.ALL_AppList);
		ALL_GRID_VIEW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> ADAPTER_VIEW, View VIEW, int I, long L) {
				//選択を変更
				ALL_SELECT = I;
			}
		});
	}

	private void LOAD_INDEX() {
		//インデックスの中身を読み込んで変数に入れる
		INDEX_APP_LIST = INDEX_Manager.GetINDEX_CONTENTS(INDEX_ID);
		ALL_APP_LIST = APP_GET.GET(PKM);

		VIEW_RIFLESH();
	}

	private void VIEW_RIFLESH() {
		GridView INDEX_GRID_VIEW = findViewById(R.id.INDEX_AppList);
		GridView ALL_GRID_VIEW = findViewById(R.id.ALL_AppList);

		//インデックスアプリ一覧を表示
		INDEX_GRID_VIEW.setAdapter(new AppGridAdapter(this, INDEX_APP_LIST, PKM));

		//全アプリ一覧を表示
		ALL_GRID_VIEW.setAdapter(new AppGridAdapter(this, ALL_APP_LIST, PKM));
	}
}
