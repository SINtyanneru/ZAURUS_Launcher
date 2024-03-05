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

	private Context context;
	private List<ApplicationInfo> appInfos;
	private PackageManager packageManager;

	public AppIconAdapter(Context context, List<ApplicationInfo> appInfos, PackageManager packageManager) {
		this.context = context;
		this.appInfos = appInfos;
		this.packageManager = packageManager;
	}

	@Override
	public int getCount() {
		return appInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return appInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try{
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
			}

			ImageView imageView = convertView.findViewById(R.id.imageView);
			TextView textView = convertView.findViewById(R.id.textView);

			ApplicationInfo appInfo = appInfos.get(position);
			String appName = packageManager.getApplicationLabel(appInfo).toString();
			Drawable icon = GET_DRAWABLE_CACHE(packageManager.getApplicationIcon(appInfo.packageName), appInfo.packageName.toString().replace(".", "_"));

			imageView.setImageDrawable(icon);
			textView.setText(appName);

			return convertView;
		}catch (Exception EX){
			EX.printStackTrace();
			return null;
		}
	}
}
