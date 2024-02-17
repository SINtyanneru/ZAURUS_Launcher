package com.rumisystem.zaurus_launcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class INDEX_DATA {
	public static List<HashMap<String, Object>> INDEX_LIST = new ArrayList<>();

	//インデックスを初期化する
	public static void INDEX_INIT(){
		//INDEX_LISTの長さが0なら初期化する、0じゃないなら既に初期化済みということだ
		if(INDEX_LIST.size() == 0){
			//ホームインデックス1（テスト）
			HashMap<String, Object> HOME_INDEX_1 = new HashMap<>();
			HOME_INDEX_1.put("ID", "HOME1");
			HOME_INDEX_1.put("NAME", "ホームインデックス-1");
			HOME_INDEX_1.put("EDIT", true);

			List<String> HOME_INDEX_1_CONTENTS = new ArrayList<>();
			HOME_INDEX_1_CONTENTS.add("com.google.android.youtube");

			HOME_INDEX_1.put("CONTENTS", HOME_INDEX_1_CONTENTS);

			INDEX_LIST.add(HOME_INDEX_1);

			//ホームインデックス2（テスト）
			HashMap<String, Object> HOME_INDEX_2 = new HashMap<>();
			HOME_INDEX_2.put("ID", "HOME2");
			HOME_INDEX_2.put("NAME", "ホームインデックス-2");
			HOME_INDEX_2.put("EDIT", true);

			List<String> HOME_INDEX_2_CONTENTS = new ArrayList<>();
			HOME_INDEX_2_CONTENTS.add("com.android.chrome");

			HOME_INDEX_2.put("CONTENTS", HOME_INDEX_2_CONTENTS);

			INDEX_LIST.add(HOME_INDEX_2);
		}
	}
}
