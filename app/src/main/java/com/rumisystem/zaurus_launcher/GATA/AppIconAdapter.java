package com.rumisystem.zaurus_launcher.GATA;

import static com.rumisystem.zaurus_launcher.Cache.GET_DRAWABLE_CACHE;
import static com.rumisystem.zaurus_launcher.LIB.APPDATA_TO_APINFO;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rumisystem.zaurus_launcher.GATA.APPData.APPData;
import com.rumisystem.zaurus_launcher.R;

import java.util.List;

public class AppIconAdapter extends BaseAdapter {

	private Context CONTEXT;
	private List<APPData> APPDATA_LIST;
	private PackageManager PM;

	public AppIconAdapter(Context context, List<APPData> APPDATA_LIST, PackageManager packageManager) {
		this.CONTEXT = context;
		this.APPDATA_LIST = APPDATA_LIST;
		this.PM = packageManager;
	}

	@Override
	public int getCount() {
		return APPDATA_LIST.size();
	}

	@Override
	public Object getItem(int position) {
		return APPDATA_LIST.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try{
			if (convertView == null) {
				convertView = LayoutInflater.from(CONTEXT).inflate(R.layout.grid_item, parent, false);
			}

			ImageView imageView = convertView.findViewById(R.id.imageView);
			TextView textView = convertView.findViewById(R.id.textView);

			APPData APPDATA = APPDATA_LIST.get(position);
			String appName = PM.getApplicationLabel(APPDATA_TO_APINFO(APPDATA, CONTEXT)).toString();
			Drawable icon = GET_DRAWABLE_CACHE(APPDATA_TO_APINFO(APPDATA, CONTEXT), PM);

			imageView.setImageDrawable(icon);
			textView.setText(appName);

			return convertView;
		}catch (Exception EX){
			EX.printStackTrace();
			return null;
		}
	}
}
