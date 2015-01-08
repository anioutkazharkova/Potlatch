package org.coursera.capstone.client.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	static String dataBase="PotlatchDb.db";
	static int version=1;
	
	public DatabaseHelper(Context context)
	{
		
		super(context,dataBase, null, version);
		Log.d("db","constructor");
	}
	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String createImageEntity="create table image_entity (id integer primary key autoincrement, url text, image_bytes blob);";
		db.execSQL(createImageEntity);
		Log.d("db","create table");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
