package com.android.mytodolist;


public class ScheduleEntry {

	public static final String TABLE_NAME = "schedule";
	public static final String COLUMN_NAME_ENTRY_ID = "_id";
	public static final String COLUMN_NAME_TITLE = "title";
	public static final String COLUMN_NAME_DESCRIPTION = "description";
	public static final String COLUMN_NAME_DATE = "date";
	public static final String COLUMN_NAME_TIME = "time";
	
	//for flags between activities
	public static final String MODE = "mode";
	public static final int NEW = 0;
	public static final int EDIT = 1;

}
