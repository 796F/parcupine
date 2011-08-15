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
import android.content.DialogInterface;
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
 * settings should have option for warning time, save info on back, the time is read and passed when starting service
 * the warning message should have refill option and ignore option
 * 
 * on park, grab lat/lon and compare with what we get from server for spot, combine and store for later.
 * park only if detect internet  on the parq button, check if internet is active.  
 * SS Tutorial 
 * 
 * DO NOT RELY ON GOOD CONNECTION.  model should be - request change, send notice okay, make change on app, confirm change on server.
 * also must consider broken connections, so app must re-send refill requests that did not go through
 * 
 * move around, and make bigger, information/money display  
 * 
 * "Share Parq" option, pulls up qr code to scan.  
 * 
 * flash light when dark
 * 
 * BOOT LOAD SERVICE RESUME
 * 
 * TimeLeftDisplay = analog timer, digital countdown (setting gives choice) 
 * Add server calls for rates.
 * 
 * INCORPORATING NUMBERPICKER:  inport class, start activity for view, setContentView of dialog
 * 
 * SEcurity in authenticating with server? encrypt, salt, and ssl all communication.
 * 
 * look into city's expenses, number of parks, gauge the server costs, lay out finance to potential
 *    partners
 * */

public class MainActivity extends ActivityGroup {

/*  Changes were made for testing . to revert...
 * 	change private warntime back to 5 minutes
	change setTime listener,
	change refill listener
	change service warningTime seconds */
	
	
	private TextView priceDisplay;
	private TextView userDisplay;
	private TextView locDisplay;
	private TextView timeDisplay;
	private Button setTimeButton;
	private Button parqButton;
	private Button unparqButton;
	private Button refillButton;
	private int remainSeconds;
	private CountDownTimer timer;
	public static ViewFlipper vf;
	private int parkMinutes;
	private int parkRate;
	private int warnTime = 30;
	private AlertDialog alert;
	private static final int NUM_PICKER_ID = 2;
	private static final int REFILL_PICKER_ID = 0;

	public static final String SAVED_INFO = "ParqMeInfo";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setMessage("You are almost out of time!")
		.setCancelable(false)
		.setPositiveButton("Refill", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				refillMe(1);
			}
		})
		.setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		 alert = builder.create();
		
		
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
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setMessage("Are you sure you want to unparq?")
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						unpark();
						vf.showPrevious();
						SavedInfo.togglePark(MainActivity.this);
						remainSeconds=parkMinutes*60;
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}});


		refillButton = (Button) findViewById(R.id.refillbutton);

		refillButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(REFILL_PICKER_ID);
			}});

	}

	private int unpark(){
		//service not stopping?  unparqing
		SharedPreferences check = getSharedPreferences(SAVED_INFO,0);
		String qrcode = check.getString("code", "badcode");
		String email = check.getString("email", "bademail");
		int test = ServerCalls.unPark(qrcode, email);

			try{
				timer.cancel();
				stopService(new Intent(MainActivity.this, Background.class));
				return 1;
			}catch (Exception e){
				ThrowDialog.show(MainActivity.this, ThrowDialog.UNPARK_ERROR);
				return 0;
			}
		
	}
	private int park(){
		SharedPreferences check = getSharedPreferences(SAVED_INFO,0);
		String contents = check.getString("code", "");

		String email = check.getString("email", "bademail");
		Date forString = new Date();
		forString.setSeconds(forString.getSeconds()+remainSeconds);
		String endtime = Global.sdf.format(forString);
		
		if(ServerCalls.Park(contents, email, endtime)!=null){
			return 1;
		}else{
			ThrowDialog.show(MainActivity.this, ThrowDialog.UNPARK_ERROR);
			return 0;
		}
	}
	private static int getRates(int LOCATION_CODE){
		//TODO: pass in park object instead of location code.
		return 25;
	}


	private NumberPickerDialog.OnNumberSetListener mNumberSetListener = 
		new NumberPickerDialog.OnNumberSetListener() {
		@Override
		public void onNumberSet(int selectedNumber) {

			//parkMinutes=selectedNumber;
			//TODO: delete me, for testing
			parkMinutes=1;
			updateDisplay();

		}
	};

	private void refillMe(int refillMinutes){
		//remainSeconds+=refillMinutes*60;
		remainSeconds+=60;
		//stop current timer, start new timer with current time + selectedNumber.
		try{
			timer.cancel();
			timer = initiateTimer(remainSeconds, vf);
			stopService(new Intent(MainActivity.this, Background.class));
			int testing = unpark();
			int testing2 = park();
			timer.start();
			startService(new Intent(MainActivity.this, Background.class).putExtra("time", remainSeconds));
			ThrowDialog.show(MainActivity.this, ThrowDialog.REFILL_DONE);
			
		}catch(Exception e){
			ThrowDialog.show(MainActivity.this, ThrowDialog.UNPARK_ERROR);
		}
		
	}
	
	private NumberPickerDialog.OnNumberSetListener mRefillListener =
		new NumberPickerDialog.OnNumberSetListener() {
		@Override
		public void onNumberSet(int selectedNumber) {
			refillMe(selectedNumber);
		}
	};

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
					parkRate=myPark.getRate();
					timer = initiateTimer(remainSeconds, vf);
					timer.start();
					vf.showNext();
					startService(new Intent(MainActivity.this, Background.class).putExtra("time", remainSeconds));
					/*parkState changes how app resumes*/
					editor.putBoolean("parkState", true);
					editor.putFloat("lat", myPark.getLat());
					editor.putFloat("lon", myPark.getLon());
					userDisplay.setText("Welcome " + check.getString("fname", "")); 
					locDisplay.setText("You are parked at\n" + myPark.getLocation()+"\nAt spot # " + myPark.getSpotNum());
				}else{
					ThrowDialog.show(MainActivity.this, ThrowDialog.RESULT_ERROR);
				}

				editor.commit();


			} else if (resultCode == RESULT_CANCELED) {

			} 
		}
	}


	public CountDownTimer initiateTimer(int countDownSeconds, final ViewFlipper myvf){


		return new CountDownTimer(secToMil(countDownSeconds), 1000){
			
			@Override
			public void onFinish() {
				timeDisplay.setText("0:00:00");
				SharedPreferences check = getSharedPreferences(SAVED_INFO,0);
				if(check.getBoolean("autoRefill", false)){
					refillMe(1);
					alert.cancel();
				}else{
					alert.cancel();
					myvf.showPrevious();
					ThrowDialog.show(MainActivity.this, ThrowDialog.TIME_OUT);
				}
			}
			@Override
			public void onTick(long arg0) {
				int seconds = (int)arg0/1000;
				if(seconds==warnTime){
					
					alert.show();
				
				}
				remainSeconds = seconds;
				timeDisplay.setText(formatMe(seconds));
			}

		};

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
				MediaPlayer mediaPlayer = MediaPlayer.create(activity, R.raw.alarm);
				mediaPlayer.start(); // no need to call prepare(); create() does that for you
			}
		}
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


	private static int getCostInCents(int mins) {
		return mins/15 * getRates(0);

	}
	private void updateDisplay() {
		priceDisplay.setText(parkMinutes +" Minutes"+" : " +moneyConverter(getCostInCents(parkMinutes)));
	}
}
