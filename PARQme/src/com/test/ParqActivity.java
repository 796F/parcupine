package com.test;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class ParqActivity extends Activity {
	private TextView priceDisplay;
    private Button parqButton;
    private int mHour=0;
    private int mMinute=0;
    private int parkMinutes;

    static final int TIME_DIALOG_ID = 0;
    
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.relative_parq);
	    //load correct layout which has the time selector and camera view.  
	    priceDisplay = (TextView) findViewById(R.id.textView1);
	    parqButton = (Button) findViewById(R.id.button1);
	    parqButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog(TIME_DIALOG_ID);
				
			}
		});
	    
	    updateDisplay();
	}
	private void updateDisplay() {
	    priceDisplay.setText(
	        new StringBuilder()
	        		.append(getPrice(parkMinutes)));
	}

	private static String getPrice(int mins) {
		//for testing purposes, must rewrite later.
		return "parktime is "+mins;

	}
	private TimePickerDialog.OnTimeSetListener mTimeSetListener =
	    new TimePickerDialog.OnTimeSetListener() {
	        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	            parkMinutes = (hourOfDay * 60) + minute;
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
