package com.test;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class TimeLeft extends Activity {
	private TextView timeDisplay;
    private Button unparqButton;
    private Button refillButton;
    private Bundle b;
    private int mHour=0;
    private int mMinute=0;
    private int timeleft;
    static final int TIME_DIALOG_ID = 0;
    
	/** Called when the activity is first created. */
	@Override
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
	    		
	    		Intent myIntent = new Intent(TimeLeft.this, TabsActivity.class);
				startActivity(myIntent);
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
}
