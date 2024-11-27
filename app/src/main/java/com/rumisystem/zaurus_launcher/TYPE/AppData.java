package com.rumisystem.zaurus_launcher.TYPE;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

public class AppData {
	private String PACKAGE_NAME;
	private String NAME;
	private int API_LEVEL;
	private Drawable IMAGE;

	public AppData(String PACKAGE_NAME, String NAME, int API_LEVEL, Drawable IMAGE) {
		this.PACKAGE_NAME = PACKAGE_NAME;
		this.NAME = NAME;
		this.API_LEVEL = API_LEVEL;
		this.IMAGE = IMAGE;
	}

	public String GetPACKAGE_NAME() {
		//a
		return PACKAGE_NAME;
	}

	public String GetNAME() {
		//a
		return NAME;
	}

	public Drawable GetIMAGE() {
		//a
		return IMAGE;
	}

	public boolean Run(Context CONTEXT, PackageManager PKM) {
		try {
			//そもそもパッケージが有るか(なければエラー落ちする)
			PKM.getApplicationInfo(PACKAGE_NAME, PackageManager.GET_ACTIVITIES);

			//起動
			Intent LANCH_INTENT = PKM.getLaunchIntentForPackage(PACKAGE_NAME);
			CONTEXT.startActivity(LANCH_INTENT);
			return true;
		} catch (Exception EX) {
			return false;
		}
	}
}
