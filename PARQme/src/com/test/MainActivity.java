package com.test;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

import com.objects.ParkObject;
import com.objects.SavedInfo;
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
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ViewFlipper;

/**
 * BOOT LOAD SERVICE RESUME
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
	private TextView userDisplay;
	private TextView locDisplay;
	private TextView timeDisplay;
	private Button setTimeButton;
	private Button parqButton;
	private Button unparqButton;
	private Button refillButton;
	private Button hideButton;
	private int remainSeconds;
	private CountDownTimer timer;

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
		priceDisplay = (TextView) findViewById(R.id.minutesprice);
		userDisplay = (TextView) findViewById(R.id.welcomeuser);
		locDisplay = (TextView) findViewById(R.id.location);
		timeDisplay = (TextView) findViewById(R.id.timeleftdisplay);
		setTimeButton = (Button) findViewById(R.id.firstparq);

		setTimeButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(NUM_PICKER_ID);
			}
		});

		parqButton = (Button) findViewById(R.id.secondparq);
		parqButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!check.getBoolean("loginState", false)){
					ThrowDialog.show(MainActivity.this, ThrowDialog.MUST_LOGIN);

				}else{
					if(parkMinutes==0){
						ThrowDialog.show(MainActivity.this, ThrowDialog.ZERO_MINUTES);
					}else{
						Intent intent = new Intent("com.google.zxing.client.android.MYSCAN");
						intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
						startActivityForResult(intent, 0);
					}
				}
			}});


		unparqButton = (Button) findViewById(R.id.unparqbutton);
		unparqButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//TODO ARE YOU SURE DIALOG
				String qrcode = check.getString("code", "badcode");
				String email = check.getString("email", "bademail");
				if(ServerCalls.unPark(qrcode, email)==1){
					stopService(new Intent(MainActivity.this, Background.class));
					vf.showPrevious();
					SavedInfo.togglePark(MainActivity.this);
					try{
						timer.cancel();
					}catch (Exception e){
						
					}
				}else{
					ThrowDialog.show(MainActivity.this, ThrowDialog.UNPARK_ERROR);
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
			
			//stop current timer, start new timer with current time + selectedNumber.
			try{
				timer.cancel();
			}catch(Exception e){
				
			}
			timer = initiateTimer(selectedNumber*60);
			timer.start();
			updateDisplay();
		}
	};
	private void updateDisplay() {
		priceDisplay.setText(parkMinutes +" Minutes"+" : " +moneyConverter(getCostInCents(parkMinutes)));
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
				//String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

				SharedPreferences check = getSharedPreferences(SAVED_INFO,0);
				String email = check.getString("email", "bademail");
				SharedPreferences.Editor editor = check.edit();
				editor.putString("code", contents);
				
				//TODO function initiateTimer starts a timer.  resuming app , refill button, and ONresult calls it.  
				timer = initiateTimer(parkMinutes*60);
				timer.start();
				String endtime = "FAKEENDTIME";
				ParkObject myPark = ServerCalls.Park(contents, email, endtime);
				if(myPark!=null){
					vf.showNext();
					startService(new Intent(MainActivity.this, Background.class).putExtra("time", parkMinutes));
					/*parkState changes how app resumes*/
					editor.putBoolean("parkState", true);
					userDisplay.setText("Welcome "); //TODO grab fname from user object
					locDisplay.setText("You are parked at " + myPark.getLocation()+"\nSpot " + myPark.getSpotNum());
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
	public static String formatMe(int seconds){
		SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss");
		try {
			Date x = sdf.parse("00:00:00");
			x.setSeconds(seconds);
			return (sdf.format(x));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "BADBADBAD";
	}
	
	public static long secToMil(int sec){
		return sec*1000;
	}
	public CountDownTimer initiateTimer(int countDownSeconds){
		if(!SavedInfo.isParked(MainActivity.this)){
			return new CountDownTimer(secToMil(countDownSeconds), 1000){
				@Override
				public void onFinish() {
					//unparq
				}
				@Override
				public void onTick(long arg0) {
					int seconds = (int)arg0/1000;
					remainSeconds = seconds;
					timeDisplay.setText("Time Left: " + formatMe(seconds));
				}

			};
		}else if(SavedInfo.isParked(MainActivity.this)){
			//it's a refill request
			return new CountDownTimer(secToMil(countDownSeconds+remainSeconds), 1000){

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onTick(long arg0) {
					int seconds = (int) arg0/1000;
					remainSeconds = seconds;
					timeDisplay.setText("Time Left: " + formatMe(seconds));
				}
				
			};
		}else{
			return null;
		}
	}

}
