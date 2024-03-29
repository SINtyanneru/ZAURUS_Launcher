package com.rumisystem.zaurus_launcher;

import static com.rumisystem.zaurus_launcher.CONFIG.APP_DIR;
import static com.rumisystem.zaurus_launcher.LIB.MESSAGE_BOX_SHOW;
import static com.rumisystem.zaurus_launcher.MainActivity.VERSION;
import static com.rumisystem.zaurus_launcher.MainActivity.appContext;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class INDEX_DATA {
	public static List<HashMap<String, Object>> INDEX_LIST = new ArrayList<>();


	//インデックスを初期化する
	public static void INDEX_INIT(){
		//INDEX_LISTの長さが0なら初期化する、0じゃないなら既に初期化済みということだ
		if(INDEX_LIST.size() == 0){
			GEN_INDEX_FILE();
		}
	}



	public static void GEN_INDEX_FILE(){
		try{
			//ファイルを準備
			File INDEX_FILE = new File(APP_DIR, "INDEX.json");

			//ファイルが存在するか
			if(INDEX_FILE.exists()){
				//存在するので読み込む
				LOAD_INDEX_FILE();
			} else {
				//無いので初期ファイルを作る
				if(INDEX_FILE.createNewFile()){
					//初期ファイルの内容
					GEN_INDEX_DATA();

					//書き込む
					BufferedWriter WRITER = new BufferedWriter(new FileWriter(INDEX_FILE));

					//MAPをString化
					ObjectMapper OM = new ObjectMapper();
					String RES = OM.writeValueAsString(INDEX_LIST);

					//書き込み
					WRITER.write(RES);

					//メモリ開放
					WRITER.close();
				} else {
					MESSAGE_BOX_SHOW(appContext, "エラー", "ファイルを作れませんでした、\n権限で許可しましたか？");
					System.exit(1);
				}
			}
		} catch (Exception EX){
			MESSAGE_BOX_SHOW(appContext, "エラー", EX.getMessage());
		}
	}

	private static void LOAD_INDEX_FILE(){
		try {
			File INDEX_FILE = new File(APP_DIR, "INDEX.json");
			if (INDEX_FILE.exists()) {
				FileReader FR = new FileReader(INDEX_FILE);
				BufferedReader BR = new BufferedReader(FR);

				StringBuilder INDEX_CONTENTS = new StringBuilder();

				String INDEX_CONTENTS_TEMP;
				while ((INDEX_CONTENTS_TEMP = BR.readLine()) != null) {
					INDEX_CONTENTS.append(INDEX_CONTENTS_TEMP);
				}
				BR.close();

				ObjectMapper OM = new ObjectMapper();
				JsonNode INDEX_CONTENTS_JSON = OM.readTree(INDEX_CONTENTS.toString());

				//順番に取り出す
				for (int I = 0; INDEX_CONTENTS_JSON.size() > I; I++) {
					JsonNode ROW = INDEX_CONTENTS_JSON.get(I);

					//JsonNodeをHashMap化
					HashMap<String, Object> RESULT = OM.convertValue(ROW, new TypeReference<HashMap<String, Object>>(){});

					//それを追加
					INDEX_LIST.add(RESULT);
				}
			}
		} catch (Exception EX){
			MESSAGE_BOX_SHOW(appContext, "エラー", EX.getMessage());
		}
	}

	public static void SAVE_INDEX_FILE(){
		try{
			//ファイルに書き込む
			File INDEX_FILE = new File(APP_DIR, "INDEX.json");
			if(INDEX_FILE.exists()){
				//書き込む
				BufferedWriter WRITER = new BufferedWriter(new FileWriter(INDEX_FILE));

				//MAPをString化
				ObjectMapper OM = new ObjectMapper();
				String RES = OM.writeValueAsString(INDEX_LIST);

				//書き込み
				WRITER.write(RES);

				//メモリ開放
				WRITER.close();
			} else {
				MESSAGE_BOX_SHOW(appContext, "エラー", "なぜかファイルがありません、なんでだろうね");
			}
		} catch (Exception EX){
			EX.printStackTrace();
			MESSAGE_BOX_SHOW(appContext, "エラー", EX.getMessage());
		}
	}

	//インデックス内の特定のアイテムを削除する関数
	public static void DELETE_INDEX_CONTENTS(String INDEX_ID, String CONTENTS_ID) {
		try{
			int I = 0;

			for(HashMap<String, Object> ROW:INDEX_LIST){
				//指定されたIDとインデックスのIDが一致するか(一致するまで回す)
				if(ROW.get("ID").toString().equals(INDEX_ID)){
					for(String PACKAGE_NAME:(List<String>)ROW.get("CONTENTS")){

						if(PACKAGE_NAME.toString().equals(CONTENTS_ID)){
							List<String> OLD_CONTENTS = (List<String>)INDEX_LIST.get(I).get("CONTENTS");

							OLD_CONTENTS.remove(PACKAGE_NAME);

							//書き換え
							INDEX_LIST.get(I).put("CONTENTS", OLD_CONTENTS);

							//保存
							SAVE_INDEX_FILE();
							return;
						}
					}
				}

				I++;
			}
		}catch (Exception EX){
			EX.printStackTrace();
		}
	}

	//初期のインデックスファイルの内容を作る関数
	private static void GEN_INDEX_DATA(){
		HashMap<String, Object> HOME_INDEX_1 = new HashMap<>();
		HOME_INDEX_1.put("ID", "HOME1");
		HOME_INDEX_1.put("NAME", "ホームインデックス-1");
		HOME_INDEX_1.put("EDIT", true);
		HOME_INDEX_1.put("CONTENTS", new ArrayList<>());
		INDEX_LIST.add(HOME_INDEX_1);

		HashMap<String, Object> HOME_INDEX_2 = new HashMap<>();
		HOME_INDEX_2.put("ID", "HOME2");
		HOME_INDEX_2.put("NAME", "ホームインデックス-2");
		HOME_INDEX_2.put("EDIT", true);
		HOME_INDEX_2.put("CONTENTS", new ArrayList<>());
		INDEX_LIST.add(HOME_INDEX_2);

		HashMap<String, Object> GAME_INDEX = new HashMap<>();
		GAME_INDEX.put("ID", "GAME");
		GAME_INDEX.put("NAME", "ゲーム");
		GAME_INDEX.put("EDIT", true);
		GAME_INDEX.put("CONTENTS", new ArrayList<>());
		INDEX_LIST.add(GAME_INDEX);

		HashMap<String, Object> SNS_INDEX = new HashMap<>();
		SNS_INDEX.put("ID", "SNS");
		SNS_INDEX.put("NAME", "ソーシャル");
		SNS_INDEX.put("EDIT", true);
		SNS_INDEX.put("CONTENTS", new ArrayList<>());
		INDEX_LIST.add(SNS_INDEX);

		HashMap<String, Object> SYSTEM_INDEX = new HashMap<>();
		SYSTEM_INDEX.put("ID", "SYSTEM");
		SYSTEM_INDEX.put("NAME", "システム");
		SYSTEM_INDEX.put("EDIT", true);

		List<String> SYSTEM_CONTENTS = new ArrayList<>();
		SYSTEM_CONTENTS.add("com.android.settings");

		SYSTEM_INDEX.put("CONTENTS", SYSTEM_CONTENTS);
		INDEX_LIST.add(SYSTEM_INDEX);
	}
}
