package com.android.mytodolist;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.android.todolist.R;

public class Form extends Activity {
	private EditText title;
	private EditText description;
	private TextView dateText;
	private Button enter;
	private DBHelper helper;
	private DatePicker date;
	int mode = 0;
	long _id = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule);
		
		enter = (Button) findViewById(R.id.save);
		title = (EditText) findViewById(R.id.title);
		description = (EditText) findViewById(R.id.description);
		dateText = (TextView) findViewById(R.id.dateText);
		date = (DatePicker) findViewById(R.id.calendar);
		

		Bundle extras = getIntent().getExtras();
		if (extras != null) {

			mode = extras.getInt(ScheduleEntry.MODE);

			if (mode == ScheduleEntry.EDIT) {
				String titleString = extras
						.getString(ScheduleEntry.COLUMN_NAME_TITLE);
				String descriptionString = extras
						.getString(ScheduleEntry.COLUMN_NAME_DESCRIPTION);
				
				int getDateInt = extras.getInt(ScheduleEntry.COLUMN_NAME_DATE);
				long getDateLong = getDateInt * 1000;
				Date getDateValue = new Date(getDateLong);
				SimpleDateFormat format = new SimpleDateFormat("MMM-dd-yyyy");
				String getDateString = format.format(getDateValue);
				
				_id = extras.getLong(ScheduleEntry.COLUMN_NAME_ENTRY_ID);

				title.setText(titleString);
				description.setText(descriptionString);
				dateText.setText(getDateString);
			}

		}

		helper = new DBHelper(this);

		enter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// get information from form
				String ttl = title.getEditableText().toString();
				String des = description.getEditableText().toString();
				long dateValueLong = dateToCal(date).getTimeInMillis();
				int dataValueInt = (int)dateValueLong;
				
				if(mode == ScheduleEntry.EDIT){
					helper.updateSchedule(_id, ttl, des, dataValueInt);
				}else{
					helper.insertSchedule(ttl, des, dataValueInt);

				}	
				
				Intent intent = new Intent(Form.this, ScheduleListActivity.class);
				startActivity(intent);
			}
		});

	}

	@Override
	protected void onPause() {
		super.onPause();
		helper.close();
	}
	
	private Calendar dateToCal(DatePicker dp){
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.set(Calendar.YEAR, dp.getYear());
		cal.set(Calendar.MONTH, dp.getMonth());
		cal.set(Calendar.DAY_OF_MONTH, dp.getDayOfMonth());
		
		return cal;
	}

}
