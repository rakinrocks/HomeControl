package com.example.homecontrol.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class HomeDBOpenHelper extends SQLiteOpenHelper {
	public final static String DATABASE_NAME = "homeControlDB.db";
	public final static String TABLE_ZONES = "zones";
	public final static String TABLE_COMPONENTS = "components";
    public final static String TABLE_MODULES = "modules";
    /*  ZONE TABLE COLUMN NAMES */
    public final static String COLUMN_ZONE_NAME = "name";
    public final static String COLUMN_RESOURCEID = "resourceId";
    private static final String CREATE_ZONES_TABLE = "CREATE TABLE " + TABLE_ZONES + " (" +
            COLUMN_ZONE_NAME + " STRING, " +
            COLUMN_RESOURCEID + " INTEGER, " +
            "PRIMARY KEY (" + COLUMN_ZONE_NAME + "));";
    /* COMPONENTS TABLE COLUMN NAMES */
    public final static String COLUMN_COMP_ZONE_NAME = "czone"; // references the associated zone
	public final static String COLUMN_COMP_TYPE = "ctype";
	public final static String COLUMN_IP = "cip";
    private static final String CREATE_COMPONENTS_TABLE = "CREATE TABLE " + TABLE_COMPONENTS + " (" +
            COLUMN_COMP_ZONE_NAME + " STRING, " +
            COLUMN_COMP_TYPE + " INTEGER, " +
            COLUMN_IP + " STRING, " +
            "PRIMARY KEY (" + COLUMN_COMP_ZONE_NAME + ", " + COLUMN_IP + "), " +
            "FOREIGN KEY (" + COLUMN_COMP_ZONE_NAME + ") REFERENCES " + TABLE_ZONES + " (" + COLUMN_ZONE_NAME + ") " +
            "ON UPDATE CASCADE " +
            "ON DELETE CASCADE );";
    /* MODULES TABLE COLUMN NAMES */
    public final static String COLUMN_MOD_ID = "mid";
    public final static String COLUMN_MOD_ZONE_NAME = "mzone"; // references the associated zone
    public final static String COLUMN_MOD_NAME = "mname";
    public final static String COLUMN_MOD_STATUS = "mstatus";
    public final static String COLUMN_MOD_TYPE = "mtype";
    private static final String CREATE_MODULES_TABLE = "CREATE TABLE " + TABLE_MODULES + " (" +
            COLUMN_MOD_ID + " INTEGER AUTO_INCREMENT, " +
            COLUMN_MOD_ZONE_NAME + " STRING, " +
            COLUMN_MOD_NAME + " STRING, " +
            COLUMN_MOD_STATUS + " INTEGER, " +
            COLUMN_MOD_TYPE + " STRING, " +
            "PRIMARY KEY (" + COLUMN_MOD_ZONE_NAME + ", " + COLUMN_MOD_ID + "), " +
            "FOREIGN KEY (" + COLUMN_MOD_ZONE_NAME + ") REFERENCES " + TABLE_ZONES + " (" + COLUMN_ZONE_NAME + ") " +
            "ON UPDATE CASCADE " +
            "ON DELETE CASCADE );";
    private final static String LOGTAG = "HOMECONTROL";
    private final static int DATABASE_VERSION = 1;

	/* constructor */
	public HomeDBOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_ZONES_TABLE);
        Log.d(LOGTAG, "(CreateTable) " + CREATE_ZONES_TABLE);
		db.execSQL(CREATE_COMPONENTS_TABLE);
        Log.d(LOGTAG, "(CreateTable) " + CREATE_COMPONENTS_TABLE);
        db.execSQL(CREATE_MODULES_TABLE);
        Log.d(LOGTAG, "(CreateTable) " + CREATE_MODULES_TABLE);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ZONES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPONENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MODULES);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly())
            db.execSQL("PRAGMA foreign_keys=ON;");
    }
}
