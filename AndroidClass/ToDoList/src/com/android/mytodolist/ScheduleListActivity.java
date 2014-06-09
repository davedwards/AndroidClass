package com.android.mytodolist;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
//import android.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;

import com.android.todolist.R;

public class ScheduleListActivity extends ListActivity {

	private Button add;
	private DBHelper helper;
	private SimpleCursorAdapter mAdapter;
	private String[] columns;
	private int[] to;
	private Cursor cursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		helper = new DBHelper(this);

		cursor = helper.getAll();
		startManagingCursor(cursor);

		// the desired columns to be bound
		columns = new String[] { ScheduleEntry.COLUMN_NAME_ENTRY_ID,
				ScheduleEntry.COLUMN_NAME_TITLE,
				ScheduleEntry.COLUMN_NAME_DESCRIPTION,
				ScheduleEntry.COLUMN_NAME_DATE, ScheduleEntry.COLUMN_NAME_TIME };
		// the XML defined views which the data will be bound to
		to = new int[] { R.id.id, R.id.title, R.id.description, R.id.dateText,
				R.id.timeText };

		// create the adapter using the cursor pointing to the desired data as
		// well as the layout information
		mAdapter = new SimpleCursorAdapter(this, R.layout.row, cursor, columns,
				to);

		// set this adapter as your ListActivity's adapter
		this.setListAdapter(mAdapter);

		add = (Button) findViewById(R.id.add);

		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ScheduleListActivity.this,
						Form.class);
				intent.putExtra(ScheduleEntry.MODE, ScheduleEntry.NEW);
				startActivity(intent);
			}
		});

		this.getListView().setLongClickable(true);
		this.getListView().setOnItemLongClickListener(
				new OnItemLongClickListener() {
					@SuppressLint("NewApi")
					public boolean onItemLongClick(AdapterView<?> parent,
							View v, int position, long id) {
						SQLiteCursor data = (SQLiteCursor) mAdapter
								.getItem(position);

						data.moveToPosition(position);

						final long _id = cursor.getLong(cursor
								.getColumnIndex(ScheduleEntry.COLUMN_NAME_ENTRY_ID));

						// ***************

						AlertDialog.Builder alertDialog = new AlertDialog.Builder(
								ScheduleListActivity.this);
						alertDialog.setTitle("Delete");
						alertDialog.setNegativeButton("Delete",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {

										helper.deleteSchedule(_id);
										//
										 Cursor newCursor = helper.getAll();
										 mAdapter.swapCursor(newCursor);
										
										 ScheduleListActivity.this.setListAdapter(mAdapter);
										 mAdapter.notifyDataSetChanged();


									}
								});
						alertDialog.setPositiveButton("Keep",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										// alertDialog.dismiss();
									}
								});

						alertDialog.show();

						return true;
					}
				});

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// get the information from the database for this position
		SQLiteCursor data = (SQLiteCursor) mAdapter.getItem(position);

		data.moveToPosition(position);
		long _id = data.getLong(data
				.getColumnIndex(ScheduleEntry.COLUMN_NAME_ENTRY_ID));
		String title = data.getString(data
				.getColumnIndex(ScheduleEntry.COLUMN_NAME_TITLE));
		String description = data.getString(data
				.getColumnIndex(ScheduleEntry.COLUMN_NAME_DESCRIPTION));
		String date = data.getString(data
				.getColumnIndex(ScheduleEntry.COLUMN_NAME_DATE));
		String time = data.getString(data
				.getColumnIndex(ScheduleEntry.COLUMN_NAME_TIME));

		// pass the information to the display activity through bundle
		Intent intent = new Intent(ScheduleListActivity.this, Form.class);
		intent.putExtra(ScheduleEntry.COLUMN_NAME_ENTRY_ID, _id);
		intent.putExtra(ScheduleEntry.COLUMN_NAME_TITLE, title);
		intent.putExtra(ScheduleEntry.COLUMN_NAME_DESCRIPTION, description);
		intent.putExtra(ScheduleEntry.COLUMN_NAME_DATE, date);
		intent.putExtra(ScheduleEntry.COLUMN_NAME_TIME, time);
		intent.putExtra(ScheduleEntry.MODE, ScheduleEntry.EDIT);
		startActivity(intent);

	}

	@Override
	protected void onPause() {
		super.onPause();
		helper.close();
	}

	@Override
	protected void onResume() {
		super.onResume();

		helper = new DBHelper(this);
		cursor = helper.getAll();
		mAdapter = new SimpleCursorAdapter(this, R.layout.row, cursor, columns,
				to);
		mAdapter.notifyDataSetChanged();

	}
	
	

}
