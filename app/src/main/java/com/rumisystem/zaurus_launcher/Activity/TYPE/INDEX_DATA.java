package com.rumisystem.zaurus_launcher.Activity.TYPE;

import java.util.List;

public class INDEX_DATA {
	public String ID;
	public String NAME;
	public List<AppData> CONTENTS;

	public INDEX_DATA(String ID,String NAME, List<AppData> CONTENTS) {
		this.ID = ID;
		this.NAME = NAME;
		this.CONTENTS = CONTENTS;
	}
}
