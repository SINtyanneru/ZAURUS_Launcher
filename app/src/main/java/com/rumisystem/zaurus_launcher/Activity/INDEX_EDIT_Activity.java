package com.rumisystem.zaurus_launcher.Activity;

import static com.rumisystem.zaurus_launcher.INDEX_DATA.INDEX_LIST;
import static com.rumisystem.zaurus_launcher.INDEX_DATA.SAVE_INDEX_FILE;
import static com.rumisystem.zaurus_launcher.LIB.APPDATA_TO_APINFO;
import static com.rumisystem.zaurus_launcher.LIB.GET_ALL_APP;
import static com.rumisystem.zaurus_launcher.LIB.LOAD_INDEX;
import static com.rumisystem.zaurus_launcher.LIB.MESSAGE_BOX_SHOW;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rumisystem.zaurus_launcher.GATA.APPData.APPData;
import com.rumisystem.zaurus_launcher.GATA.AppIconAdapter;
import com.rumisystem.zaurus_launcher.INDEX_DATA;
import com.rumisystem.zaurus_launcher.R;

import java.util.ArrayList;
import java.util.List;

public class INDEX_EDIT_Activity extends AppCompatActivity {
	private APPData SELECT_ALL_APP;
	private APPData SELECT_INDEX_APP;
	private PackageManager PACKAGE_MANAGER;
	private GridView ALL_APP_LIST_GRIDVIEW;
	private List<APPData> ALL_APP_LIST_LIST;
	private GridView INDEX_APP_LIST_GRIDVIEW;
	private List<APPData> INDEX_APP_LIST_LIST;

	public static int INDEX_ID = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);

			//タクスバーを削除
			getSupportActionBar().hide();
			//表示する
			setContentView(R.layout.index_edit);

			//パッケージマネジャーを生成
			PACKAGE_MANAGER = this.getPackageManager();

			//アプリ一覧を入れる配列を初期化
			ALL_APP_LIST_LIST = new ArrayList<>();
			INDEX_APP_LIST_LIST = new ArrayList<>();

			//リストビューを追加
			ALL_APP_LIST_GRIDVIEW = findViewById(R.id.ALL_APP_LIST);
			INDEX_APP_LIST_GRIDVIEW = findViewById(R.id.INDEX_APP_LIST);

			//インデックス名を入れる
			TextView INDEX_NAME = findViewById(R.id.INDEX_NAME);
			INDEX_NAME.setText(INDEX_DATA.INDEX_LIST.get(INDEX_ID).get("NAME").toString());

			//全アプリ一覧のアイテムがタップされたときの処理を設定
			ALL_APP_LIST_GRIDVIEW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> PARENT, View VIEW, int POS, long ID) {
					APPData SELECT_APP_PACKAGE = ALL_APP_LIST_LIST.get(POS);

					//選択する
					SELECT_ALL_APP = SELECT_APP_PACKAGE;

					System.out.println(SELECT_APP_PACKAGE.PACKAGE_NAME);

					//選択したことをトースターでお知らせ
					try {
						if (!SELECT_APP_PACKAGE.PACKAGE_NAME.startsWith("rumi_zaurus")) {
							Toast.makeText(INDEX_EDIT_Activity.this,  "「" + PACKAGE_MANAGER.getApplicationLabel(APPDATA_TO_APINFO(SELECT_APP_PACKAGE, INDEX_EDIT_Activity.this)).toString() + "」が選択されました", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(INDEX_EDIT_Activity.this,  "「" + SELECT_APP_PACKAGE.NAME + "」が選択されました", Toast.LENGTH_SHORT).show();
						}
					} catch (PackageManager.NameNotFoundException EX) {
						EX.printStackTrace();
						MESSAGE_BOX_SHOW(INDEX_EDIT_Activity.this, "エラー", EX.getMessage());
					}
				}
			});

			//インデックス内のアプリ一覧のアイテムがタップされたときの処理を設定
			INDEX_APP_LIST_GRIDVIEW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> PARENT, View VIEW, int POS, long ID) {
					APPData SELECT_APP_PACKAGE = INDEX_APP_LIST_LIST.get(POS);

					//選択する
					SELECT_INDEX_APP = SELECT_APP_PACKAGE;

					//選択したことをトースターでお知らせ
					try {
						Toast.makeText(INDEX_EDIT_Activity.this,  "「" + PACKAGE_MANAGER.getApplicationLabel(APPDATA_TO_APINFO(SELECT_APP_PACKAGE, INDEX_EDIT_Activity.this)).toString() + "」が選択されました", Toast.LENGTH_SHORT).show();
					} catch (PackageManager.NameNotFoundException EX) {
						EX.printStackTrace();
						MESSAGE_BOX_SHOW(INDEX_EDIT_Activity.this, "エラー", EX.getMessage());
					}
				}
			});

			//追加ボタン
			Button ADD_BUTTON = findViewById(R.id.ADD_BUTTON);
			ADD_BUTTON.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(SELECT_ALL_APP != null){
						List<String> OLD_CONTENTS = (List<String>)INDEX_DATA.INDEX_LIST.get(INDEX_ID).get("CONTENTS");

						OLD_CONTENTS.add(SELECT_ALL_APP.PACKAGE_NAME);

						//リストを更新する
						INDEX_DATA.INDEX_LIST.get(INDEX_ID).put("CONTENTS", OLD_CONTENTS);

						//リストを更新
						SET_INDEX_APP();
						SET_ALL_APP();

						//選択を解除
						SELECT_ALL_APP = null;
					} else {
						Toast.makeText(INDEX_EDIT_Activity.this,  "全アプリ一覧から選択してください", Toast.LENGTH_SHORT).show();
					}
				}
			});

			//削除ボタン
			Button RM_BUTTON = findViewById(R.id.RM_BUTTON);
			RM_BUTTON.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(SELECT_INDEX_APP != null){
						List<String> OLD_CONTENTS = (List<String>)INDEX_DATA.INDEX_LIST.get(INDEX_ID).get("CONTENTS");

						OLD_CONTENTS.remove(SELECT_INDEX_APP.PACKAGE_NAME);

						//リストを更新する
						INDEX_DATA.INDEX_LIST.get(INDEX_ID).put("CONTENTS", OLD_CONTENTS);

						//リストを更新
						SET_INDEX_APP();
						SET_ALL_APP();

						//選択を解除
						SELECT_INDEX_APP = null;
					} else {
						Toast.makeText(INDEX_EDIT_Activity.this,  "インデックス内のアプリを選択してください", Toast.LENGTH_SHORT).show();
					}
				}
			});

			//適応ボタン
			Button APPLAY_BUTTON = findViewById(R.id.APPLAY_BUTTON);
			APPLAY_BUTTON.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();

					Intent INTENT = new Intent(INDEX_EDIT_Activity.this, MainActivity.class);
					startActivity(INTENT);

					SAVE_INDEX_FILE();
				}
			});

			//全てのアプリを一覧にセット
			SET_ALL_APP();
			SET_INDEX_APP();
		} catch (Exception EX) {
			EX.printStackTrace();
			MESSAGE_BOX_SHOW(this, "エラー", EX.getMessage());
		}
	}

	//端末内の全アプリを一覧にセットする関数
	private void SET_ALL_APP(){
		//リストを初期化
		ALL_APP_LIST_LIST.clear();

		for(APPData APP:GET_ALL_APP(this)){
			//ランチャー独自のアプリではないなら実行
			if (!APP.PACKAGE_NAME.startsWith("rumi_zaurus")) {
				Intent launchIntent = getPackageManager().getLaunchIntentForPackage(APP.PACKAGE_NAME);

				//実行可能なインテントがあるか、無いということはランチャーから実行できないので除外する
				if (launchIntent != null) {
					boolean NOT_DUB = false; //アプリがダブってるかどうかを登録する変数

					//インデックスの内容を全て確認して、アプリがダブってないかをチェック
					for(String PACKAGE_NAME:(List<String>)INDEX_LIST.get(INDEX_ID).get("CONTENTS")){
						if(APP.PACKAGE_NAME.equals(PACKAGE_NAME)){
							//ダブってる
							NOT_DUB = true;
							break;
						}
					}

					//ダブって無ければ追加
					if(!NOT_DUB){
						ALL_APP_LIST_LIST.add(APP);
					}
				}
			} else {
				ALL_APP_LIST_LIST.add(APP);
			}
		}

		ALL_APP_LIST_GRIDVIEW.setAdapter(new AppIconAdapter(this, ALL_APP_LIST_LIST, PACKAGE_MANAGER));
	}


	//インデックス内のアプリ一覧をセットする
	private void SET_INDEX_APP(){
		//再読込
		LOAD_INDEX(INDEX_EDIT_Activity.this, (ArrayList<APPData>) INDEX_APP_LIST_LIST, PACKAGE_MANAGER, INDEX_APP_LIST_GRIDVIEW, INDEX_LIST.get(INDEX_ID).get("ID").toString());

		INDEX_APP_LIST_GRIDVIEW.setAdapter(new AppIconAdapter(this, INDEX_APP_LIST_LIST, PACKAGE_MANAGER));
	}
}
