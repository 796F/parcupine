package com.test;

import java.util.ArrayList;
import java.util.Timer;

import com.quietlycoding.android.picker.NumberPickerDialog;

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
 * onFinish(), how to end activity and return bundled instance info.
 * Currently crashing on refill when trying to show the dialog box.  works when you start via startActivity w/o extras
 * Currently exiting on unparq, have it return to other view.  
 * 
 * LOCK BACK BUTTON HERE
 * */

public class TimeLeft extends ActivityGroup {
	private Timer mytimer;
	private TextView timeDisplay;
	private Button unparqButton;
	private Button refillButton;
	private Bundle b;
	private int parkMinutes;
	static final int TIME_DIALOG_ID = 0;
	private static final int NUM_PICKER_ID = 0;
	public static TimeLeft group;
	private TimeLeft z =this;

	@Override

	/**/
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeleft);
		z = this;

		unparqButton = (Button) findViewById(R.id.unparqbutton);
		timeDisplay = (TextView) findViewById(R.id.textView5);

		b = getIntent().getExtras();
		parkMinutes = b.getInt("time", 0);
		timeDisplay.setText("The minutes left = " + parkMinutes);

		unparqButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				//	    		are you sure? dialogue
				//	    				if sure start summary activity
				//	    				          this has time parked, amount spent, 
				stopService(new Intent(TimeLeft.this, Background.class));

				
				Intent myIntent = new Intent(TimeLeft.this, TabsActivity.class);
				myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				View view = getLocalActivityManager().startActivity("TimeLeft",myIntent).getDecorView();
				Bundle time = new Bundle();
				time.putInt("time",  parkMinutes );  //Bundle parktime with intent.
				myIntent.putExtras(time);
				startActivity(myIntent);
				
				//UNPARQ should maybe take us to goodbye screen, which then goes back to start activity
				//setContentView(view);
			}});


		refillButton = (Button) findViewById(R.id.refillbutton);


		refillButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//prompt a dialogue
				//increase time on timer
				//increase session cost
				showDialog(NUM_PICKER_ID);
				//timeDisplay.setText();
			}});
	}
	
	private void updateDisplay() {
		timeDisplay.setText("The minutes left = " +parkMinutes);
	}

	private NumberPickerDialog.OnNumberSetListener mNumberSetListener = 
		new NumberPickerDialog.OnNumberSetListener() {
		public void onNumberSet(int selectedNumber) {
			parkMinutes = selectedNumber;
			updateDisplay();

		}
	};

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case NUM_PICKER_ID:
			//this.getApplicationContext();
			NumberPickerDialog x =new NumberPickerDialog(z.getApplicationContext(), 1, 0);
			x.setOnNumberSetListener(mNumberSetListener);
			return x;
		}
		return null;
	}
}
