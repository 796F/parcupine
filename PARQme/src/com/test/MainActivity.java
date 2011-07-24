package com.test;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

import com.quietlycoding.android.picker.NumberPickerDialog;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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
 * Add time and charge logic.
 * Add servlet calls for rates.
 * currently crashing on parqButton after user comes back from TimeLeft Activity.  
 * Buttons do not cancel correctly.  
 * PARKING is free after 7pm.  include a time check method which may alertdialog.  
 * WRITE alert dialog class for NumberPicker, looks bad.
 * Phone should vibrate etc if 5 minutes remain,
 *    service should shutdown once time is up. 
 * On restore should take us back to unparq/refill page.  use sql lite to store time, and resume on boot
 * */

public class MainActivity extends Activity {

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

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.flipper);
		vf = (ViewFlipper) findViewById(R.id.flipper);

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
				Intent intent = new Intent("com.google.zxing.client.android.MYSCAN");
				intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
				startActivityForResult(intent, 0);
				vf.showNext();
				startService(new Intent(MainActivity.this, Background.class).putExtra("time", parkMinutes));
				
			}});

		
		unparqButton = (Button) findViewById(R.id.unparqbutton);
		unparqButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				//	    		are you sure? dialogue
				//	    				if sure start summary activity
				//	    				          this has time parked, amount spent, 
				stopService(new Intent(MainActivity.this, Background.class));
				vf.showPrevious();
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
			if (resultCode == RESULT_OK) {


				int contentInt = intent.getIntExtra("SCAN_RESULT", Global.BAD_RESULT_CODE);
				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				
				/*scan results will contain an integer number, which represents a park location*/

				/*send request to server with email + contentInt + time*/
				timeDisplay.setText("RESULT GOT" + contents);
				contactServer("test", contentInt, parkMinutes);



			} else if (resultCode == RESULT_CANCELED) {
				priceDisplay.setText("RESULT BAD GOT :(");
				// Handle cancel
			}
		}
	}


	public int contactServer(String email, int contentInt, int time){
		try {
			URL url = new URL("http://192.268.1.57:8080/UserBounce/UpdateDatabase");

			URLConnection servletConnection = url.openConnection();

			// inform the connection that we will send output and accept input
			servletConnection.setDoInput(true);
			servletConnection.setDoOutput(true);

			// Don't use a cached version of URL connection.
			servletConnection.setUseCaches(false);
			servletConnection.setDefaultUseCaches(false);

			// Specify the content type that we will send binary data
			servletConnection.setRequestProperty("Content-Type", "application/x-java-serialized-object");

			// get input and output streams on servlet
			ObjectOutputStream os = new ObjectOutputStream(servletConnection.getOutputStream());

			// send your data to the servlet
			os.writeObject("TESTEMAIL@JA.COM" +","+contentInt+","+parkMinutes);
			os.flush();
			os.close();

			ObjectInputStream iStream = new ObjectInputStream(servletConnection.getInputStream());
			int responseCode = iStream.readInt();

			if(responseCode==0){
				//spot taken
			}else if(responseCode==1){
				//spot okay
				return 1;
			}else if(responseCode==2){
				//jus tin case.  
			}else{
				//spoof code.  easter egg ascii art
			}

			/*read response code and interprit*/

		}catch (Exception e){
			e.printStackTrace();
		}
		return 0;
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
