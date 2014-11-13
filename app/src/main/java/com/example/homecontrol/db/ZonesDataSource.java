package com.example.homecontrol.db;

import com.example.homecontrol.model.Component;
import com.example.homecontrol.model.Zone;
import com.example.homecontrol.model.ZoneList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class ZonesDataSource {
	private static final String LOGTAG = "HOMECONTROL";
	
	/*private static final String[] allColumns = new String{
		HomeDBOpenHelper.
	}*/
    
	SQLiteOpenHelper dbhelper;
	SQLiteDatabase database;
	
	public ZonesDataSource(Context context){
		// create database and tables if they don't exist
		dbhelper = new HomeDBOpenHelper(context);
	}
	
	public void open(){
		//open the database
		database = dbhelper.getWritableDatabase();
        Log.i(LOGTAG, "(ZonesDataSource) open() - Database opened.");
	}
	
	public void close(){
		Log.i(LOGTAG, "(ZonesDataSource) close() - Database closed.");
		dbhelper.close();
	}
	
	/*
	 * Add a new Zone to the database
	 */
	public void addZone(Zone z){
        String insertQuery = "INSERT " +
                "INTO " + HomeDBOpenHelper.TABLE_ZONES + " " +
                "VALUES (\"" + z.getName() + "\", " + z.getImgResId() + ")";
        database.execSQL(insertQuery);
        Log.d(LOGTAG, "Query: " + insertQuery);
		Log.i(LOGTAG, "Zone inserted");
	}

    public void addComponent(Component c){
        String insertQuery = "INSERT " +
                "INTO " + HomeDBOpenHelper.TABLE_COMPONENTS + " " +
                "VALUES (\"" + c.getZone() + "\", " + c.getType() + ", \"" + c.getIP() + "\")";
        Log.d(LOGTAG, "Query: " + insertQuery);
        database.execSQL(insertQuery);
    }
	
	/*
	 * Get a list of all zones and return to calling fnc
	 */
	public ZoneList getAllZones(){
		ZoneList list = new ZoneList();
		
		// get all entries from zone table
		String query = "SELECT * FROM " + HomeDBOpenHelper.TABLE_ZONES;
		Cursor cursor = database.rawQuery(query, null);
		Log.i(LOGTAG, "(ZonesDataSource) getAllZones() - getting records..");
		/* populate "list" with all the zones from the 
		 * database.  Return this to the caller
		 */
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			String name = cursor.getString(0);
			int imgResId = cursor.getInt(1);

            // Find components for associated zone.
            ArrayList<Component> comps = getComponentsForZone(name);

			Zone z = new Zone(name, imgResId, comps);
			list.add(z);
			cursor.moveToNext();
		}
		
		if ((cursor != null) && (!cursor.isClosed()))
			cursor.close();

		return list;
	}

    public void updateZone(String oldName, String newName, int imgResId){
        String query = "UPDATE " + HomeDBOpenHelper.TABLE_ZONES + " " +
                "SET " + HomeDBOpenHelper.COLUMN_ZONE_NAME + " = \"" + newName + "\", " +
                         HomeDBOpenHelper.COLUMN_RESOURCEID + " = \"" + imgResId + "\" " +
                "WHERE " + HomeDBOpenHelper.COLUMN_ZONE_NAME + " = \"" + oldName + "\"";
        Log.d(LOGTAG, "(ZonesDataSource) Query: " + query);
        database.execSQL(query);

    }

    public ArrayList<Component> getComponentsForZone(String zone){
        ArrayList<Component> comps = new ArrayList<Component>();
        String query = "SELECT " + HomeDBOpenHelper.COLUMN_COMP_TYPE + ", " +
                                   HomeDBOpenHelper.COLUMN_IP + " " +
                "FROM " + HomeDBOpenHelper.TABLE_COMPONENTS + " " +
                "WHERE " + HomeDBOpenHelper.COLUMN_COMP_ZONE_NAME + " = \"" + zone + "\"";
        Log.d(LOGTAG, "(Query) " + query);

        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            int ctype = cursor.getInt(0);
            String cip = cursor.getString(1);
            comps.add(new Component(zone, ctype, cip));
            cursor.moveToNext();
        }

        return comps;
    }
	
	public boolean removeZone(Zone z){
        String qdelete = "DELETE " +
                "FROM " + HomeDBOpenHelper.TABLE_ZONES + " " +
                "WHERE " + HomeDBOpenHelper.COLUMN_ZONE_NAME + " = \"" + z.getName() + "\"";

        database.execSQL(qdelete);

        Log.i(LOGTAG, "(ZonesDataSource) removeZone() - Zone \"" + z.getName() + "\" deleted!");
        return true;
	}
	
	
	/*
	 * USED FOR DEBUGGING ONLY!
	 * REMOVE BEFORE FINAL IMPLEMENTATION
	 */
	public void deleteDB(Context c){
		c.deleteDatabase(HomeDBOpenHelper.DATABASE_NAME);
	}
	
}
