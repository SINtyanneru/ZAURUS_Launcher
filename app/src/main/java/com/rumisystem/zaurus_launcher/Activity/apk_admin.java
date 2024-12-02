package com.rumisystem.zaurus_launcher.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.rumisystem.zaurus_launcher.R;

public class apk_admin extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);

			//タクスバーを削除
			getSupportActionBar().hide();

			//アクティビティを表示
			setContentView(R.layout.apk_admin);
		} catch (Exception EX) {

		}
	}
}
