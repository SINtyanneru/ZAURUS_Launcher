package com.rumisystem.zaurus_launcher.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.rumisystem.zaurus_launcher.MODULE.AppGet;
import com.rumisystem.zaurus_launcher.MODULE.AppIconManager;
import com.rumisystem.zaurus_launcher.R;
import com.rumisystem.zaurus_launcher.TYPE.AppData;

import java.util.ArrayList;
import java.util.List;

public class apk_admin extends AppCompatActivity {
	private static final int REQUEST_CODE_UNINSTALL = 1;

	private Context CONTEXT;
	private List<AppData> APP_LIST;
	private int SELECT = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);

			//タクスバーを削除
			getSupportActionBar().hide();

			//アクティビティを表示
			setContentView(R.layout.apk_admin);

			//変数初期化
			CONTEXT = this;
			APP_LIST = AppGet.AllGet(this.getPackageManager(), CONTEXT);

			Refresh();

			GridView ALL_LIST_VIEW = findViewById(R.id.ALL_AppList);
			ALL_LIST_VIEW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> ADAPTER_VIEW, View VIEW, int I, long L) {
					try {
						//↓+1しないとズレますわよ
						if (APP_LIST.get(I+1) != null) {
							SELECT = I+1;

							PackageManager PKM = CONTEXT.getPackageManager();
							ApplicationInfo APP = PKM.getPackageInfo(APP_LIST.get(I+1).GetPACKAGE_NAME(), 0).applicationInfo;

							ImageView IV = findViewById(R.id.package_icon);
							IV.setImageBitmap(AppIconManager.Get(APP, PKM));

							TextView TV = findViewById(R.id.package_app_name);
							TV.setText(APP_LIST.get(I+1).GetNAME());
						}
					} catch (Exception EX) {
						EX.printStackTrace();
					}
				}
			});

			Button REMOVE_BTN = findViewById(R.id.remove_btn);
			REMOVE_BTN.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					try {
						if (APP_LIST.get(SELECT) != null) {
							Intent LantchIntent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
							LantchIntent.setData(Uri.parse("package:" + APP_LIST.get(SELECT).GetPACKAGE_NAME()));
							startActivityForResult(LantchIntent, REQUEST_CODE_UNINSTALL);

							APP_LIST = AppGet.AllGet(CONTEXT.getPackageManager(), CONTEXT);
						}
					} catch (Exception EX) {
						EX.printStackTrace();
					}
				}
			});
		} catch (Exception EX) {
			EX.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int RequestCode, int ResultCode, Intent Data) {
		super.onActivityResult(RequestCode, ResultCode, Data);

		if (RequestCode == REQUEST_CODE_UNINSTALL) {
			//アンインスコされた
			if (!isPKGInstalled(APP_LIST.get(SELECT).GetPACKAGE_NAME())) {
				APP_LIST.remove(SELECT);
				Refresh();
			}
		}
	}

	//パケージが有るか否か
	private boolean isPKGInstalled(String PackageName) {
		try {
			this.getPackageManager().getPackageInfo(PackageName, 0);

			return true;
		} catch (Exception EX) {
			return false;
		}
	}

	private void Refresh() {
		List<String> ALLAPP_TEXT = new ArrayList<>();
		for (AppData ROW:APP_LIST) {
			if (ROW.GetPACKAGE_NAME().startsWith("rumi_zaurus.")) {
				continue;
			}

			ALLAPP_TEXT.add(ROW.GetNAME());
		}

		GridView ALL_LIST_VIEW = findViewById(R.id.ALL_AppList);
		ALL_LIST_VIEW.setAdapter(new ArrayAdapter<>(CONTEXT, android.R.layout.simple_list_item_1, ALLAPP_TEXT));
	}
}
