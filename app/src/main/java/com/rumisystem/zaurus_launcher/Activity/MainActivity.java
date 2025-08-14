package com.rumisystem.zaurus_launcher.Activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.rumisystem.zaurus_launcher.MODULE.AppGridAdapter;
import com.rumisystem.zaurus_launcher.MODULE.IndexManager;
import com.rumisystem.zaurus_launcher.MODULE.Loading;
import com.rumisystem.zaurus_launcher.TYPE.AppData;
import com.rumisystem.zaurus_launcher.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity {
	private Context CONTEXT;
	private List<AppData> APP_LIST = new ArrayList<>();
	private PackageManager PKM;
	private String INDEX_ID;
	public static String APP_DIR;
	public static String CACHE_DIR;
	private final BroadcastReceiver Receive = new BroadcastReceiver() {
		@Override
		public void onReceive(Context CTX, Intent IT) {
			String Action = IT.getAction();
			switch (Action) {
				case Intent.ACTION_POWER_CONNECTED: {
					ImageView IV = findViewById(R.id.power_source_img);
					IV.setImageDrawable(getDrawable(R.drawable.power_ac));
					return;
				}

				case Intent.ACTION_POWER_DISCONNECTED: {
					ImageView IV = findViewById(R.id.power_source_img);
					IV.setImageDrawable(getDrawable(R.drawable.power_battery));
					return;
				}
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();

		IntentFilter IF = new IntentFilter();
		IF.addAction(Intent.ACTION_POWER_CONNECTED);
		IF.addAction(Intent.ACTION_POWER_DISCONNECTED);
		registerReceiver(Receive, IF);
	}

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
				IndexManager.GetINDEX_LIST()
			));

			//選択時のイベント
			INDEX_DROPDOWN.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> ADAPTER_VIEW, View VIEW, int I, long L) {
					INDEX_ID = IndexManager.I_TO_ID(I);
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
			INDEX_EDIT_BTN.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View view) {
					Intent INTENT = new Intent(CONTEXT, com.rumisystem.zaurus_launcher.Activity.IndexManager.class);
					INTENT.putExtra("INDEX_ID", INDEX_ID);
					startActivity(INTENT);
					finish();

					return true;
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
		try {
			GridView GRID_VIEW = findViewById(R.id.AppList);

			//ロック済みなら編集できなくする
			if (IndexManager.isLock(INDEX_ID)) {
				Button EditButton = findViewById(R.id.index_edit_button);
				EditButton.setEnabled(false);
			} else {
				Button EditButton = findViewById(R.id.index_edit_button);
				EditButton.setEnabled(true);
			}

			//インデックスの中身を読み込んで変数に入れる
			IndexManager.GetINDEX_CONTENTS(INDEX_ID, PKM, CONTEXT, new Consumer<List<AppData>>() {
				@Override
				public void accept(List<AppData> app_data) {
					APP_LIST = app_data;

					//アプリ一覧を表示
					AppGridAdapter ADAPTER = new AppGridAdapter(CONTEXT, APP_LIST, PKM);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							GRID_VIEW.setAdapter(ADAPTER);
						}
					});
				}
			});
		} catch (Exception EX) {
			EX.printStackTrace();
		}
	}

	private void Init() throws PackageManager.NameNotFoundException, IOException {
		IndexManager.Init(PKM, CONTEXT);
		System.out.println("初期化おｋ");

		//インデックスを開く
		INDEX_ID = IndexManager.GetFaastINDEX_ID();
		LOAD_INDEX();
	}
}
