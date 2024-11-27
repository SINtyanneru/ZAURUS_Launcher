package com.rumisystem.zaurus_launcher.TYPE;

import java.util.List;

public class INDEX_DATA {
	public String ID;
	public String NAME;
	public List<AppData> CONTENTS;
	public boolean LOCK;

	public INDEX_DATA(String ID,String NAME, List<AppData> CONTENTS, boolean LOCK) {
		this.ID = ID;
		this.NAME = NAME;
		this.CONTENTS = CONTENTS;
		this.LOCK = LOCK;
	}
}
