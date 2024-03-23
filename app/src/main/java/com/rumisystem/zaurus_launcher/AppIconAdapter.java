package com.rumisystem.zaurus_launcher;

import static com.rumisystem.zaurus_launcher.Cache.GET_DRAWABLE_CACHE;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rumisystem.zaurus_launcher.R;

import java.util.List;

public class AppIconAdapter extends BaseAdapter {

	private Context CONTEXT;
	private List<ApplicationInfo> APP_INFOS;
	private PackageManager PM;

	public AppIconAdapter(Context context, List<ApplicationInfo> appInfos, PackageManager packageManager) {
		this.CONTEXT = context;
		this.APP_INFOS = appInfos;
		this.PM = packageManager;
	}

	@Override
	public int getCount() {
		return APP_INFOS.size();
	}

	@Override
	public Object getItem(int position) {
		return APP_INFOS.get(position);
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

			ApplicationInfo appInfo = APP_INFOS.get(position);
			String appName = PM.getApplicationLabel(appInfo).toString();
			Drawable icon = GET_DRAWABLE_CACHE(appInfo, PM);

			imageView.setImageDrawable(icon);
			textView.setText(appName);

			return convertView;
		}catch (Exception EX){
			EX.printStackTrace();
			return null;
		}
	}
}
