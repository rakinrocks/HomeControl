package com.example.homecontrol;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity{
    public static final String LOGTAG = "HOME CONTROL";

	ActionBar.Tab tabHome, tabZones, tabSettings;
	Fragment fragHome = new FragHome();
	Fragment fragZones = new FragZones();
	Fragment fragSettings = new FragSettings();
	
	ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		tabHome = actionBar.newTab().setText("Home").setTag("Home");
		tabHome.setIcon(R.drawable.tab_home);
		tabZones = actionBar.newTab().setText("Zones").setTag("Zones");
		tabZones.setIcon(R.drawable.tab_zones);
		tabSettings = actionBar.newTab().setText("Settings").setTag("Settings");
		tabSettings.setIcon(R.drawable.tab_settings);
		
		
		tabHome.setTabListener(new MainTabListener(fragHome));
		tabZones.setTabListener(new MainTabListener(fragZones));
		tabSettings.setTabListener(new MainTabListener(fragSettings));
		
		actionBar.addTab(tabHome);
		actionBar.addTab(tabZones);
		actionBar.addTab(tabSettings);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/*  Listener class for tabs */
	
	public class MainTabListener implements ActionBar.TabListener {

		Fragment frag;
		
		
		public MainTabListener(Fragment fragment){
			this.frag = fragment;
		}
		
	
		@Override
		public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction xaction = fm.beginTransaction();
			xaction.replace(R.id.frag_container, frag);//.addToBackStack(null);
			xaction.commit();
		}

		@Override
		public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction xaction = fm.beginTransaction();
			xaction.remove(frag).commit();
						
		}

	}

	
	/*
	 * Save the current tab we are on and restore it when needed 
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		int index = actionBar.getSelectedNavigationIndex();
		outState.putInt("currentTab", index);	
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		int selected = savedInstanceState.getInt("currentTab");
		actionBar.setSelectedNavigationItem(selected);
		
	}
	
	@Override
	public void onBackPressed(){
		/*
		 * Don't allow normal back button operation. Back button will be used to exit
		 * the app.
		 */
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Exit Home Control?");
		alertDialogBuilder
			.setCancelable(false)
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int id) {
					MainActivity.this.finish();
				}
			})
			.setNegativeButton("No", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
		
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}



}
