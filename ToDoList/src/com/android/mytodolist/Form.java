package com.android.mytodolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.android.todolist.R;

public class Form extends Activity {
	private EditText title;
	private EditText text;
	private Button enter;
	private DBHelper helper;
	int mode = 0;
	long _id = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note);

		enter = (Button) findViewById(R.id.save);
		title = (EditText) findViewById(R.id.title);
		text = (EditText) findViewById(R.id.text);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {

			mode = extras.getInt(NoteEntry.MODE);

			if (mode == NoteEntry.EDIT) {
				String titleString = extras
						.getString(NoteEntry.COLUMN_NAME_TITLE);
				String textString = extras
						.getString(NoteEntry.COLUMN_NAME_TEXT);
				_id = extras.getLong(NoteEntry.COLUMN_NAME_ENTRY_ID);

				title.setText(titleString);
				text.setText(textString);

			}

		}

		helper = new DBHelper(this);

		enter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// get information from form
				String ttl = title.getEditableText().toString();
				String txt = text.getEditableText().toString();
				
				if(mode == NoteEntry.EDIT){
					helper.updateNote(_id, ttl, txt);
				}else{
					helper.insertNote(ttl, txt);

				}	
				
				Intent intent = new Intent(Form.this, Main.class);
				startActivity(intent);
			}
		});

	}

	@Override
	protected void onPause() {
		super.onPause();
		helper.close();
	}

}
