package com.rumisystem.zaurus_launcher.MODULE;

import static com.rumisystem.zaurus_launcher.Activity.MainActivity.APP_DIR;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rumisystem.zaurus_launcher.TYPE.AppData;
import com.rumisystem.zaurus_launcher.TYPE.INDEX_DATA;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class INDEX_Manager {
	private static List<INDEX_DATA> INDEX_LIST = new ArrayList<>();

	public static void Init(PackageManager PKM, Context CONTEXT) throws IOException, PackageManager.NameNotFoundException {
		//変数初期化
		INDEX_LIST = new ArrayList<>();

		File INDEX_FILE = new File(APP_DIR, "INDEX.json");
		if (INDEX_FILE.exists()) {
			//インデックスファイルを読み込む
			JsonNode LOAD_DATA = new ObjectMapper().readTree(INDEX_FILE);
			for (int I = 0;I < LOAD_DATA.size(); I++) {
				JsonNode ROW = LOAD_DATA.get(I);

				//コンテンツをセット
				List<AppData> CONTENTS = new ArrayList<>();
				for (int I2 = 0;I2 < ROW.get("CONTENTS").size(); I2++) {
					String PKG_NAME = ROW.get("CONTENTS").get(I2).asText();
					PackageInfo PKG = PKM.getPackageInfo(PKG_NAME, 0);
					ApplicationInfo APP = PKG.applicationInfo;
					CONTENTS.add(new AppData(PKG_NAME, PKM.getApplicationLabel(APP).toString(), APP.targetSdkVersion, PKM.getApplicationIcon(APP)));
				}

				//追加
				INDEX_LIST.add(new INDEX_DATA(ROW.get("ID").asText(), ROW.get("NAME").asText(), CONTENTS, ROW.get("LOCK").asBoolean()));
			}
		} else {
			//インデックスファイルを新規作成
			INDEX_FILE.createNewFile();
			INDEX_LIST.add(new INDEX_DATA("home-1", "ホームインデックス-1", new ArrayList<>(), false));
			INDEX_LIST.add(new INDEX_DATA("home-2", "ホームインデックス-2", new ArrayList<>(), false));
			INDEX_LIST.add(new INDEX_DATA("home-2", "ソーシャルメディア", new ArrayList<>(), false));
			INDEX_LIST.add(new INDEX_DATA("system_setting", "システム設定", new ArrayList<>(), false));
			SAVE();
		}

		INDEX_LIST.add(new INDEX_DATA("MORE", "MOREインデックス", APP_GET.GET(PKM, CONTEXT), true));
	}

	public static void SAVE() {
		try {
			List<LinkedHashMap<String, Object>> DATA_LIST = new ArrayList<>();
			for (INDEX_DATA ROW:INDEX_LIST) {
				//例外なやつは保存しない
				if (!ROW.ID.equals("MORE")) {
					LinkedHashMap<String, Object> INDEX = new LinkedHashMap<>();

					//情報
					INDEX.put("ID", ROW.ID);
					INDEX.put("NAME", ROW.NAME);
					INDEX.put("LOCK", ROW.LOCK);

					//内容
					List<String> CONTENTS = new ArrayList<>();
					for (AppData APP:ROW.CONTENTS) {
						CONTENTS.add(APP.GetPACKAGE_NAME());
					}
					INDEX.put("CONTENTS", CONTENTS);

					DATA_LIST.add(INDEX);
				}
			}

			//JSON化して書き出し
			String SAVE_DATA = new ObjectMapper().writeValueAsString(DATA_LIST);
			BufferedWriter BW = new BufferedWriter(new FileWriter(new File(APP_DIR, "INDEX.json")));
			BW.write(SAVE_DATA);
			BW.flush();
			BW.close();
		} catch (Exception EX) {
			EX.printStackTrace();
		}
	}

	/**
	 * インデックスの一覧を名前で取得する
	 * @return
	 */
	public static List<String> GetINDEX_LIST() {
		List<String> RETURN = new ArrayList<>();
		for (INDEX_DATA ROW:INDEX_LIST) {
			RETURN.add(ROW.NAME);
		}
		return RETURN;
	}

	/**
	 * 一番最初のインデックスのIDを取得する
	 * @return
	 */
	public static String GetFaastINDEX_ID() {
		return INDEX_LIST.get(0).ID;
	}

	/**
	 * 配列の番号を指定してインデックスのIDを取得する
	 * @param I
	 * @return
	 */
	public static String I_TO_ID(int I) {
		return INDEX_LIST.get(I).ID;
	}

	/**
	 * インデックスのIDから中身を取得する
	 * @param ID
	 * @return
	 */
	public static List<AppData> GetINDEX_CONTENTS(String ID) {
		for (INDEX_DATA ROW:INDEX_LIST) {
			if (ROW.ID.equals(ID)) {
				return ROW.CONTENTS;
			}
		}

		return null;
	}
}
