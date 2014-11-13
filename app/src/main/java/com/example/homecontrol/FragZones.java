package com.example.homecontrol;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homecontrol.db.ZonesDataSource;
import com.example.homecontrol.model.Component;
import com.example.homecontrol.model.Zone;
import com.example.homecontrol.model.ZoneAdapter;
import com.example.homecontrol.model.ZoneList;

public class FragZones extends Fragment {
	private static final String LOGTAG = "HOMECONTROL";
	
	public static final String SAVED_ZONE_LIST = "homecontrol.zones";
	public static final int REQ_NEW_ZONE = 3;
    public static final int REQ_NEW_COMP = 1;
    public static final int REQ_MOD_ZONE = 2;
    private int lastExpandedPosition = -1;
	
	ExpandableListView lvZones;
	ImageButton ibtnAddZone, ibtnRemoveZone;
	ZoneList listZones;  // list of all current zones
	ZoneAdapter zoneAdapter;  // adapter for lvZones to display icons and text
	
	ZonesDataSource zoneSource;
	
	/*
	 * FOR TESTING - REMOVE BEFORE FINAL IMPLEMENTATION
	 */
	Button btnMoveSd, btnDeleteDB;
	/*
	 * END OF TESTING
	 */
	
	/* variable for deleting entries from listZones */
	Zone selZone = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedState) {

		View view = inflater.inflate(R.layout.frag_zones, container, false);
		
		ibtnAddZone = (ImageButton) view.findViewById(R.id.ibtnAdd);
		ibtnRemoveZone = (ImageButton) view.findViewById(R.id.ibtnRemove);
		lvZones = (ExpandableListView) view.findViewById(R.id.lvZones);
		listZones = new ZoneList();


		zoneSource = new ZonesDataSource(getActivity());
		
		/*
		 * FOR TESTING - REMOVE BEFORE FINAL IMPLEMENTATION
		 */
		btnMoveSd = (Button) view.findViewById(R.id.btnMoveSD);
		btnDeleteDB = (Button) view.findViewById(R.id.btnDeleteDB);
		btnMoveSd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					moveDBtoSD();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		btnDeleteDB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				deleteDB();
			}
		});
		/*
		 * END OF TESTING
		 */
		
		// set ExpandableListView adapter
		zoneAdapter = new ZoneAdapter(getActivity(), listZones);
		lvZones.setAdapter(zoneAdapter);

        //register ExpandableListView for ContextMenu
        registerForContextMenu(lvZones);
				
		// restore data from last state
		if ( savedState != null){
			ZoneList tempList = new ZoneList((ZoneList) savedState.getParcelable("zones"));
			listZones.clear();
			zoneAdapter.notifyDataSetChanged();
            for (Zone z : tempList){
                listZones.add(z);
                zoneAdapter.notifyDataSetChanged();
            }

		} else {
			populateFromDB();
		}

        lvZones.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    lvZones.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });
		
		/*
		 *  On click listeners
		lvZones.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				// for now this variable is used for removeZone() only
				//selZone = listZones.get(position);
                selZone = listZones.get(position);

                String[] options = {"Add Component", "Edit Zone", "Remove Zone"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                        .setTitle("Options for \"" + selZone.getName() + "\"")
                        .setItems(options, new AlertDialog.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i){
                                    case 0: // add component
                                        addComponent(selZone.getName());
                                        break;
                                    case 1: // edit zone
                                        addZone(REQ_MOD_ZONE, selZone.getName());
                                        break;
                                    case 2: // remove zone
                                        removeZone();
                                        break;
                                }

                            }
                        })
                        .setNegativeButton("Cancel", null);

                AlertDialog dialog = builder.create();
                dialog.show();
			}
		});*/

		
		ibtnAddZone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addZone(REQ_NEW_ZONE, "");
			}
		});
		
		ibtnRemoveZone.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				removeZone();
			}
		});
		return view;
	}

    //Create ContextMenu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        ExpandableListView.ExpandableListContextMenuInfo info =
                (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;

        int type =
                ExpandableListView.getPackedPositionType(info.packedPosition);

        int group =
                ExpandableListView.getPackedPositionGroup(info.packedPosition);

        int child =
                ExpandableListView.getPackedPositionChild(info.packedPosition);

        // Only create a context menu for group items
        if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP)
        {
            selZone = listZones.get(group);
            menu.setHeaderTitle("Options for \"" + selZone.getName() + "");
            menu.add(0, 0, 0, "Add Component");
            menu.add(0, 1, 0, "Edit Zone");
            menu.add(0, 2, 0, "Remove Zone");
        } else if(type == ExpandableListView.PACKED_POSITION_TYPE_CHILD){
            menu.setHeaderTitle("Sample menu");
            menu.add(0, 0, 0, "Sample Action");
        }
    }

    //ContextMenu Item Selection
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) item.getMenuInfo();

        //String title = ((TextView) info.targetView).getText().toString();

        int type = ExpandableListView.getPackedPositionType(info.packedPosition);
        if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);
            int childPos = ExpandableListView.getPackedPositionChild(info.packedPosition);
            //Toast.makeText(getActivity(), title + ": Child " + childPos + " clicked in group " + groupPos, Toast.LENGTH_SHORT).show();
            return true;
        } else if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
            int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);
            //Toast.makeText(getActivity(), title + ": Group " + groupPos + " clicked", Toast.LENGTH_SHORT).show();
            selZone = listZones.get(groupPos);
            switch (item.getItemId())
            {

                case 0: // add component
                    addComponent(selZone.getName());
                    return true;

                case 1: // edit zone
                    addZone(REQ_MOD_ZONE, selZone.getName());
                    return true;

                case 2: // remove zone
                    removeZone();
                    return true;

                default:
                    return super.onContextItemSelected(item);
            }
        }

        return false;
    }

    public Zone getZoneByName(String zone){
        for (Zone z : listZones){
            if (z.getName().equals(zone))
                return z;
        }
        return null;
    }

    @Override
	public void onPause() {
		super.onPause();
		Log.i(LOGTAG, "(FragZones) onPause() - database closed.");
		zoneSource.close();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {

        outState.putParcelable("zones", listZones);
	}
	
	/**
	 * Receive the new Zone name and image resource id from a
	 * custom dialog.
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode != Activity.RESULT_OK) return;
        String newZoneName = "";
        String oldZoneName = "";
        int imgResId = -1;

        zoneSource.open();

        switch (requestCode) {

            case REQ_NEW_ZONE:
                newZoneName = data.getSerializableExtra(AddZoneDialog.EXTRA_ZONE_NAME).toString();
                //int imgResId = Integer.parseInt((String) data.getSerializableExtra(AddZoneDialog.EXTRA_ZONE_ICON));
                imgResId = data.getIntExtra(AddZoneDialog.EXTRA_ZONE_ICON, -1);

                Zone z = new Zone(newZoneName, imgResId);
                // add zone to database...
                zoneSource.addZone(z);
                // ... and to the list
                listZones.add(z);
                // notify adapter to populate ListView
                zoneAdapter.notifyDataSetChanged();
                break;

            case REQ_MOD_ZONE:
                oldZoneName = data.getStringExtra(AddZoneDialog.EXTRA_ZONE_OLD_NAME);
                newZoneName = data.getStringExtra(AddZoneDialog.EXTRA_ZONE_NAME);
                //int imgResId = Integer.parseInt((String) data.get)
                imgResId = data.getIntExtra(AddZoneDialog.EXTRA_ZONE_ICON, -1);

                int count = 0;
                for (Zone tempZone : listZones){
                    if (tempZone.getName().equals(oldZoneName))
                        break;
                    count++;
                }

                listZones.get(count).setName(newZoneName);
                listZones.get(count).setImgResId(imgResId);
                zoneSource.updateZone(oldZoneName, newZoneName, imgResId);

                break;

            case REQ_NEW_COMP:
                String toZone = data.getStringExtra(AddComponentDialog.EXTRA_ZONE_NAME);
                String compIP = data.getStringExtra(AddComponentDialog.EXTRA_COMP_IP);
                int compType = data.getIntExtra(AddComponentDialog.EXTRA_COMP_TYPE, -1);

                // If, for some reason, we received an invalid component type,
                // Display error and don't add the component.
                if (compType == Component.TYPE_ERR){
                    Toast.makeText(getActivity(), "Error resolving component type.\nPlease try " +
                        "to add the component again.", Toast.LENGTH_LONG).show();
                    return;
                }
                Component c = new Component(toZone, compType, compIP);

                Zone czone = null;

                // Find the zone the new component belongs to.
                for (Zone cz : listZones){
                    if (cz.getName().equals(toZone)) {
                        czone = cz;
                        break;
                    }
                }

                // Add the component to the associated zone.
                if (czone.getComponents() != null)
                    czone.getComponents().add(c);
                else{
                    ArrayList<Component> comps = new ArrayList<Component>();
                    comps.add(c);
                    czone.setComponents(comps);
                }
                zoneSource.addComponent(c);
                break;
        }
        zoneSource.close();
	}

    /**
     * Shows a pop up dialog to add a new zone to the list.  Adds the zone
     * and its associated icon to the list and database via onActivityResult.
     */
	public void addZone(int code, String zname){
		FragmentManager fm = getActivity().getSupportFragmentManager();
        AddZoneDialog dialog = new AddZoneDialog().newInstance(listZones, zname);
        dialog.setTargetFragment(FragZones.this, code);
        dialog.show(fm, "addzone");
        /*if (code == REQ_NEW_ZONE) {

        }
        if (code == REQ_MOD_ZONE) {
            AddZoneDialog dialog = new AddZoneDialog().newInstance(listZones, zname);
            dialog.setTargetFragment(FragZones.this, REQ_MOD_ZONE);
            dialog.show(fm, "modzone");
        }*/

	}

    /**
     * Shows a pop up dialog to add a new component to the selected zone.  Adds the
     * new component to its associated zone and to the database via onActivityResult.
     */
    public void addComponent(String toZone){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        AddComponentDialog dialog = new AddComponentDialog().newInstance(toZone);
        dialog.setTargetFragment(FragZones.this, REQ_NEW_COMP);
        dialog.show(fm, "addcomponent");
    }

    /**
     * Removes a zone from the list of zones as well as from
     * the database.
     */
	public void removeZone(){
        zoneSource.open();
		if (selZone != null){
			listZones.remove(selZone);
			zoneAdapter.notifyDataSetChanged();
			
			// remove from database
			zoneSource.removeZone(selZone);
			
			selZone = null;
		} else
			Toast.makeText(getActivity(), "Select zone to remove", Toast.LENGTH_SHORT).show();
        zoneSource.close();
	}


    /**
     * Populates the list of zones from the Database.
     */
	public void populateFromDB(){
        zoneSource.open();
		// make sure listZones is clear before populating
		listZones.clear();
		
		/* get the list of zones from our database
		 * and add them to our display
		 */
		ZoneList tempList = new ZoneList(zoneSource.getAllZones());
		for (Zone z : tempList){
			listZones.add(z);
			zoneAdapter.notifyDataSetChanged();
		}
		zoneSource.close();
	}
	
	/*
	 * FOR TESTING - REMOVE BEFORE FINAL IMPLEMENTATION
	 */
	public void moveDBtoSD() throws IOException{
		InputStream myInput = null;
		OutputStream myOutput = null;
		Log.e("*INPUT**", "** Getting input file **");
		myInput = new FileInputStream("/data/data/com.example.homecontrol/databases/homeControlDB.db");
		Log.e("*INPUT**", "** Got input file **");
		File dir = new File("/sdcard/homecontrol/");
		//if (!dir.exists())
			dir.mkdirs();
		Log.e("*DIR**", "** Made Dir **");
		
		File newFile = new File(dir.getPath(),"backup.db");
		if (!newFile.exists())
			newFile.createNewFile(); 
		Log.e("*CHECK FILE**", "** Made File **");
		myOutput = new FileOutputStream(newFile);
		Log.e("*OUTPUT**", "** Got output file **");
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer))> 0)
			myOutput.write(buffer, 0 , length);
		Log.e("*WRITING**", "** Wrote file **");
		
		myOutput.flush();
		myOutput.close();
		myInput.close();
		
		Toast.makeText(getActivity(), "DB Moved!", Toast.LENGTH_SHORT).show();
	}
	public void deleteDB(){
		Context c = getActivity();
		zoneSource.deleteDB(c);
		listZones.clear();
		zoneAdapter.notifyDataSetChanged();
	}
	/*
	 * END OF TESTING
	 */

}
