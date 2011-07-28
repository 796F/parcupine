package com.test;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

import com.objects.ParkObject;
import com.objects.ThrowDialog;
import com.quietlycoding.android.picker.NumberPickerDialog;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ViewFlipper;

/**
 * 
 * TIME LOGIC and BOOT LOAD SERVICE RESUME, real time updated timer display
 * logic ------------
 *       on parq, calculate first end-time, store and schedule
 *       on refill, get end time, calculate new end-time (old.mins+refill.mins) and schedule
 * CUSTOM buttons and GRAPHICS
 * 
 * TimeLeftDisplay = analog timer, digital countdown (setting gives choice) 
 * ?? change accounT to an only logout screen, which takes the user back to splash/login screen.
 * FIX MAP
 * Add server calls for rates.
 * PARKING is free after 7pm.  include a time check method which may alertdialog.  
 * 
 * WRITE alert dialog class for NumberPicker, looks bad.
 * Phone should vibrate etc if 5 minutes remain,
 *    service should shutdown once time is up. 
 *    
 * SEcurity in authenticating with server?  
 * Once we log in, autoclose the keyboard prompt
 * login splash screen?  no functionality unless registered.  
 * look into city's expenses, number of parks, gauge the server costs, lay out finance to potential
 *    partners
 * */

public class MainActivity extends Activity {
	
	/* on bootup, check for parkstate true.  
	 * if state is parked, update timer and resume service.
	 *   
	 * when qr code is scanned, grab current date, calculate end date, 
	 * start timer for end date, and store end date.  
	 *    send end date associated with phonenumber and acc.  start timer on server end.  
	 * 
	 * 
	 * when qrcode scanned again / service ends or time out, or unparqed 
	 * delete end date, parkstate false.  
	 *    send unparq to server. 
	 *    
	 *    EXITING always logs out, unless parked.  


IF NOT logged in
	NO PARK
	STARTUP ACC PAGE

IF logged in
	PARK
	STARTUP PARK PAGE
	ACC PAGE starts second view
	exit logs out

IF remember checked
	STARTUP PARK PAGE
	ACC PAGE starts second view
	 * 
	 * 
	 * */

	private TextView priceDisplay;
	private TextView timeDisplay;
	
	private Button setTimeButton;
	private Button parqButton;
	private Button unparqButton;
	private Button refillButton;
	private Button hideButton;

	
	private int parkMinutes;
	
	static final int NUM_PICKER_ID = 2;
	private static final int REFILL_PICKER_ID = 0;
	private ViewFlipper vf;
	public static final String SAVED_INFO = "ParqMeInfo";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flipper);
		vf = (ViewFlipper) findViewById(R.id.flipper);

		final SharedPreferences check = getSharedPreferences(SAVED_INFO,0);
		final SharedPreferences.Editor editor = check.edit();
		//crash on return, cause is here?
		if(check.getBoolean("parkState", false)){
			vf.showNext();
		}
		//load correct layout which has the time selector and camera view.  
		priceDisplay = (TextView) findViewById(R.id.textView1);
		timeDisplay = (TextView) findViewById(R.id.textView5);
		setTimeButton = (Button) findViewById(R.id.firstparq);
		setTimeButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(2);
			}
		});
		
		parqButton = (Button) findViewById(R.id.secondparq);
		parqButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!check.getBoolean("loginState", false)){
					ThrowDialog.show(MainActivity.this, ThrowDialog.MUST_LOGIN);
					
				}else{


					//TODO IF TIME SET ==0 minutes, SHOW DIALOG.  
					Intent intent = new Intent("com.google.zxing.client.android.MYSCAN");
					intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
					startActivityForResult(intent, 0);
					
				}
			}});

		
		unparqButton = (Button) findViewById(R.id.unparqbutton);
		unparqButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String qrcode = check.getString("code", "badcode");
				String email = check.getString("email", "bademail");
				//	    		are you sure? dialogue
				//	    				if sure start summary activity
				//	    				          this has time parked, amount spent, 
				if(ServerCalls.unPark(qrcode, email)==1){
					stopService(new Intent(MainActivity.this, Background.class));
					vf.showPrevious();
					editor.putBoolean("parkState", false);
					editor.commit();
				}else{
					
					//interpret response code
				}
			}});
		

		refillButton = (Button) findViewById(R.id.refillbutton);
		
		refillButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(REFILL_PICKER_ID);
			}});

	}

	private static int getRates(int LOCATION_CODE){
		//CENTS PER 15 MINUTS
		/*	Send http request to servlet with location code
		 *  servlet responds with a double indicating rate
		 *  
		 *  URLConnection test = new URLConnection("http://www.parqme.com/SERVLET");
		 * */
		/* myLocation = getLocation();
		 * return rates of myLocation;
		 * */
		return 25;
	}


	private static int getCostInCents(int mins) {
		return mins/15 * getRates(0);

	}

	private NumberPickerDialog.OnNumberSetListener mNumberSetListener = 
		new NumberPickerDialog.OnNumberSetListener() {
		public void onNumberSet(int selectedNumber) {
			parkMinutes = selectedNumber;
			updateDisplay();

		}
	};
	private NumberPickerDialog.OnNumberSetListener mRefillListener =
		new NumberPickerDialog.OnNumberSetListener() {
			@Override
			public void onNumberSet(int selectedNumber) {
				parkMinutes+=selectedNumber;
				updateDisplay();
			}
		};
	private void updateDisplay() {
		priceDisplay.setText(parkMinutes +" Minutes"+" : " +moneyConverter(getCostInCents(parkMinutes)));
		timeDisplay.setText("time left =" + parkMinutes);
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case NUM_PICKER_ID:
			NumberPickerDialog x =new NumberPickerDialog(this, 0, 0);
			x.setOnNumberSetListener(mNumberSetListener);
			return x;
		
		case REFILL_PICKER_ID:
			NumberPickerDialog y = new NumberPickerDialog(this, 0,0);
			y.setOnNumberSetListener(mRefillListener);
			return y;
		}
		return null;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) { //TODO SHOULD ALSO CHECK if returned park object is good, before we make changes.

				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				
				/*scan results will contain an integer number, which represents a park location*/

				/*send request to server with email + contentInt + time*/
				timeDisplay.setText("RESULT GOT" + contents);
				SharedPreferences check = getSharedPreferences(SAVED_INFO,0);
				String email = check.getString("email", "bademail");
				SharedPreferences.Editor editor = check.edit();
				editor.putString("code", contents);
				//TODO
				String endtime = "FAKEENDTIME";
				ParkObject myPark = ServerCalls.Park(contents, email, endtime);
				if(myPark!=null){
					vf.showNext();
					startService(new Intent(MainActivity.this, Background.class).putExtra("time", parkMinutes));
					/*parkState changes how app resumes*/
					editor.putBoolean("parkState", true);
					timeDisplay.setText("You are parked in " + myPark.getLocation()+"\n Spot " + myPark.getSpotNum());
				}
				
				editor.commit();


			} else if (resultCode == RESULT_CANCELED) {
				priceDisplay.setText("This QR Code is Broken");
				// Handle cancel
			}
		}
	}


	public String moneyConverter (int cents){

		int m = cents;
		if(m/100 > 0){
			// there's at least 1 dollar
			int dollars = m/100;
			cents = m%100;
				if(cents==0)
					return "$ " + dollars + ".00";
			return "$ " + dollars + "." + cents;

		}
		// there's only cents
		else {
			if(cents==0)
				return " $ 0.00";
			return "$ 0." + cents;
			
		}
	}

}
