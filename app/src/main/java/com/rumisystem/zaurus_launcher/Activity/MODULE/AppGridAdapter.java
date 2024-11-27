package com.rumisystem.zaurus_launcher.Activity.MODULE;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rumisystem.zaurus_launcher.Activity.TYPE.AppData;
import com.rumisystem.zaurus_launcher.R;

import java.util.List;

public class AppGridAdapter extends BaseAdapter {
	private final Context CONTEXT;
	private final List<AppData> APP_LIST;
	private PackageManager PKM;

	public AppGridAdapter(Context CONTEXT, List<AppData> APP_LIST, PackageManager PKM) {
		this.CONTEXT = CONTEXT;
		this.APP_LIST = APP_LIST;
		this.PKM = PKM;
	}

	@Override
	public int getCount() {
		return APP_LIST.size();
	}

	@Override
	public Object getItem(int I) {
		return APP_LIST.get(I);
	}

	@Override
	public long getItemId(int I) {
		return I;
	}

	@Override
	public View getView(int I, View VIEW, ViewGroup VG) {
		try {
			if (VIEW == null) {
				VIEW = LayoutInflater.from(CONTEXT).inflate(R.layout.grid_item, VG, false);
			}

			AppData APP = APP_LIST.get(I);
			TextView TEXTVIEW = VIEW.findViewById(R.id.textView);
			ImageView IMAGE_VIEW = VIEW.findViewById(R.id.imageView);

			TEXTVIEW.setText(APP.GetNAME());
			IMAGE_VIEW.setImageDrawable(APP.GetIMAGE());

			return VIEW;
		} catch (Exception EX) {
			return null;

		}
	}
}
