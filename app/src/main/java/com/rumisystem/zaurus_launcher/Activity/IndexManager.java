package com.rumisystem.zaurus_launcher.Activity;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rumisystem.zaurus_launcher.R;

public class IndexManager extends AppCompatActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//タクスバーを削除
		getSupportActionBar().hide();

		//アクティビティを表示
		setContentView(R.layout.index_manager);
	}
}
