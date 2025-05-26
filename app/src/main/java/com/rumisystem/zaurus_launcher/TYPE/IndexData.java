package com.rumisystem.zaurus_launcher.TYPE;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class IndexData {
	public String ID;
	public String NAME;
	public boolean LOCK;
	public List<String> CONTENTS;

	public IndexData(String ID, String NAME, boolean LOCK, List<String> CONTENTS) {
		this.ID = ID;
		this.NAME = NAME;
		this.LOCK = LOCK;
		this.CONTENTS = CONTENTS;
	}

	public IndexData(JsonNode Data) {
		this.ID = Data.get("ID").asText();
		this.NAME = Data.get("NAME").asText();
		this.LOCK = Data.get("LOCK").asBoolean();

		this.CONTENTS = new ArrayList<>();
		for (int I2 = 0;I2 < Data.get("CONTENTS").size(); I2++) {
			this.CONTENTS.add(Data.get("CONTENTS").get(I2).asText());
		}
	}

	public LinkedHashMap<String, Object> toHM() {
		LinkedHashMap<String, Object> Data = new LinkedHashMap<>();
		Data.put("ID", ID);
		Data.put("NAME", NAME);
		Data.put("LOCK", LOCK);
		Data.put("CONTENTS", CONTENTS);
		return Data;
	}
}
