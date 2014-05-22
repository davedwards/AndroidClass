package com.android.mytodolist;
import model.Note;
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
			+ NoteEntry.TABLE_NAME + " (" + NoteEntry.COLUMN_NAME_ENTRY_ID
			+ " INTEGER PRIMARY KEY," + NoteEntry.COLUMN_NAME_TITLE + TEXT_TYPE
			+ COMMA_SEP + NoteEntry.COLUMN_NAME_TEXT + TEXT_TYPE
			+ " )";

	private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
			+ NoteEntry.TABLE_NAME;

	private Context context;
	private NotesDBHelper mDbHelper;
	private SQLiteDatabase db;

	public DBHelper(Context context) {
		this.context = context;
		mDbHelper = new NotesDBHelper(context);

	}

	public long insertNote(String title, String text) {
		db = mDbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(NoteEntry.COLUMN_NAME_TITLE, title);
		cv.put(NoteEntry.COLUMN_NAME_TEXT, text);

		return db.insert(NoteEntry.TABLE_NAME, null, cv);
	}
	
	public void updateNote(long id, String title, String text){
		
		db = mDbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(NoteEntry.COLUMN_NAME_TITLE, title);
		cv.put(NoteEntry.COLUMN_NAME_TEXT, text);
		String[] args = {String.valueOf(id)};

		db.update(NoteEntry.TABLE_NAME, cv, "_id=?", args);
	}
	
	public void deleteNote(long id){
		
		String[] args = {String.valueOf(id)};
		db.delete(NoteEntry.TABLE_NAME,"_id=?", args);
		
	}

	public Note getSingleNote(long id) {

		db = mDbHelper.getReadableDatabase();

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = { "*" };

		String[] args = { String.valueOf(id) };

		Cursor c = db.query(NoteEntry.TABLE_NAME, // The table to query
				projection, // The columns to return
				"where " + NoteEntry.COLUMN_NAME_ENTRY_ID + "=?", // The columns
																	// for the
																	// WHERE
																	// clause
				args, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				null // The sort order
				);

		c.moveToFirst();
		String title = c.getString(c
				.getColumnIndex(NoteEntry.COLUMN_NAME_TITLE));
		String text = c.getString(c.getColumnIndex(NoteEntry.COLUMN_NAME_TEXT));
		Note result = new Note(id, title, text);
		return result;
	}

	public Cursor getAll() {

		db = mDbHelper.getReadableDatabase();

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = { NoteEntry.COLUMN_NAME_ENTRY_ID, NoteEntry.COLUMN_NAME_TITLE,
				NoteEntry.COLUMN_NAME_TEXT };

		Cursor c = db.query(NoteEntry.TABLE_NAME, // The table to query
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
		if(db != null){
			db.close();
		}
	}

	public class NotesDBHelper extends SQLiteOpenHelper {
		public static final int DATABASE_VERSION = 1;
		public static final String DATABASE_NAME = "Notes.db";

		public NotesDBHelper(Context context) {
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
