package com.rumisystem.zaurus_launcher.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.rumisystem.zaurus_launcher.R;

public class MainActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);

			//タクスバーを削除
			getSupportActionBar().hide();

			setContentView(R.layout.activity_main);
		} catch (Exception EX) {
			EX.printStackTrace();
		}
	}
}
