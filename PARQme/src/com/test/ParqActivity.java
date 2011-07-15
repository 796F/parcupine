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
/*



refillButton.OnClick{

}

unparqbutton.onclick{

}

RESUMING APP TO CERTAIN ACTIVITY*/


public class ParqActivity extends Activity {
	private TextView priceDisplay;
    private Button parqButton;
    private Button parqButton2;
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
	    parqButton = (Button) findViewById(R.id.firstparq);
	    parqButton2 = (Button) findViewById(R.id.secondparq);
	    
	    
	    parqButton.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
//				//prompt time, get hour input, 
//	    		hide parqbutton, show parqbutton2, 
//	    		hide closed shutter image, show qr code image, 
//	    		update display of textview to include price
				parqButton.setVisibility(-1);
				parqButton2.setVisibility(0);
			}});
	    parqButton2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
//					start timeleft activity, 
//					start countdown timer
				Intent myIntent = new Intent(ParqActivity.this, TimeLeft.class);
				Bundle time = new Bundle();
				time.putInt("time",  parkMinutes );
				myIntent.putExtras(time);
				startActivity(myIntent);
				
			}});
	    /**
	     * there is an issue with using setContentView to pull up another layout.  for example, we have layout1 and layout2,
	     * layout1 loads on startup.  when I setContentView(layout2) and try to set a button in layout2's fields, it breaks.  
	     * perhaps android doesn't see the button, or those buttons aren't instantiated yet
	     * 
	     * the above works kinda, but when we go back to relative_parq layout, button listeners have reset.  now useless.
	     * 
	     * net suggests new activities per screen, so maybe use intent or  startActivity() and startActivityForResult() instead. 
	     * Keep in mind ViewFlipper */
	    

	    
	   // updateDisplay();
	}
	private void updateDisplay() {
	    priceDisplay.setText(
	        new StringBuilder()
	        		.append(getPrice(parkMinutes)).append(parqButton.getVisibility()));
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
