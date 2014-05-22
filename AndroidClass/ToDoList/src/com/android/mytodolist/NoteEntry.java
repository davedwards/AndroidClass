package com.android.mytodolist;


public class NoteEntry {

	public static final String TABLE_NAME = "entry";
	public static final String COLUMN_NAME_ENTRY_ID = "_id";
	public static final String COLUMN_NAME_TITLE = "title";
	public static final String COLUMN_NAME_TEXT = "text";
	
	//for flags between activities
	public static final String MODE = "mode";
	public static final int NEW = 0;
	public static final int EDIT = 1;

}
