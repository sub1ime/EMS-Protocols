package com.integraresoftware.emsbuddy.data;

import android.content.Context;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DbHandler extends SQLiteAssetHelper {
	
	public static final String TAG = "MainDbHandler";
	
	// database specific schema
	public static final String DATABASE_NAME = "ODEMSA_sqlite";
	public static final int DATABASE_VERSION = 20;
	
	public DbHandler(Context ctx){
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgradeVersion(DATABASE_VERSION);
		Log.d(TAG, "MainDbHandler accessed");			
	}


}
