package com.example.homecontrol.model;

import com.example.homecontrol.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ZoneIconAdapter extends BaseAdapter {
	
	private static final String LOGTAG = "HOMECONTROL";

	Context context;
	TypedArray imgs;
	
	public ZoneIconAdapter(Context c, TypedArray a){
		this.context = c;
		imgs = a;
	}
	
	@Override
	public int getCount() {
		return imgs.length();
	}

	@Override
	public Object getItem(int index) {
		return index;
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if(convertView == null){

			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			convertView = inflater.inflate(R.layout.zone_icon_list, parent, false);
			
			holder = new ViewHolder();
			holder.iv = (ImageView) convertView.findViewById(R.id.ivIcon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.iv.setImageResource(imgs.getResourceId(position, -1));
		
		return convertView;
	}
	
	private class ViewHolder {
		ImageView iv;
	}

}
