package com.example.homecontrol.model;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.homecontrol.R;

public class HomeZoneIconAdapter extends BaseAdapter {
	
	Context context;
	ZoneList zones;
	
	public HomeZoneIconAdapter (Context c, ZoneList z){
		this.context = c;
		this.zones = z;
		
	}

	@Override
	public int getCount() {
		return zones.size();
	}

	@Override
	public Object getItem(int index) {
		return zones.get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView == null){
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			convertView = inflater.inflate(R.layout.home_zone_list, parent, false);
			
			holder = new ViewHolder();
			holder.cb = (CheckBox) convertView.findViewById(R.id.cbZone);
			holder.tv = (TextView) convertView.findViewById(R.id.tvZoneName);
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Zone z = zones.get(position);
		Resources res = ((Activity) context).getResources();
		holder.cb.setBackgroundDrawable(res.getDrawable(z.getImgResId()));
		holder.tv.setText(z.getName());
		
		return convertView;
	}
	
	private class ViewHolder{
		CheckBox cb;
		TextView tv;
	}

}
