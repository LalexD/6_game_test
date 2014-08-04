package com.example.test_game.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import com.example.test_game.game.Game;
import com.example.test_game.records.Record;

public class GameDB extends SQLiteOpenHelper {
	private Context context;
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "game_DB";
	private static final String TABLE_RECORDS = "records";
	private static final String TABLE_SAVE_GAME = "save_game";
	private static final String RECORD_KEY_ID = "id";
	private static final String RECORD_KEY_NAME = "name";
	private static final String RECORD_KEY_NUMANSWERS = "answers";
	private static final String RECORD_KEY_DATE = "date";
	private static final String GAME_KEY_ID = "id";
	private static final String GAME_KEY_NUM_ANSWER = "num_answer";
	private static final String GAME_KEY_SCORE = "score";
	private static final String GAME_KEY_AMSWER_1 = "answer_1";
	private static final String GAME_KEY_AMSWER_2 = "answer_2";
	private static final String GAME_KEY_AMSWER_3 = "answer_3";
	private static final String GAME_KEY_AMSWER_4 = "answer_4";
	private static final String GAME_KEY_CORR_ANSWER = "corr_answer";
	private static final String CREATE_TABLE_RECORDS = "CREATE TABLE "
	        + TABLE_RECORDS + "(" + RECORD_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
	        + RECORD_KEY_NAME + " TEXT," + RECORD_KEY_NUMANSWERS + " INTEGER," + RECORD_KEY_DATE
	        + " INTEGER)";
	private static final String CREATE_TABLE_SAVE_GAME = "CREATE TABLE "
	        + TABLE_SAVE_GAME + "(" + GAME_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
	        + GAME_KEY_NUM_ANSWER + " INTEGER," + GAME_KEY_SCORE + " INTEGER," + GAME_KEY_CORR_ANSWER
	        + " INTEGER," + GAME_KEY_AMSWER_1 + " INTEGER," + GAME_KEY_AMSWER_2
	        + " INTEGER," + GAME_KEY_AMSWER_3 + " INTEGER," + GAME_KEY_AMSWER_4 + " INTEGER)";

	public GameDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_RECORDS);
		db.execSQL(CREATE_TABLE_SAVE_GAME);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void deleteGame() {
		SQLiteDatabase dataBase = this.getWritableDatabase();
		dataBase.delete(TABLE_SAVE_GAME, null, null);
		dataBase.close();
	}

	public void saveGame(Game game) {
		SQLiteDatabase dataBase = this.getWritableDatabase();
		dataBase.delete(TABLE_SAVE_GAME, null, null);
		ContentValues values = new ContentValues();
		values.put(GAME_KEY_NUM_ANSWER, game.getNumAnswers());
		values.put(GAME_KEY_SCORE, game.getScore());
		values.put(GAME_KEY_CORR_ANSWER, game.getCorrAnswer());
		values.put(GAME_KEY_AMSWER_1, game.getAnswerButton1());
		values.put(GAME_KEY_AMSWER_2, game.getAnswerButton2());
		values.put(GAME_KEY_AMSWER_3, game.getAnswerButton3());
		values.put(GAME_KEY_AMSWER_4, game.getAnswerButton4());
		dataBase.insert(TABLE_SAVE_GAME, null, values);
		dataBase.close();
	}

	public Game getGame() {
		Game game = new Game(context);
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SAVE_GAME, null);
		cursor.moveToFirst();
		db.close();
		if (cursor != null && cursor.getCount() > 0) {
			game.setData(cursor.getInt(1), cursor.getInt(2), cursor.getInt(3),
			        cursor.getInt(4), cursor.getInt(5), cursor.getInt(6), cursor.getInt(7));
			return game;
		}
		else
			return null;
	}

	public void addRecord(Record record) {
		SQLiteDatabase dataBase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(RECORD_KEY_NAME, record.getName());
		values.put(RECORD_KEY_NUMANSWERS, record.getScore());
		values.put(RECORD_KEY_DATE, record.getDate());
		dataBase.insert(TABLE_RECORDS, null, values);
		dataBase.execSQL("DELETE FROM " + TABLE_RECORDS + " WHERE " + RECORD_KEY_ID
		        + " NOT IN ( SELECT " + RECORD_KEY_ID + " FROM " + TABLE_RECORDS
		        + " ORDER BY " + RECORD_KEY_NUMANSWERS + " DESC LIMIT 10 )");
		dataBase.close();

	}

	public List<Record> getTopRecords() {
		List<Record> listRecord = new ArrayList<Record>();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RECORDS
		        + " ORDER BY " + RECORD_KEY_NUMANSWERS + " DESC", null);
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
		        + " WHERE " + RECORD_KEY_NUMANSWERS + " = (SELECT MIN("
		        + RECORD_KEY_NUMANSWERS + ") FROM " + TABLE_RECORDS + ")", null);
		cursor.moveToFirst();
		db.close();
		if (cursor != null && cursor.getCount() > 0)
			return cursor.getInt(2);
		else
			return 0;

	}

	public boolean checkNumRecords(int num) {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RECORDS, null);
		if (cursor.getCount() < num)
			return false;
		else
			return true;
	}
}
