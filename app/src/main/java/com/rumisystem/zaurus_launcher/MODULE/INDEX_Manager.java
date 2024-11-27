package com.rumisystem.zaurus_launcher.MODULE;

import android.content.pm.PackageManager;

import com.rumisystem.zaurus_launcher.TYPE.AppData;
import com.rumisystem.zaurus_launcher.TYPE.INDEX_DATA;

import java.util.ArrayList;
import java.util.List;

public class INDEX_Manager {
	private static List<INDEX_DATA> INDEX_LIST = new ArrayList<>();

	public static void Init(PackageManager PKM) {
		INDEX_LIST.add(new INDEX_DATA("aaa", "ホームインデックス-1", new ArrayList<>()));
		INDEX_LIST.add(new INDEX_DATA("MORE", "MOREインデックス", APP_GET.GET(PKM)));
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
