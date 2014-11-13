package com.example.homecontrol.model;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.homecontrol.R;

import java.util.ArrayList;

public class ZoneAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ZoneList groups;
    private String[][] children = {
            { "Arnold", "Barry", "Chuck", "David" },
            { "Ace", "Bandit", "Cha-Cha", "Deuce" },
            { "Fluffy", "Snuggles" },
            { "Goldy", "Bubbles" } };
	
	public ZoneAdapter(Context c, ZoneList l){
		this.context = c;
		this.groups = l;
	}

    public Object getChild(int groupPosition, int childPosition) {
        return children[groupPosition][childPosition];
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public int getChildrenCount(int groupPosition) {
        return children[groupPosition].length;
    }

    public TextView getGenericView() {
        // Layout parameters for the ExpandableListView
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 64);

        TextView textView = new TextView(context);
        textView.setLayoutParams(lp);
        // Center the text vertically
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        // Set the text starting position
        textView.setPadding(36, 0, 0, 0);
        return textView;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        TextView textView = getGenericView();
        textView.setText(getChild(groupPosition, childPosition).toString());
        return textView;
    }

    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    public int getGroupCount() {
        return groups.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {
        /*TextView textView = getGenericView();
        textView.setText(getGroup(groupPosition).toString());
        return textView;*/
        ViewHolder holder;

        if (convertView == null){
            // inflate layout
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.zone_item_list, parent, false);

            // set up viewholder
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.subTitle = (TextView) convertView.findViewById(R.id.tvSubTitle);
            holder.icon = (ImageView) convertView.findViewById(R.id.ivIcon);

            // store holder with the view
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ArrayList<Component> comps = groups.get(groupPosition).getComponents();
        String ips = "";
        if (comps != null){
            for (Component c : comps){
                ips += c.getIP() + ", ";
            }
        }
        holder.title.setText(groups.get(groupPosition).getName());
        //holder.subTitle.setText("" + list.get(position).getImgResId() + "");
        holder.subTitle.setText(ips);
        holder.icon.setImageResource(groups.get(groupPosition).getImgResId());
        return convertView;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean hasStableIds() {
        return true;
    }
	
	private class ViewHolder {
		TextView title;
		TextView subTitle;
		ImageView icon;
		
	}

}
