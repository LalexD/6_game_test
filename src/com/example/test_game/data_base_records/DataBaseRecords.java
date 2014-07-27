package com.example.test_game.data_base_records;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseRecords extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "dbRecords";
	private static final String TABLE_RECORDS = "records";
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_NUMANSWERS = "answers";
	private static final String KEY_DATE = "date";

	public DataBaseRecords(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_RECORDS + "(" + KEY_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
				+ KEY_NUMANSWERS + " INTEGER," + KEY_DATE + " INTEGER)");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void addRecord(Record record) {
		SQLiteDatabase dataBase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, record.getName());
		values.put(KEY_NUMANSWERS, record.getScore());
		values.put(KEY_DATE, record.getDate());
		dataBase.insert(TABLE_RECORDS, null, values);
		dataBase.execSQL("DELETE FROM " + TABLE_RECORDS + " WHERE " + KEY_ID
				+ " NOT IN ( SELECT " + KEY_ID + " FROM " + TABLE_RECORDS
				+ " ORDER BY " + KEY_NUMANSWERS + " DESC LIMIT 10 )");
		dataBase.close();

	}

	public List<Record> getTopRecords() {
		List<Record> listRecord = new ArrayList<Record>();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RECORDS
				+ " ORDER BY " + KEY_NUMANSWERS + " DESC", null);
		if (cursor.moveToFirst()) {
			do {
				Record record = new Record(cursor.getString(1),
						cursor.getInt(2), cursor.getLong(3));
				listRecord.add(record);
			} while (cursor.moveToNext());
		}
		db.close();
		return listRecord;

	}

	public int getMinScore() {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RECORDS
				+ " WHERE " + KEY_NUMANSWERS + " = (SELECT MIN("
				+ KEY_NUMANSWERS + ") FROM " + TABLE_RECORDS + ")", null);
		cursor.moveToFirst();
		db.close();
		if (cursor != null && cursor.getCount() > 0)
			return cursor.getInt(2);
		else
			return 0;

	}

	public boolean checkNumRecords(int num){
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RECORDS, null);
		if(cursor.getCount()<num) return false;
		else return true;
	}
}
