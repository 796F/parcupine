/* 
 * MainActivity represents the first tab, where users can scan a qr code,
 * select the amount of time they want to park, and park it.  Also while
 * parked, they are able to refill time and unpark themselves.  
 * 
 * */

package com.test;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.objects.Global;
import com.objects.ParkInstanceObject;
import com.objects.SpotObject;
import com.objects.SavedInfo;
import com.objects.ServerCalls;
import com.objects.ThrowDialog;
import com.quietlycoding.android.picker.NumberPicker;
import com.quietlycoding.android.picker.NumberPickerButton;
import com.quietlycoding.android.picker.NumberPickerDialog;

/**
 * connection ready display, ping server.  
 * robust communication model.  
 * 
 * new timer, crashes on timer end.  Also, two vars in saved pref holding endtime (endTime and TIMER).  use one.  
 * erase unnecessary instance variables such as remainSeconds.  
 * 
 * by saving all info, you can resume properly.  on create, use string starttime to find out how much time is left, calculate it
 * and then use it to initiate timer.  service should still be running, to resume app and auto-unpark and stuff.  have service conduct autorefill,
 * not the timer.  do so by editing a saved string for time, a service can edit it.  
 * 
 * edittext with tags, layout similar to citrix meeting app.  you can edit the layout of the actual edittext widget.	
 * 
 * save all info to be displayed, such as email etc, and load to prevent app from looking bad if resume doesn't work.  true "re-load"
 * refill should edit usertable and history table, and then edit app.  unparking then parking is lazy programming.  
 * edit add_history.php to check if users are currently parked, if true then update else insert.
 * sqlite db for parking history
 * internet detection
 * different user  
 * pop-up tutorial
 * write find car w/ pop-up for location, find parking (nearby? in general? how to do?)
 * create managerial app
 * time zone? how is it delt with on phone?  ON PARK, SAVE PRIVATE STARTTIME DATE OBJ.
 * 
 * compatibility!!!! works on emulators but not on phone.
 * app doesn't work on 2.3, the app always quits and services dont stop.
 * OR it isn't quitting, but the resume is just making a new instance of same app each time.  possible?
 * 
 * expense handling, create database of company charge tokens, for specific user email.  
 *    if the user's expense handlingn is flagged, check db for how much company has paid.  
 *    
 * refill complete dialog cancels after a while
 * settings should have option for warning time, save info on back, the time is read and passed when starting service
 * 
 * on park, grab lat/lon and compare with what we get from server for spot, combine and store for later.
 * park only if detect internet  on the parq button, check if internet is active.  
 * 
 * DO NOT RELY ON GOOD CONNECTION.  model should be - ping server, respond okay, make change on app, confirm change on server.
 * also must consider broken connections, so app must re-send refill requests that did not go through
 * 
 * perfect information display (sizing, borders, etc)
 * INSIDE NUMBER PICKER DISPLAY should show hrs:minutes ???
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
 * INCORPORATING NUMBERPICKER:  import class, start activity for view, setContentView of dialog
 * 
 * SEcurity in authenticating with server? encrypt, salt, and ssl all communication.
 * 
 * look into city's expenses, number of parks, gauge the server costs, lay out finance to potential
 *    partners
 * */

public class MainActivity extends ActivityGroup {

	/*Textual Display objects declared here*/
	private TextView priceDisplay;
	private TextView userDisplay;
	private TextView locDisplay;
	private TextView timeDisplay;

	/*Buttons declared here*/
	private EditText spotNum;
	private Button submitButton;
	private Button scanButton;
	private Button unparqButton;
	private Button refillButton;
	private Button minTimeButton;
	private Button maxTimeButton;
	private Button cancelPark;
	private Button parkButton;
	private Button gpsButton;
	private NumberPickerButton pickerIncButton;
	private NumberPickerButton pickerDecButton;

	/*ints used by calculations*/
	private int warnTime = 30; //in seconds
	private int remainSeconds; //in seconds
	private int parkMinutes;//in minutes
	private int minimalIncrement;//in minutes
	private int maxTime; //in minutes
	private int totalTimeParked = 0; //in minutes
	private int parkCost =0;

	/*various Objects used declared here*/
	private static SpotObject mySpot;
	private CountDownTimer timer;
	public static ViewFlipper vf;
	private AlertDialog alert;
	private static MediaPlayer mediaPlayer;
	private NumberPicker parkTimePicker;
	private Date globalEndTime;
	private RadioButton servPing;
	/*final variables*/
	public static final String SAVED_INFO = "ParqMeInfo";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		

		//check for internet on create, do it a lot later too.
		//use flipper view
		setContentView(R.layout.flipper);

		//Create refill dialog which has special components
		alert = makeRefillDialog();

		//hook elements
		priceDisplay = (TextView) findViewById(R.id.priceDisplay);
		userDisplay = (TextView) findViewById(R.id.welcomeuser);
		locDisplay = (TextView) findViewById(R.id.location);
		timeDisplay = (TextView) findViewById(R.id.timeleftdisplay);
		vf = (ViewFlipper) findViewById(R.id.flipper);
		parkTimePicker = (NumberPicker) findViewById(R.id.parktimepicker);
		mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.alarm);
		servPing = (RadioButton) findViewById(R.id.pingdot);
		final SharedPreferences check = getSharedPreferences(SAVED_INFO,0);

		/*	ON CREATE
		 * 	grab string from TIMER
		 * 	parse it to a date
		 * 	compare with current time
		 * 	
		 *	if time left, 
		 *		showNext() x2, initiate timer
		 *	if else
		 *		set TIMER = "none"
		 * 
		 * */

		try {
			Date timerEnd = Global.sdf.parse(check.getString("TIMER", ""));
			Date now = new Date();
			int seconds = (int)(timerEnd.getTime() - now.getTime())/1000;
			if(seconds>0){
				vf.showNext();
				vf.showNext();
				timer = initiateTimer(timerEnd, vf);
				timer.start();
			}else{
				SharedPreferences.Editor edit = check.edit();
				edit.putString("TIMER", "none");
				edit.commit();
			}
		} catch (ParseException e) {
			//bad time in timer, thus no timer was active.  do nothing.  
		}
		
		/*	Ping server in thread, 
		 * 	if server reachable 
		 * 		yay
		 * 	else 
		 * 		schedule runnable for 15 seconds later.  
		 * 		repeat till yay
		 * 
		 * */
		
		new Thread(new Runnable() {
		    public void run() {
		    	String host = "http://www.parqme.com";
				try {
					URL url = new URL(host);
					HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
					urlc.setConnectTimeout(1000 * 60); // mTimeout is in seconds
					urlc.connect();
					
					if (urlc.getResponseCode() == 200) {
						servPing.post(new Runnable(){
							@Override
							public void run() {
								servPing.setChecked(true);
								servPing.setText("Server Ready :D");
							}
						});
					}else{
						throw new Exception("LAAAA");
					}
				} catch (Exception e1) {
					servPing.post(new Runnable(){

						@Override
						public void run() {
							servPing.setChecked(false);
							servPing.setText("No Service :(");
						}
						
						
					});
				} 
		    	
		    }
		  }).start();
		
		//initialize buttons and set actions
		submitButton = (Button) findViewById(R.id.submitButton);
		submitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String contents = spotNum.getText().toString();
				// contents contains string "parqme.com/p/c36/p123456" or w/e...
				SharedPreferences check = getSharedPreferences(SAVED_INFO,0);

				mySpot = ServerCalls.getSpotInfo(contents,check);
				// if we get the object successfully
				if (mySpot != null) {
					vf.showNext();
					// prepare time picker for this spot
					parkTimePicker.setRange(mySpot.getMinIncrement(),
							mySpot.getMaxTime());
					parkTimePicker.setMinInc(mySpot.getMinIncrement());

					// initialize all variables to match spot
					minimalIncrement = mySpot.getMinIncrement();
					maxTime = mySpot.getMaxTime();

					parkMinutes = minimalIncrement;
					remainSeconds = parkMinutes * 60;
					// store some used info
					SharedPreferences.Editor editor = check.edit();
					editor.putString("code", contents);
					editor.putFloat("lat", (float) mySpot.getLat());
					editor.putFloat("lon", (float) mySpot.getLon());
					editor.commit();
					updateDisplay();
				} else {
					ThrowDialog.show(MainActivity.this,
							ThrowDialog.RESULT_ERROR);
				}
			}
		});
		spotNum = (EditText) findViewById(R.id.spot_num);
		spotNum.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				submitButton.setEnabled(s.length() == 1);
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
		scanButton = (Button) findViewById(R.id.scanButton);
		scanButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!SavedInfo.isLoggedIn(MainActivity.this)){
					//if not logged in, alert user via dialog
					ThrowDialog.show(MainActivity.this, ThrowDialog.MUST_LOGIN);
				}else{

					//else start scan intent
					IntentIntegrator.initiateScan(MainActivity.this);
				}
			}});


		parkButton = (Button) findViewById(R.id.parkbutton);
		parkButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//call park, which ONLY updates server and gives us response code
				int parkResult = park();
				//if that response code is good,
				if (parkResult==1){
					totalTimeParked+=parkMinutes;
					parkCost = getCostInCents(parkMinutes);
					if(totalTimeParked == maxTime){
						ThrowDialog.show(MainActivity.this, ThrowDialog.MAX_TIME);
					}
					vf.showNext();

					//TODO: DELETE ME FOR TESTING
					remainSeconds = 60;
					parkMinutes = 1;
					//END TESTING CODE

					//create and start countdown display
					try {
						timer = initiateTimer(Global.sdf.parse(check.getString("TIMER", "")), vf);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					timer.start();
					//start timer background service
					startService(new Intent(MainActivity.this, Background.class).putExtra("time", remainSeconds));

					SharedPreferences check = getSharedPreferences(SAVED_INFO,0);
					SharedPreferences.Editor editor = check.edit();
					/*mark the user as currently parked*/
					editor.putBoolean("parkState", true);
					editor.commit();

					//display where user is parked and who they are
					userDisplay.setText("Welcome " + check.getString("fname", "")); 
					locDisplay.setText("You are parked at\n" + mySpot.getLocation()+"\nAt spot # " + mySpot.getSpot());


				}else{
					ThrowDialog.show(MainActivity.this, ThrowDialog.RESULT_ERROR);
				}
			}});


		unparqButton = (Button) findViewById(R.id.unparqbutton);
		unparqButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setMessage("Are you sure you want to unpark?")
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						//if they selected yes, call unpark()
						int unparkResult= unpark();
						if(unparkResult==1){
							//reset ints
							parkMinutes=0;
							totalTimeParked=0;
							remainSeconds=0;
							minimalIncrement=0;
							maxTime=0;
							parkCost=0;

							//change view back to initial one
							vf.showPrevious();
							vf.showPrevious();
							//and set user as not currently parked
							SharedPreferences.Editor editor = check.edit();
							editor.putBoolean("parkState", false);
							editor.putString("TIMER", "");
							editor.commit();
						}else{
							Toast.makeText(MainActivity.this, unparkResult, Toast.LENGTH_LONG);
						}
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
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
				if(totalTimeParked<maxTime){
					NumberPickerDialog y = new NumberPickerDialog(MainActivity.this, 0,0);
					y.setRange(mySpot.getMinIncrement(), mySpot.getMaxTime()-totalTimeParked);
					y.setMinInc(mySpot.getMinIncrement());
					y.setOnNumberSetListener(mRefillListener);
					y.show();
				}else{
					ThrowDialog.show(MainActivity.this, ThrowDialog.MAX_TIME);
				}
			}});

		minTimeButton = (Button) findViewById(R.id.minparktime);
		minTimeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				parkTimePicker.setCurrent(minimalIncrement);
				parkMinutes=minimalIncrement;
				remainSeconds=minimalIncrement*60;
				updateDisplay();
			}});
		maxTimeButton = (Button) findViewById(R.id.maxparktime);
		maxTimeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				parkTimePicker.setCurrent(maxTime);
				parkMinutes=maxTime;
				remainSeconds=maxTime*60;
				updateDisplay();
			}});
		cancelPark = (Button) findViewById(R.id.cancelpark);
		cancelPark.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mySpot=null;
				//return to previous view
				vf.showPrevious();
			}});

		/*these buttons appear on the first park, not refills.*/
		pickerIncButton = parkTimePicker.getIncButton();
		pickerIncButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(parkMinutes<maxTime){
					//update the parking minutes and seconds
					parkMinutes+=minimalIncrement;
					remainSeconds+=minimalIncrement*60;
					parkTimePicker.setCurrent(parkMinutes);
					updateDisplay();
				}
			}
		});
		pickerDecButton = parkTimePicker.getDecButton();
		pickerDecButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(parkMinutes>minimalIncrement){
					//update the parking minutes and seconds
					parkMinutes-=minimalIncrement;
					remainSeconds-=minimalIncrement*60;
					parkTimePicker.setCurrent(parkMinutes);
					updateDisplay();
				}
			}
		});

	}

	private AlertDialog makeRefillDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setMessage("You are almost out of time!")
		.setCancelable(false)
		.setPositiveButton("Refill", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				if(totalTimeParked<maxTime)
					refillMe(1);
				//refillMe(minimalIncrement);
				else
					ThrowDialog.show(MainActivity.this, ThrowDialog.MAX_TIME);
				//mediaPlayer.stop();
			}
		})
		.setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				//mediaPlayer.stop();
			}
		});
		return builder.create();
	}

	/**
	 * unpark and park are methods that handle ending/initializing
	 * the background service, as well as sending queries to the server. 
	 * So basically, changes that happen behind the app.*/
	private int unpark(){
		//grab the qr code and user email from saved info
		SharedPreferences check = getSharedPreferences(SAVED_INFO,0);
		String qrcode = check.getString("code", "badcode");
		String email = check.getString("email", "bademail");
		//use them to unpark the user serverside

		if (ServerCalls.unPark(qrcode, email)==1){
			try{
				//stop the countdown timer and service
				timer.cancel();
				stopService(new Intent(MainActivity.this, Background.class));
				//return happy
				return 1;
			}catch (Exception e){
				return 0;
			}
		}else{
			return 0;
		}

	}
	private int park(){
//		//grab qr code content from saved info.  
//		SharedPreferences check = getSharedPreferences(SAVED_INFO,0);
//		String contents = check.getString("code", "");
//
//		String email = check.getString("email", "bademail");
//		//calculate when parking ends
//		globalEndTime = new Date();
//		String starttime = Global.sdf.format(globalEndTime);
//		String parkDate = Global.sdfDate.format(globalEndTime);		
//		globalEndTime.setSeconds(globalEndTime.getSeconds()+remainSeconds);
//		String endtime = Global.sdf.format(globalEndTime);

		final SharedPreferences prefs = getSharedPreferences(SAVED_INFO, 0);

		final ParkInstanceObject parkInstance = ServerCalls.park(mySpot.getSpot(), remainSeconds, prefs);
		if(parkInstance != null){
			//Toast.makeText(MainActivity.this, ""+ServerCalls.addHistory(parkDate, starttime, endtime, contents, email, parkCost), Toast.LENGTH_SHORT).show();
			SharedPreferences.Editor edit = prefs.edit();
			edit.putString("PARKID", String.valueOf(parkInstance.getParkInstanceId()));
			edit.putString("TIMER", String.valueOf(parkInstance.getEndTime()));
			edit.commit();
//			ServerCalls.addHistory(parkDate, starttime, endtime, contents, email, parkCost);
			//return a happy response
			return 1;
		}else{
			ThrowDialog.show(MainActivity.this, ThrowDialog.UNPARK_ERROR);
			return 0;
		}
	}



	private void refillMe(int refillMinutes){
		//if we haven't gone past the total time we're allowed to park
		if(totalTimeParked+refillMinutes<=maxTime){

			//update the total time parked and remaining time.
			parkMinutes+=refillMinutes;
			parkCost = getCostInCents(parkMinutes);
			totalTimeParked+=refillMinutes;
			remainSeconds+=refillMinutes*60;
			updateDisplay();

			//TODO:  DELETE ME TESTING CODE
			//remainSeconds+=60;
			//END TESTING CODE

			//stop current timer, start new timer with current time + selectedNumber.
			try{
				
				SharedPreferences check = getSharedPreferences(SAVED_INFO,0);
				Date oldEnd = Global.sdf.parse(check.getString("TIMER", ""));
				oldEnd.setMinutes(oldEnd.getMinutes() + refillMinutes);
				//calculate new endtime and initiate timer from it.  
				timer.cancel();
				timer = initiateTimer(oldEnd, vf);
				stopService(new Intent(MainActivity.this, Background.class));
				int testing = unpark();
				int testing2 = park();
				timer.start();
				startService(new Intent(MainActivity.this, Background.class).putExtra("time", remainSeconds));
				if(totalTimeParked==maxTime){
					ThrowDialog.show(MainActivity.this, ThrowDialog.MAX_TIME);
				}else{
					ThrowDialog.show(MainActivity.this, ThrowDialog.REFILL_DONE);
				}
			}catch(Exception e){
				ThrowDialog.show(MainActivity.this, ThrowDialog.UNPARK_ERROR);
			}
		}
	}

	private NumberPickerDialog.OnNumberSetListener mRefillListener =
		new NumberPickerDialog.OnNumberSetListener() {
		@Override
		public void onNumberSet(int selectedNumber) {
			if(selectedNumber>=minimalIncrement)
				//TODO testing delete me
				refillMe(1);
			//refillMe(selectedNumber);
		}
	};


	//once we scan the qr code
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		final IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if (scanResult != null) {
			//call server using the qr code, to get a resulting spot's info.
			String contents = scanResult.getContents();
			if (contents != null) {
				//contents contains string "parqme.com/p/c36/p123456" or w/e...
				SharedPreferences check = getSharedPreferences(SAVED_INFO,0);
				mySpot = ServerCalls.getSpotInfo(contents, check);
				//if we get the object successfully
				if(mySpot!=null){
					vf.showNext();
					//prepare time picker for this spot
					parkTimePicker.setRange(mySpot.getMinIncrement(), mySpot.getMaxTime());
					parkTimePicker.setMinInc(mySpot.getMinIncrement());

					//initialize all variables to match spot
					minimalIncrement=mySpot.getMinIncrement();
					maxTime = mySpot.getMaxTime();

					//TODO delete me testing
					parkMinutes=1;
					//parkMinutes=minimalIncrement;
					remainSeconds=parkMinutes*60;

					//store some used info
					SharedPreferences.Editor editor = check.edit();
					editor.putString("code", contents);
					editor.putFloat("lat", (float) mySpot.getLat());
					editor.putFloat("lon", (float) mySpot.getLon());
					editor.commit();
					updateDisplay();
				}else{
					ThrowDialog.show(MainActivity.this, ThrowDialog.RESULT_ERROR);
				}
			} else {
				//do nothing if the user doesn't scan and just cancels.
				//call server using the qr code, to get a resulting spot's info.
				contents = "3";
				SharedPreferences check = getSharedPreferences(SAVED_INFO,0);
				//contents contains string "parqme.com/p/c36/p123456" or w/e...
				mySpot = ServerCalls.getSpotInfo(contents, check);
				//if we get the object successfully
				if(mySpot!=null){
					vf.showNext();
					//prepare time picker for this spot
					parkTimePicker.setRange(mySpot.getMinIncrement(), mySpot.getMaxTime());
					parkTimePicker.setMinInc(mySpot.getMinIncrement());
					//initialize all variables to match spot
					minimalIncrement=mySpot.getMinIncrement();
					maxTime = mySpot.getMaxTime();

					parkMinutes=minimalIncrement;
					remainSeconds=parkMinutes*60;

					//store some used info
					SharedPreferences.Editor editor = check.edit();
					editor.putString("code", contents);
					editor.putFloat("lat", (float) mySpot.getLat());
					editor.putFloat("lon", (float) mySpot.getLon());
					editor.commit();
					updateDisplay();
				}else{
					ThrowDialog.show(MainActivity.this, ThrowDialog.RESULT_ERROR);
				}
			}
		}
	}

	/* THIS TIMER is only used for in-app visuals.  The actual server updating and such are
	 * done via user button clicks, and the background service we run.  */
	public CountDownTimer initiateTimer(Date endTime, final ViewFlipper myvf){
		//creates the countdown timer
		Date now = new Date();
		int countDownSeconds = (int)(endTime.getTime() - now.getTime())/1000;
		return new CountDownTimer(countDownSeconds*1000, 1000){
			//on each 1 second tick, 
			@Override
			public void onTick(long arg0) {

				int seconds = (int)arg0/1000;
				//if the time is what our warning-time is set to
				if(seconds==warnTime&&SavedInfo.autoRefill(MainActivity.this)==false){
					//alert the user
					alert.show();
				}
				//update remain seconds and timer.
				remainSeconds = seconds;
				timeDisplay.setText(formatMe(seconds));
			}
			//on last tick,
			//TODO calling alert.cancel() or throwdialog when app isn't in forefront may cause crash?
			@Override
			public void onFinish() {
				//timeDisplay.setText("0:00:00");
				SharedPreferences check = getSharedPreferences(SAVED_INFO,0);
				//if autorefill is on, refill the user minimalIncrement
				if(check.getBoolean("autoRefill", false)){
					alert.cancel();
					refillMe(1);
				}else{
					SavedInfo.eraseTimer(MainActivity.this);
					//else we cancel the running out of tie dialog
					alert.cancel();
					//and restore view
					myvf.showPrevious();
					myvf.showPrevious();
					ThrowDialog.show(MainActivity.this, ThrowDialog.TIME_OUT);
				}
			}


		};

	}
	public static void warnMe(Context activity){
		SharedPreferences check = activity.getSharedPreferences(SAVED_INFO,0);
		//if time warning is enabled
		if(check.getBoolean("warningEnable", false)){
			//vibrate if settings say so
			if(check.getBoolean("vibrateEnable", false)){
				((Vibrator)activity.getSystemService(VIBRATOR_SERVICE)).vibrate(1000);
			}
			if(check.getBoolean("ringEnable", false)){
				mediaPlayer.start();
			}
		}
	}


	//converts cents to "$ x.xx"
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
	//converts seconds to "H:mm:ss"
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
	//TODO:  THIN METHOD -- will become robust later.  must support varying units and different parking minutes and all day parking etc.
	private static int getCostInCents(int mins) {
		//number of minutes parked, divided by the minimum increment (aka unit of time), multiplied by price per unit in cents
		return (mins/(mySpot.getMinIncrement())) * mySpot.getDefaultRate();
	}
	private void updateDisplay() {	
		priceDisplay.setText(moneyConverter(getCostInCents(parkMinutes)));
	}
}
