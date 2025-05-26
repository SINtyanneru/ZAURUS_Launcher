package com.rumisystem.zaurus_launcher.TYPE;

import java.util.List;

public class IndexData {
	public String ID;
	public String NAME;
	public List<AppData> CONTENTS;
	public boolean LOCK;

	public IndexData(String ID, String NAME, List<AppData> CONTENTS, boolean LOCK) {
		this.ID = ID;
		this.NAME = NAME;
		this.CONTENTS = CONTENTS;
		this.LOCK = LOCK;
	}
}
