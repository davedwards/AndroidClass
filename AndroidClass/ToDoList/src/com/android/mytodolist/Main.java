package com.android.mytodolist;

import android.app.ListActivity;
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
import android.widget.SimpleCursorAdapter;

import com.android.todolist.R;

public class Main extends ListActivity {

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
		columns = new String[] { NoteEntry.COLUMN_NAME_ENTRY_ID,
				NoteEntry.COLUMN_NAME_TITLE };
		// the XML defined views which the data will be bound to
		to = new int[] { R.id.id, R.id.title };

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
				Intent intent = new Intent(Main.this, Form.class);
				intent.putExtra(NoteEntry.MODE, NoteEntry.NEW);
				startActivity(intent);
			}
		});

		this.getListView().setLongClickable(true);
		this.getListView().setOnItemLongClickListener(
				new OnItemLongClickListener() {
					public boolean onItemLongClick(AdapterView<?> parent,
							View v, int position, long id) {
						SQLiteCursor data = (SQLiteCursor) mAdapter.getItem(position);

						data.moveToPosition(position);

						long _id = cursor.getLong(cursor
								.getColumnIndex(NoteEntry.COLUMN_NAME_ENTRY_ID));

						helper.deleteNote(_id);
						
						Cursor newCursor = helper.getAll();
						mAdapter.swapCursor(newCursor);
						
						Main.this.setListAdapter(mAdapter);				
						mAdapter.notifyDataSetChanged();

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
				.getColumnIndex(NoteEntry.COLUMN_NAME_ENTRY_ID));
		String title = data.getString(data
				.getColumnIndex(NoteEntry.COLUMN_NAME_TITLE));
		String text = data.getString(data
				.getColumnIndex(NoteEntry.COLUMN_NAME_TEXT));

		// pass the information to the display activity through bundle
		Intent intent = new Intent(Main.this, Form.class);
		intent.putExtra(NoteEntry.COLUMN_NAME_ENTRY_ID, _id);
		intent.putExtra(NoteEntry.COLUMN_NAME_TITLE, title);
		intent.putExtra(NoteEntry.COLUMN_NAME_TEXT, text);
		intent.putExtra(NoteEntry.MODE, NoteEntry.EDIT);
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
