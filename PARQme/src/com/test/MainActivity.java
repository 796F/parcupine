package com.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.objects.Global;
import com.objects.ParkObject;
import com.objects.SavedInfo;
import com.objects.ThrowDialog;
import com.quietlycoding.android.picker.NumberPicker;
import com.quietlycoding.android.picker.NumberPickerDialog;
import com.quietlycoding.android.picker.Picker;

import android.media.MediaPlayer;
import android.os.Vibrator;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.ViewFlipper;

/**
 * find parking nearby, find parking general, find my car(popup message w/ info)
 *
 * MAX # of refills      just store number, and check each time you refill.  on unparq reset.  
 * auto refill function      the task should include a check, for if auto-refill is on.  if on, automatically call refill 15 minutes.
 *      refill function should be passed a context activity, which will be used when we call refill etc???  
 * park only if detect internet  on the parq button, check if internet is active.  
 * SS Tutorial 
 * 
 * DO NOT RELY ON GOOD CONNECTION.  model should be - request change, send notice okay, make change on app, confirm change on server.
 * 
 * also must consider broken connections, so app must re-send refill requests that did not go through
 * 
 * move around, and make bigger, information/money display  
 *  
 * "Share Parq" option, pulls up qr code to scan.  
 * 
 * BOOT LOAD SERVICE RESUME
 * 
 * TimeLeftDisplay = analog timer, digital countdown (setting gives choice) 
 * Add server calls for rates.
 * 
 * 
 * SEcurity in authenticating with server?
 * 
 * look into city's expenses, number of parks, gauge the server costs, lay out finance to potential
 *    partners
 * */

public class MainActivity extends ActivityGroup {

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
	public static ViewFlipper vf;
	private int parkMinutes;
	private static CheckBox box;
	private static final int NUM_PICKER_ID = 2;
	private static final int REFILL_PICKER_ID = 0;

	public static final String SAVED_INFO = "ParqMeInfo";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.flipper);
		vf = (ViewFlipper) findViewById(R.id.flipper);
		final SharedPreferences check = getSharedPreferences(SAVED_INFO,0);
		//final SharedPreferences.Editor editor = check.edit();
		//crash on return, cause is here?
		if(SavedInfo.isParked(MainActivity.this)){
			vf.showNext();
		}
		//load correct layout which has the time selector and camera view.  
		priceDisplay = (TextView) findViewById(R.id.minutesprice);
		userDisplay = (TextView) findViewById(R.id.welcomeuser);
		locDisplay = (TextView) findViewById(R.id.location);
		timeDisplay = (TextView) findViewById(R.id.timeleftdisplay);
		setTimeButton = (Button) findViewById(R.id.firstparq);

		setTimeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(NUM_PICKER_ID);
			}
		});

		parqButton = (Button) findViewById(R.id.secondparq);
		parqButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!SavedInfo.isLoggedIn(MainActivity.this)){
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
			@Override
			public void onClick(View v) {
				//TODO ARE YOU SURE DIALOG
				String qrcode = check.getString("code", "badcode");
				String email = check.getString("email", "bademail");
				if(ServerCalls.unPark(qrcode, email)==1){
					stopService(new Intent(MainActivity.this, Background.class));
					vf.showPrevious();
					SavedInfo.togglePark(MainActivity.this);
					remainSeconds=parkMinutes*60;
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
			@Override
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
		@Override
		public void onNumberSet(int selectedNumber) {
			parkMinutes=selectedNumber;
			//remainSeconds = selectedNumber*60;
			updateDisplay();

		}
	};
	
	private void refillMe(int refillMinutes){
		remainSeconds+=refillMinutes*60;

		//stop current timer, start new timer with current time + selectedNumber.
		try{
			timer.cancel();
		}catch(Exception e){

		}
		timer = initiateTimer(remainSeconds, vf);
		//why not just 							instead of checking park state.
		//timer = initiateTimer(selectedNumber*60+remainSeconds);
		timer.start();
		stopService(new Intent(MainActivity.this, Background.class));
		startService(new Intent(MainActivity.this, Background.class).putExtra("time", remainSeconds));
		//TODO update server's endtime.  
	}
	private NumberPickerDialog.OnNumberSetListener mRefillListener =
		new NumberPickerDialog.OnNumberSetListener() {
		@Override
		public void onNumberSet(int selectedNumber) {
			refillMe(selectedNumber);
//			remainSeconds+=selectedNumber*60;
//
//			//stop current timer, start new timer with current time + selectedNumber.
//			try{
//				timer.cancel();
//			}catch(Exception e){
//
//			}
//			timer = initiateTimer(remainSeconds, vf);
//			//why not just 							instead of checking park state.
//			//timer = initiateTimer(selectedNumber*60+remainSeconds);
//			timer.start();
//			stopService(new Intent(MainActivity.this, Background.class));
//			startService(new Intent(MainActivity.this, Background.class).putExtra("time", remainSeconds));
//			//TODO update server's endtime.  
		}
	};
	private void updateDisplay() {
		priceDisplay.setText(parkMinutes +" Minutes"+" : " +moneyConverter(getCostInCents(parkMinutes)));
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case NUM_PICKER_ID:
			NumberPickerDialog x = new NumberPickerDialog(MainActivity.this, 0, 0);
			x.setOnNumberSetListener(mNumberSetListener);
			return x;

		case REFILL_PICKER_ID:
			NumberPickerDialog y = new NumberPickerDialog(this, 0,0);
			y.setOnNumberSetListener(mRefillListener);
			return y;
		}
		return null;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) { 
				remainSeconds=parkMinutes*60;
				String contents = intent.getStringExtra("SCAN_RESULT");
				//String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

				SharedPreferences check = getSharedPreferences(SAVED_INFO,0);
				String email = check.getString("email", "bademail");
				SharedPreferences.Editor editor = check.edit();
				editor.putString("code", contents);

				Date forString = new Date();
				forString.setSeconds(forString.getSeconds()+remainSeconds);
				String endtime = Global.sdf.format(forString);

				ParkObject myPark = ServerCalls.Park(contents, email, endtime);
				if(myPark!=null){
					timer = initiateTimer(remainSeconds, vf);
					timer.start();
					vf.showNext();
					startService(new Intent(MainActivity.this, Background.class).putExtra("time", remainSeconds));
					/*parkState changes how app resumes*/
					editor.putBoolean("parkState", true);
					editor.putFloat("lat", myPark.getLat());
					editor.putFloat("lon", myPark.getLon());
					userDisplay.setText("Welcome " + check.getString("fname", "")); 
					locDisplay.setText("You are parked at " + myPark.getLocation()+"\nSpot " + myPark.getSpotNum());
				}else{
					ThrowDialog.show(MainActivity.this, ThrowDialog.RESULT_ERROR);
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
			e.printStackTrace();
		}
		return "BADBADBAD";
	}

	public static long secToMil(int sec){
		return sec*1000;
	}
	
	public CountDownTimer initiateTimer(int countDownSeconds, final ViewFlipper myvf){
		
		
			return new CountDownTimer(secToMil(countDownSeconds), 1000){
				@Override
				public void onFinish() {
					timeDisplay.setText("Time Left: 0:00:00");
					myvf.showPrevious();
					refillMe(15);
				}
				@Override
				public void onTick(long arg0) {
					int seconds = (int)arg0/1000;
					remainSeconds = seconds;
					timeDisplay.setText("Time Left: " + formatMe(seconds));
				}

			};
		
	}
	//tada
	public void onBackPressed(){
		Log.d("CDA", "OnBackPressed Called");
		Intent setIntent = new Intent(Intent.ACTION_MAIN);
		setIntent.addCategory(Intent.CATEGORY_HOME);
		setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(setIntent);
		return;
	}
	public static void warnMe(Context activity){
		SharedPreferences check = activity.getSharedPreferences(SAVED_INFO,0);
		//if time warning is enabled
		if(check.getBoolean("warningEnable", false)){
			//vibrate if settings say so
			if(check.getBoolean("vibrateEnable", true)){
				((Vibrator)activity.getSystemService(VIBRATOR_SERVICE)).vibrate(1000);
			}
			if(check.getBoolean("ringEnable", true)){
				MediaPlayer mediaPlayer = MediaPlayer.create(activity, R.raw.soundfile);
				mediaPlayer.start(); // no need to call prepare(); create() does that for you
			}
		}
	}

}
