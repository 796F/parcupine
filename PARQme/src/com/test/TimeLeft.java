package com.test;

import java.util.ArrayList;
import java.util.Timer;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Include Yes/No Dialogues
 * Create Timer
 * onFinish(), how to end activity and return bundled instance info.
 * Currently crashing on refill when trying to show the dialog box.  works when you start via startActivity w/o extras
 * Currently exiting on unparq, have it return to other view.  
 * */

public class TimeLeft extends ActivityGroup {
	private Timer mytimer;
	private TextView timeDisplay;
	private Button unparqButton;
	private Button refillButton;
	private Bundle b;
	private int mHour=0;
	private int mMinute=0;
	private int timeleft;
	static final int TIME_DIALOG_ID = 0;
	public static TimeLeft group;
	private ArrayList<View> history;

	@Override

	/**/
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeleft);

		unparqButton = (Button) findViewById(R.id.unparqbutton);
		timeDisplay = (TextView) findViewById(R.id.textView5);

		b = getIntent().getExtras();
		timeleft = b.getInt("time", 0);
		timeDisplay.setText("The minutes left = " + timeleft);

		unparqButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				//	    		are you sure? dialogue
				//	    				if sure start summary activity
				//	    				          this has time parked, amount spent, 
				Intent myIntent = new Intent(TimeLeft.this, ParqActivity.class);
				myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				View view = getLocalActivityManager().startActivity("TimeLeft",myIntent).getDecorView();
				Bundle time = new Bundle();
				time.putInt("time",  timeleft );  //Bundle parktime with intent.
				myIntent.putExtras(time);

				//replaceView(view);
				setContentView(view);
			}});


		refillButton = (Button) findViewById(R.id.refillbutton);


		refillButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//prompt a dialogue
				//increase time on timer
				//increase session cost
				showDialog(TIME_DIALOG_ID);
			}});
	}
	private void updateDisplay() {
		timeDisplay.setText("The minutes left = " +timeleft);
	}

	private TimePickerDialog.OnTimeSetListener mTimeSetListener =
		new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hours, int minute) {
			timeleft += (hours*60) + minute;
			updateDisplay();
		}
	};

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this,
					mTimeSetListener, mHour, mMinute, false);
		}
		return null;
	}
	public void back() {  
		if(history.size() > 0) {  
			history.remove(history.size()-1);  
			setContentView(history.get(history.size()-1));  
		}else {  
			finish();  
		}  
	}  
	public void replaceView(View v) {  
		// Adds the old one to history  
		history.add(v);  
		// Changes this Groups View to the new View.  
		setContentView(v);  
	}
	@Override  
	public void onBackPressed() {  
		TimeLeft.group.back();  
		return;  
	}
}
