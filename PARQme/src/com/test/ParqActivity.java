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

import com.google.zxing.client.android.CaptureActivity;

/**
 * Test ZXing, confirm working.  
 * Add time and charge logic.
 * Add servlet calls for rates.
 * */
	
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
	    		Intent intent = new Intent("com.google.zxing.client.android.MYSCAN");
	    		//Intent intent = new Intent(ParqActivity.this, CaptureActivity.class);
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
				time.putInt("time",  parkMinutes );  //Bundle parktime with intent.
				
				
				myIntent.putExtras(time);
				startActivity(myIntent);
			}});
	}
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		   if (requestCode == 0) {
		      if (resultCode == RESULT_OK) {
		         String contents = intent.getStringExtra("SCAN_RESULT");
		         String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
		         
		         priceDisplay.setText("RESULT_OKAY GOT");
		         
		         // Handle successful scan
		      } else if (resultCode == RESULT_CANCELED) {
		    	  priceDisplay.setText("RESULT BAD GOT :(");
		         // Handle cancel
		      }
		   }
		}

	
	private void updateDisplay() {
	    priceDisplay.setText("park costs = cents in" +costToText(getCost(parkMinutes)));
	}
	
	private static boolean pingDB(){
		
		
		return false;
	}
	
	private static double getRates(int LOCATION_CODE){
		/*	Send http request to servlet with location code
		 *  servlet responds with a double indicating rate
		 *  
		 *  URLConnection test = new URLConnection("http://www.parqme.com/SERVLET");
		 * */
		return 25.0/15;
	}
	
	private static String costToText(double cost){
		
		return "" + cost;
	}

	private static double getCost(int mins) {
		return mins*getRates(0);
		/* myLocation = getLocation();
		 * return mins*getRates(myLocation);
		 * */
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
