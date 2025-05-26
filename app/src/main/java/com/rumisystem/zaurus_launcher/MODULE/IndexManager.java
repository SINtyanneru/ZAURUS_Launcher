package com.rumisystem.zaurus_launcher.MODULE;

import static com.rumisystem.zaurus_launcher.Activity.MainActivity.APP_DIR;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rumisystem.zaurus_launcher.TYPE.AppData;
import com.rumisystem.zaurus_launcher.TYPE.IndexData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

public class IndexManager {
	private static List<IndexData> IndexList = new ArrayList<>();

	public static void Init(PackageManager PKM, Context CONTEXT) throws IOException, PackageManager.NameNotFoundException {
		IndexList = new ArrayList<>();

		File INDEX_FILE = new File(APP_DIR, "INDEX.json");
		if (INDEX_FILE.exists()) {
			//インデックスファイルを読み込む
			JsonNode LOAD_DATA = new ObjectMapper().readTree(INDEX_FILE);

			System.out.println("インデックスファイルロードおｋ");
			for (int I = 0;I < LOAD_DATA.size(); I++) {
				//追加
				IndexList.add(new IndexData(LOAD_DATA.get(I)));
			}

			IndexList.add(new IndexData("MORE", "MOREインデックス", true, new ArrayList<>()));
		} else {
			//インデックスファイルを新規作成
			INDEX_FILE.createNewFile();
			NEW("ホームインデックス-1");
			NEW("ホームインデックス-2");
			NEW("ソーシャルメディア");
			NEW("システム設定");
		}
	}

	public static void NEW(String NAME) throws IOException {
		File IndexFile = new File(APP_DIR, "INDEX.json");
		JsonNode LoadData = new ObjectMapper().readTree(IndexFile);
		List<LinkedHashMap<String, Object>> SaveData = new ArrayList<>();
		for (int I = 0; I < LoadData.size(); I++) {
			SaveData.add(new IndexData(LoadData.get(I)).toHM());
		}

		//追加
		SaveData.add(new IndexData(UUID.randomUUID().toString(), NAME, false, new ArrayList<>()).toHM());

		SaveFile(SaveData);
	}

	public static void SAVE(String ID, List<String> ContentList) throws IOException {
		File IndexFile = new File(APP_DIR, "INDEX.json");
		JsonNode LoadData = new ObjectMapper().readTree(IndexFile);
		List<LinkedHashMap<String, Object>> SaveData = new ArrayList<>();
		for (int I = 0; I < LoadData.size(); I++) {
			if (LoadData.get(I).get("ID").asText().equals(ID)) {
				//編集対象
				LinkedHashMap<String, Object> D = new IndexData(LoadData.get(I)).toHM();
				D.put("CONTENTS", ContentList);
				SaveData.add(D);
			} else {
				//編集対象ではない
				SaveData.add(new IndexData(LoadData.get(I)).toHM());
			}
		}

		SaveFile(SaveData);
	}

	private static void SaveFile(List<LinkedHashMap<String, Object>> SaveData) throws IOException {
		//JSON化して書き出し
		String SAVE_DATA = new ObjectMapper().writeValueAsString(SaveData);
		BufferedWriter BW = new BufferedWriter(new FileWriter(new File(APP_DIR, "INDEX.json")));
		BW.write(SAVE_DATA);
		BW.flush();
		BW.close();
	}

	/**
	 * インデックスの一覧を名前で取得する
	 * @return
	 */
	public static List<String> GetINDEX_LIST() {
		List<String> RETURN = new ArrayList<>();
		for (IndexData ROW:IndexList) {
			RETURN.add(ROW.NAME);
		}
		return RETURN;
	}

	/**
	 * 一番最初のインデックスのIDを取得する
	 * @return
	 */
	public static String GetFaastINDEX_ID() {
		return IndexList.get(0).ID;
	}

	/**
	 * 配列の番号を指定してインデックスのIDを取得する
	 * @param I
	 * @return
	 */
	public static String I_TO_ID(int I) {
		return IndexList.get(I).ID;
	}

	/**
	 * インデックスのIDから中身を取得する
	 * @param ID
	 * @return
	 */
	public static List<AppData> GetINDEX_CONTENTS(String ID, PackageManager PKM, Context CONTEXT) throws IOException, PackageManager.NameNotFoundException {
		switch (ID) {
			case "MORE": {
				return AppGet.GET(PKM, CONTEXT);
			}

			default: {
				//取得
				File IndexFile = new File(APP_DIR, "INDEX.json");		//←Initでファイル作ってるから消失しているわけがない
				JsonNode LoadData = new ObjectMapper().readTree(IndexFile);
				for (int I = 0; I < LoadData.size(); I++) {
					JsonNode Row = LoadData.get(I);
					if (Row.get("ID").asText().equals(ID)) {
						List<AppData> CONTENTS = new ArrayList<>();
						for (int I2 = 0;I2 < Row.get("CONTENTS").size(); I2++) {
							String PKG_NAME = Row.get("CONTENTS").get(I2).asText();
							PackageInfo PKG = PKM.getPackageInfo(PKG_NAME, 0);
							ApplicationInfo APP = PKG.applicationInfo;
							CONTENTS.add(new AppData(PKG_NAME, PKM.getApplicationLabel(APP).toString(), APP.targetSdkVersion, PKM.getApplicationIcon(APP)));

							System.out.println("コンテンツ:" + APP.packageName);
						}
						return CONTENTS;
					}
				}

				return null;
			}
		}
	}
}
