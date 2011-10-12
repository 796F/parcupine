package com.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.objects.Global;
import com.objects.SavedInfo;
import com.objects.ServerCalls;
import com.objects.ThrowDialog;

import android.app.Activity;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class DebugActivity extends Activity {
	
	private CountDownTimer timer;
	private TextView display;
	private Button debugbutton;
	private LocationManager locationManager;
	private String bestProvider;
	int remainSeconds;
	public static final String SAVED_INFO = "ParqMeInfo";
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.debuglayout);
	    SharedPreferences check = getSharedPreferences(SAVED_INFO,0);
		
	    //initiate timer on creation of activity
	    String endtime = check.getString("TIMER", "notimer");
	    if(!endtime.equals("notimer")){
	    	Date endObject;
			try {
				endObject = Global.sdf.parse(endtime);
				Date now = new Date();
			    int seconds = (int)(endObject.getTime() - now.getTime())/1000;
			    timer = initiateTimer(seconds, null);
			    timer.start();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    
	    
	    debugbutton = (Button) findViewById(R.id.debugButton1);
	    display = (TextView) findViewById(R.id.debugText1);

	    debugbutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Date nowtime = new Date();
				nowtime.setMinutes((nowtime.getMinutes() + 5));
				SharedPreferences check = getSharedPreferences(SAVED_INFO,0);
				SharedPreferences.Editor editor = check.edit();
				editor.putString("TIMER", Global.sdf.format(nowtime));
				editor.commit();
				timer = initiateTimer(300, null);
				timer.start();
			}
			
		});
	    
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
	public CountDownTimer initiateTimer(int countDownSeconds, final ViewFlipper myvf){
		//creates the countdown timer
		return new CountDownTimer(countDownSeconds*1000, 1000){
			//on each 1 second tick, 
			@Override
			public void onTick(long arg0) {
				int seconds = (int)arg0/1000;
				//update remain seconds and timer.
				remainSeconds = seconds;
				display.setText(formatMe(seconds));
			}
			//on last tick,
			@Override
			public void onFinish() {
				//timeDisplay.setText("0:00:00");
				SharedPreferences check = getSharedPreferences(SAVED_INFO,0);
				//if autorefill is on, refill the user minimalIncrement
				if(check.getBoolean("autoRefill", false)){
					timer = initiateTimer(300, null);
					timer.start();
				}else{
					SharedPreferences.Editor editor = check.edit();
					editor.putString("TIMER", "notimer");
					editor.commit();
				}
			}


		};

	}
}
