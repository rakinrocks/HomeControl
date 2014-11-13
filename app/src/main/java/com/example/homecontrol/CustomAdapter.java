package com.example.homecontrol;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<String> list;
	
	public CustomAdapter(Context c, ArrayList<String> l){
		this.context = c;
		this.list = l;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView == null){
			// inflate layout
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			convertView = inflater.inflate(R.layout.zone_item_list, parent, false);
			
			// set up viewholder
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.subTitle = (TextView) convertView.findViewById(R.id.tvSubTitle);
			
			// store holder with the view
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.title.setText(list.get(position));
		holder.subTitle.setText(list.get(position));
		return convertView;
	}
	
	private class ViewHolder {
		TextView title;
		TextView subTitle;
		
	}

}
