package com.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class ParqActivity extends Activity {
	
	private TextView priceDisplay;
    private Button parqButton;
    private Button parqButton2;
    private int mHour=0;
    private int mMinute=0;
    private int parkMinutes;
    static final int TIME_DIALOG_ID = 0;
    static final int OKAY_DIALOGUE_ID = 1;
    
    
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.relative_parq);
	    //load correct layout which has the time selector and camera view.  
	    priceDisplay = (TextView) findViewById(R.id.textView1);
	    parqButton = (Button) findViewById(R.id.firstparq);
	    parqButton2 = (Button) findViewById(R.id.secondparq);
	    Button testButton = (Button) findViewById(R.id.testbutton);
	    
	    testButton.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
	    		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
	    		intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
	    		startActivityForResult(intent, 0);
	    		
			}});
	    
	    parqButton.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
				parqButton.setVisibility(-1);
				parqButton2.setVisibility(0);
				showDialog(TIME_DIALOG_ID);
			}});
	    
	    parqButton2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(ParqActivity.this, TimeLeft.class);
				Bundle time = new Bundle();
				time.putInt("time",  parkMinutes );
				myIntent.putExtras(time);
				startActivity(myIntent);
				
			}});
	}
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		   if (requestCode == 0) {
		      if (resultCode == RESULT_OK) {
		         String contents = intent.getStringExtra("SCAN_RESULT");
		         String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
		         // Handle successful scan
		      } else if (resultCode == RESULT_CANCELED) {
		         // Handle cancel
		      }
		   }
		}

	
	private void updateDisplay() {
	    priceDisplay.setText(getPrice(parkMinutes));
	}

	private static String getPrice(int mins) {
		//TODO: Bad Method.  
		return "parking minutes = "+mins;

	}
	
	private TimePickerDialog.OnTimeSetListener mTimeSetListener =
	    new TimePickerDialog.OnTimeSetListener() {
	        public void onTimeSet(TimePicker view, int hours, int minute) {
	            parkMinutes = (hours * 60) + minute;
	            //showDialog(OKAY_DIALOGUE_ID);
	            //Are you sure you want to add time?  (Yes/No)
	            updateDisplay();
	        }
	    };

	    protected Dialog onCreateDialog(int id) {
	        switch (id) {
	        case TIME_DIALOG_ID:
	            return new TimePickerDialog(this,
	                    mTimeSetListener, mHour, mMinute, false);
//	        case OKAY_DIALOGUE_ID:
//	        	return new AlertDialog(this, blah blah);
	        }
	        return null;
	    }
	    
}
