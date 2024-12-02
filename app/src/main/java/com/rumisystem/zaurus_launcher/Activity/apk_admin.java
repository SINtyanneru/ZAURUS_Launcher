package com.rumisystem.zaurus_launcher.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.rumisystem.zaurus_launcher.MODULE.APP_GET;
import com.rumisystem.zaurus_launcher.MODULE.AppGridAdapter;
import com.rumisystem.zaurus_launcher.R;
import com.rumisystem.zaurus_launcher.TYPE.AppData;

import java.util.ArrayList;
import java.util.List;

public class apk_admin extends AppCompatActivity {
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
			APP_LIST = APP_GET.GET(this.getPackageManager(), CONTEXT);

			REFLESH();

			GridView ALL_LIST_VIEW = findViewById(R.id.ALL_AppList);
			ALL_LIST_VIEW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> ADAPTER_VIEW, View VIEW, int I, long L) {
					if (APP_LIST.get(I) != null) {
						SELECT = I;
					}
				}
			});

			Button REMOVE_BTN = findViewById(R.id.remove_btn);
			REMOVE_BTN.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (APP_LIST.get(SELECT) != null) {
						Intent LANCH_INTENT = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
						LANCH_INTENT.setData(Uri.parse("package:" + APP_LIST.get(SELECT).GetPACKAGE_NAME()));
						startActivity(LANCH_INTENT);

						APP_LIST = APP_GET.GET(CONTEXT.getPackageManager(), CONTEXT);
					}
				}
			});
		} catch (Exception EX) {
			EX.printStackTrace();
		}
	}

	private void REFLESH() {
		List<String> ALLAPP_TEXT = new ArrayList<>();
		for (AppData ROW:APP_LIST) {
			ALLAPP_TEXT.add(ROW.GetNAME());
		}

		GridView ALL_LIST_VIEW = findViewById(R.id.ALL_AppList);
		ALL_LIST_VIEW.setAdapter(new ArrayAdapter<>(CONTEXT, android.R.layout.simple_list_item_1, ALLAPP_TEXT));
	}
}
