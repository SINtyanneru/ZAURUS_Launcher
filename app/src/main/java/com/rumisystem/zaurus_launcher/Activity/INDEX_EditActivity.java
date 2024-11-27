package com.rumisystem.zaurus_launcher.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.rumisystem.zaurus_launcher.MODULE.INDEX_Manager;
import com.rumisystem.zaurus_launcher.R;

public class INDEX_EditActivity extends AppCompatActivity {
	private String INDEX_ID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);

			//タクスバーを削除
			getSupportActionBar().hide();

			//アクティビティを表示
			setContentView(R.layout.index_edit);

			//編集するインデックスのIDを入れる
			Intent EDIT_INTENT = this.getIntent();
			if (EDIT_INTENT != null) {
				//もし編集するインデックスが指定されているなら、それを入れる
				INDEX_ID = EDIT_INTENT.getStringExtra("INDEX_ID");
			} else {
				//最初のインデックスのIDを入れる
				INDEX_ID = INDEX_Manager.GetFaastINDEX_ID();
			}
		} catch (Exception EX) {
			EX.printStackTrace();
		}
	}
}
