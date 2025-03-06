package com.rumisystem.zaurus_launcher.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.rumisystem.zaurus_launcher.MODULE.AppGridAdapter;
import com.rumisystem.zaurus_launcher.MODULE.INDEX_Manager;
import com.rumisystem.zaurus_launcher.TYPE.AppData;
import com.rumisystem.zaurus_launcher.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
	private Context CONTEXT;
	private List<AppData> APP_LIST = new ArrayList<>();
	private PackageManager PKM;
	private String INDEX_ID;
	public static String APP_DIR;
	public static String CACHE_DIR;

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
			APP_DIR = CONTEXT.getExternalFilesDir(null).getPath();
			CACHE_DIR = CONTEXT.getExternalCacheDir().getPath();

			//初期化
			Init();

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
					INDEX_ID = INDEX_Manager.I_TO_ID(I);
					LOAD_INDEX();
				}

				@Override
				public void onNothingSelected(AdapterView<?> PARENT) {}
			});

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
			GRID_VIEW.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> ADAPTER_VIEW, View VIEW, int I, long L) {
					StringBuilder SB = new StringBuilder();
					SB.append("アプリ名：" + APP_LIST.get(I).GetNAME() + "\n");
					SB.append("パッケージ名：" + APP_LIST.get(I).GetPACKAGE_NAME() + "\n");

					AlertDialog.Builder DB = new AlertDialog.Builder(CONTEXT, R.style.CustomAlertDialog);
					DB.setTitle("アプリ情報");
					DB.setMessage(SB.toString());
					DB.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//そのまま閉じる
						}
					});
					DB.show();
					return true;
				}
			});

			//インデックス編集ボタンクリック時
			Button INDEX_EDIT_BTN = findViewById(R.id.index_edit_button);
			INDEX_EDIT_BTN.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent INTENT = new Intent(CONTEXT, INDEX_EditActivity.class);
					INTENT.putExtra("INDEX_ID", INDEX_ID);
					startActivity(INTENT);
					finish();
				}
			});

			//リロードボタンクリック
			Button ReloadButton = findViewById(R.id.reload_button);
			ReloadButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					try {
						Init();
					} catch (Exception EX) {
						EX.printStackTrace();
					}
				}
			});
		} catch (Exception EX) {
			EX.printStackTrace();
		}
	}

	private void LOAD_INDEX() {
		GridView GRID_VIEW = findViewById(R.id.AppList);

		//インデックスの中身を読み込んで変数に入れる
		APP_LIST = INDEX_Manager.GetINDEX_CONTENTS(INDEX_ID);

		//アプリ一覧を表示
		AppGridAdapter ADAPTER = new AppGridAdapter(this, APP_LIST, PKM);
		GRID_VIEW.setAdapter(ADAPTER);
	}

	private void Init() throws PackageManager.NameNotFoundException, IOException {
		INDEX_Manager.Init(PKM, CONTEXT);
		System.out.println("初期化おｋ");

		//インデックスを開く
		INDEX_ID = INDEX_Manager.GetFaastINDEX_ID();
		LOAD_INDEX();
	}
}
