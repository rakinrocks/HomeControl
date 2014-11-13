package com.example.homecontrol;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homecontrol.db.ZonesDataSource;
import com.example.homecontrol.model.HomeZoneIconAdapter;
import com.example.homecontrol.model.Zone;
import com.example.homecontrol.model.ZoneList;

// This is to fuck with you
public class FragHome extends Fragment {
	private final static String LOGTAG = "HOMECONTROL";

	ZoneList listZones;
	TextView tvNumZones;
	GridView gvZones;

    SeekBar sbRed, sbGreen, sbBlue, sbMaster;

	HomeZoneIconAdapter iconAdapter;
	
	ZonesDataSource zoneSource;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedState) {
		
		View view = inflater.inflate(R.layout.test_layout, container, false);

		// get zone database
		zoneSource = new ZonesDataSource(getActivity());

        initLayout(view);

		if (savedState != null){
            int r = savedState.getInt("rlevel");
            int g = savedState.getInt("glevel");
            int b = savedState.getInt("blevel");
            int m = savedState.getInt("mlevel");

            Toast.makeText(getActivity(), "SAVED STATE " + r + ", " + g + ", " + b + ", " + m , Toast.LENGTH_SHORT).show();

            sbRed.setProgress(r);
            sbGreen.setProgress(g);
            sbBlue.setProgress(b);
            sbMaster.setProgress(m);
		}

		listZones = new ZoneList();
	
		iconAdapter = new HomeZoneIconAdapter(getActivity(), listZones);
		gvZones.setAdapter(iconAdapter);

		// get all zones from database
		populateZonesFromDB();

		// get dimensions of the grid view pictures from resource file
		// and set the column width accordingly
		Resources res = getActivity().getResources();
		int picSize = (int) res.getDimension(R.dimen.gridstd_imageview_size);
		gvZones.setColumnWidth(picSize);
		
		return view;
	}

    public void initLayout(View view){
        tvNumZones = (TextView) view.findViewById(R.id.tvZoneStatus);
        gvZones = (GridView) view.findViewById(R.id.gvZones);

        sbRed = (SeekBar) view.findViewById(R.id.sbRed);
        sbGreen = (SeekBar) view.findViewById(R.id.sbGreen);
        sbBlue = (SeekBar) view.findViewById(R.id.sbBlue);
        sbMaster = (SeekBar) view.findViewById(R.id.sbBrightness);
    }
		
	public int populateZonesFromDB(){
        zoneSource.open();
		if (listZones == null)
			listZones = new ZoneList();
	
		listZones.clear();
		ZoneList templist = new ZoneList(zoneSource.getAllZones());

		for (Zone z: templist){
			listZones.add(z);
			iconAdapter.notifyDataSetChanged();
		}
		
		if (listZones.size() == 1)
			tvNumZones.setText("You have 1 zone");
		else if (listZones.size() > 0)
			tvNumZones.setText("You have " + listZones.size() + " zones.");
		else
			tvNumZones.setText("You currently have no active zones.  Go to the \"Zones\" tab to add zones.");
		
		zoneSource.close();
		return listZones.size();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	    outState.putInt("rlevel", sbRed.getProgress());
        outState.putInt("glevel", sbGreen.getProgress());
        outState.putInt("blevel", sbBlue.getProgress());
        outState.putInt("mlevel", sbMaster.getProgress());
	}

}
