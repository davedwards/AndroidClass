package com.android.mytodolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.todolist.R;

public class Form extends Activity {
	private EditText title;
	private EditText description;
	private TextView dateText;
	private TextView timeText;
	private Button enter;
	private DBHelper helper;
	private DatePicker date;
	private TimePicker time;
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
		timeText = (TextView) findViewById(R.id.timeText);
		time = (TimePicker) findViewById(R.id.timePick);
		time.setIs24HourView(false);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {

			mode = extras.getInt(ScheduleEntry.MODE);

			if (mode == ScheduleEntry.EDIT) {
				String titleString = extras
						.getString(ScheduleEntry.COLUMN_NAME_TITLE);
				String descriptionString = extras
						.getString(ScheduleEntry.COLUMN_NAME_DESCRIPTION);
				String dateString = extras
						.getString(ScheduleEntry.COLUMN_NAME_DATE);
				String timeString = extras
						.getString(ScheduleEntry.COLUMN_NAME_TIME);
				
				_id = extras.getLong(ScheduleEntry.COLUMN_NAME_ENTRY_ID);

				title.setText(titleString);
				description.setText(descriptionString);
				dateText.setText(dateString);
				timeText.setText(timeString);
			}

		}

		helper = new DBHelper(this);

		enter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// get information from form
				String ttl = title.getEditableText().toString();
				String des = description.getEditableText().toString();
				
				int getYear = date.getYear();
				int getMonth = date.getMonth();
				int getDay = date.getDayOfMonth();
				
				int getHour = time.getCurrentHour();
				int getMinutes = time.getCurrentMinute();
				int dbHour = getHour % 12;
				String ampm;
				
				if(dbHour == 0) {
					dbHour = 12;
				}
				
				if(getHour > 11){
					ampm = "PM";
				} else {
					ampm = "AM";
				}
				
				String dateValue = getYear + "-" + getMonth + "-" + getDay;
				String timeValue = dbHour + ":" + getMinutes + " " + ampm;
				
				if(mode == ScheduleEntry.EDIT){
					helper.updateSchedule(_id, ttl, des, dateValue, timeValue);
				}else{
					helper.insertSchedule(ttl, des, dateValue, timeValue);

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
	
//	private Calendar dateToCal(DatePicker dp){
//		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
//		cal.set(Calendar.YEAR, dp.getYear());
//		cal.set(Calendar.MONTH, dp.getMonth());
//		cal.set(Calendar.DAY_OF_MONTH, dp.getDayOfMonth());
//		
//		return cal;
//	}

}
