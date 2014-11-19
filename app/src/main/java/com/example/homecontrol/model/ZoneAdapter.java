package com.example.homecontrol.model;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.homecontrol.R;
import com.example.homecontrol.view.AnimatedExpandableListView.AnimatedExpandableListAdapter;

import java.util.ArrayList;

public class ZoneAdapter extends AnimatedExpandableListAdapter {

	private Context context;
	private ZoneList groups;
	
	public ZoneAdapter(Context c, ZoneList l){
		this.context = c;
		this.groups = l;
	}

    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).getModules().get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public int getRealChildrenCount(int groupPosition) {
        int size = 0;
        if (groups.get(groupPosition).getModules() != null)
            size = groups.get(groupPosition).getModules().size();
        return size;
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

    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        /*TextView textView = getGenericView();
        textView.setText(getChild(groupPosition, childPosition).toString());
        return textView;*/
        ViewHolder holder;

        if (convertView == null) {
            // inflate layout
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.module_item_list, parent, false);

            // set up viewholder
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.module_name);
            holder.status = (TextView) convertView.findViewById(R.id.module_status);
            holder.type = (TextView) convertView.findViewById(R.id.module_type);

            // store holder with the view
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(groups.get(groupPosition).getModules().get(childPosition).getName());
        int modStatus = groups.get(groupPosition).getModules().get(childPosition).getStatus();
        String status = "";
        if (modStatus == Module.MOD_ON) {
            status = this.context.getResources().getString(R.string.status_on);
        } else if (modStatus == Module.MOD_OFF) {
            status = this.context.getResources().getString(R.string.status_off);
        } else {
            status = this.context.getResources().getString(R.string.status_not_available);
        }
        holder.status.setText(status);
        holder.type.setText(groups.get(groupPosition).getModules().get(childPosition).getType());
        return convertView;
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
        TextView name;
        TextView status;
        TextView type;
        ImageView icon;

    }

}
