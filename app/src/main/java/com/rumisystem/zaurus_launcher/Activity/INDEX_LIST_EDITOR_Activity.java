package com.rumisystem.zaurus_launcher.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.rumisystem.zaurus_launcher.INDEX_DATA;
import com.rumisystem.zaurus_launcher.R;

import java.util.ArrayList;
import java.util.HashMap;

public class INDEX_LIST_EDITOR_Activity extends AppCompatActivity {
	private ArrayList<String> ITEM_LIST = new ArrayList<>();
	private ArrayAdapter ADAPTER;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);

			//タクスバーを削除
			getSupportActionBar().hide();

			setContentView(R.layout.index_list_editor);

			//インデックスリスト
			ListView INDEX_LIST = findViewById(R.id.INDEX_LIST);

			//アダプターの初期化
			ADAPTER = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ITEM_LIST);
			INDEX_LIST.setAdapter(ADAPTER);

			//アイテムがタップされたときの処理
			INDEX_LIST.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> PARENT, View VIEW, int POS, long ID) {
					if(INDEX_DATA.INDEX_LIST.get(POS) != null){
						HashMap<String, Object> INDEX = INDEX_DATA.INDEX_LIST.get(POS);
						String INDEX_NAME = INDEX.get("NAME").toString();

						//入力欄
						AppCompatEditText NEW_NAME_INPUT = new AppCompatEditText(INDEX_LIST_EDITOR_Activity.this);
						NEW_NAME_INPUT.setText(INDEX_NAME);

						//ダイアログを表示
						new AlertDialog.Builder(INDEX_LIST_EDITOR_Activity.this)
								.setTitle(INDEX_NAME + "の名前を変更")
								.setView(NEW_NAME_INPUT)
								.setPositiveButton("OK", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										//注意：こうしないと動きません
										String NEW_NAME = NEW_NAME_INPUT.getText().toString();

										//変更を適応
										INDEX.put("NAME", NEW_NAME);

										//リストを再読込
										ALL_INDEX_ADD_LIST();
									}
								}).create().show();
					}
				}
			});

			//アイテムが長押しされたときの処理
			INDEX_LIST.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> PARENT, View VIEW, int POS, long ID) {
					return true;
				}
			});

			//変更をファイルに書き込む
			Button APPLAY_BUTTON = findViewById(R.id.APPLAY_BUTTON);
			APPLAY_BUTTON.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();

					Intent INTENT = new Intent(INDEX_LIST_EDITOR_Activity.this, MainActivity.class);
					startActivity(INTENT);

					INDEX_DATA.SAVE_INDEX_FILE();
				}
			});

			ALL_INDEX_ADD_LIST();
		} catch (Exception EX) {
			EX.printStackTrace();
		}
	}

	private void ALL_INDEX_ADD_LIST(){
		//初期化
		ADAPTER.clear();

		//インデックスをリストに全て入れる
		for(HashMap<String, Object> INDEX_DATA:INDEX_DATA.INDEX_LIST){
			ITEM_LIST_ADD_ITEM(INDEX_DATA.get("NAME").toString());
		}
	}

	private void ITEM_LIST_ADD_ITEM(String TEXT){
		ITEM_LIST.add(TEXT);
		ADAPTER.notifyDataSetChanged(); // アダプターにデータの変更を通知
	}
}
