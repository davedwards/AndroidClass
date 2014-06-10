package com.android.mytodolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper {

	private static final String TEXT_TYPE = " TEXT";
	private static final String COMMA_SEP = ",";
	private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
			+ ScheduleEntry.TABLE_NAME + " ("
			+ ScheduleEntry.COLUMN_NAME_ENTRY_ID + " INTEGER PRIMARY KEY,"
			+ ScheduleEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP
			+ ScheduleEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP
			+ ScheduleEntry.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP
			+ ScheduleEntry.COLUMN_NAME_TIME + TEXT_TYPE + " )";

	private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
			+ ScheduleEntry.TABLE_NAME;

	private Context context;
	private ScheduleDBHelper mDbHelper;
	private SQLiteDatabase db;

	public DBHelper(Context context) {
		this.context = context;
		mDbHelper = new ScheduleDBHelper(context);

	}

	public long insertSchedule(String title, String description, String date,
			String time) {
		db = mDbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(ScheduleEntry.COLUMN_NAME_TITLE, title);
		cv.put(ScheduleEntry.COLUMN_NAME_DESCRIPTION, description);
		cv.put(ScheduleEntry.COLUMN_NAME_DATE, date);
		cv.put(ScheduleEntry.COLUMN_NAME_TIME, time);

		return db.insert(ScheduleEntry.TABLE_NAME, null, cv);
	}

	public void updateSchedule(long id, String title, String text, String date,
			String time) {

		db = mDbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(ScheduleEntry.COLUMN_NAME_TITLE, title);
		cv.put(ScheduleEntry.COLUMN_NAME_DESCRIPTION, text);
		cv.put(ScheduleEntry.COLUMN_NAME_DATE, date);
		cv.put(ScheduleEntry.COLUMN_NAME_TIME, time);
		String[] args = { String.valueOf(id) };

		db.update(ScheduleEntry.TABLE_NAME, cv, "_id=?", args);
	}

	public void deleteSchedule(long id) {

		String[] args = { String.valueOf(id) };
		db.delete(ScheduleEntry.TABLE_NAME, "_id=?", args);

	}

	public Schedule getSingleNote(long id) {

		db = mDbHelper.getReadableDatabase();

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = { "*" };

		String[] args = { String.valueOf(id) };

		Cursor c = db.query(ScheduleEntry.TABLE_NAME, // The table to query
				projection, // The columns to return
				"where " + ScheduleEntry.COLUMN_NAME_ENTRY_ID + "=?", // The
																		// columns
																		// for
																		// the
																		// WHERE
																		// clause
				args, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				null // The sort order
				);

		c.moveToFirst();
		String title = c.getString(c
				.getColumnIndex(ScheduleEntry.COLUMN_NAME_TITLE));
		String description = c.getString(c
				.getColumnIndex(ScheduleEntry.COLUMN_NAME_DESCRIPTION));
		String date = c.getString(c
				.getColumnIndex(ScheduleEntry.COLUMN_NAME_DATE));
		String time = c.getString(c
				.getColumnIndex(ScheduleEntry.COLUMN_NAME_TIME));

		Schedule result = new Schedule(id, title, description, date, time);
		return result;
	}

	public Cursor getAll() {

		db = mDbHelper.getReadableDatabase();

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = { ScheduleEntry.COLUMN_NAME_ENTRY_ID,
				ScheduleEntry.COLUMN_NAME_TITLE,
				ScheduleEntry.COLUMN_NAME_DESCRIPTION,
				ScheduleEntry.COLUMN_NAME_DATE, ScheduleEntry.COLUMN_NAME_TIME };

		Cursor c = db.query(ScheduleEntry.TABLE_NAME, // The table to query
				projection, // The columns to return
				null, // The columns for the WHERE clause
				null, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				null // The sort order
				);

		return c;
	}

	public void close() {
		if (db != null) {
			db.close();
		}
	}

	public class ScheduleDBHelper extends SQLiteOpenHelper {
		public static final int DATABASE_VERSION = 1;
		public static final String DATABASE_NAME = "Schedule.db";

		public ScheduleDBHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);

		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.i("myTag", SQL_CREATE_ENTRIES);
			db.execSQL(SQL_CREATE_ENTRIES);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(SQL_DELETE_ENTRIES);
			onCreate(db);

		}

	}

}
