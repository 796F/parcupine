package com.objects;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//[start time, end time, price, where, email]
public class HistoryOpenHelper extends SQLiteOpenHelper{


	private static final String DATABASE_NAME = "parkhistory.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME = "userhistory";
	private static final String TABLE_CREATE =
		"CREATE TABLE " + TABLE_NAME + " (start TEXT, end TEXT, price TEXT, where TEXT, email TEXT);";
	
		public HistoryOpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(TABLE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}
	
}
