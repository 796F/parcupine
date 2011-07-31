package com.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.objects.Global;
import com.objects.SavedInfo;
import com.objects.ThrowDialog;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;

public class Background extends Service{
	private int parkTime;
	private Bundle b;
	public static final String SAVED_INFO = "ParqMeInfo";
	private Timer x;
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		b = intent.getExtras();

		final SharedPreferences check = getSharedPreferences(SAVED_INFO,0);
		final SharedPreferences.Editor editor = check.edit();
		//grab current time, and declare end time
		Date currentTime = new Date();
		Date endTime = new Date();
		Date warningTime = new Date();
		//add park minutes to current time's mins and set as end time.
		
		//TODO make this calculated in seconds.
		//warningTime.setMinutes(currentTime.getMinutes()+b.getInt("time")-5);
		warningTime.setSeconds(currentTime.getSeconds()+b.getInt("time")-5*60);

		//endTime.setMinutes(currentTime.getMinutes() + b.getInt("time"));
		endTime.setSeconds(currentTime.getSeconds() + b.getInt("time"));
		
		//create formatter and get endtime string
		String endTimeString = Global.sdf.format(endTime);
		editor.putString("endTime", endTimeString);
		editor.commit();
		//ON REFILL, stop this service, RECALCULATE ENDTIME by adding refill mins, AND RESTART service with new time.
		
		x = new Timer();
		x.schedule(new TimerTask(){
			@Override
			public void run() {
				// vibrate, ding, and bring up the refill page.  
				//pass intent that started the main activity?? instead of new Intent();
				Intent myIntent = new Intent(Background.this, TabsActivity.class);
				myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(myIntent);
				((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(1000);
			}
		}, warningTime);
		
		x.schedule(new TimerTask(){
			@Override
			public void run() {
				//unparq user from server, stop the service, Vibrate
				((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(2000);
				//stopService(new Intent(Background.this, Background.class));
				 String qrcode = check.getString("code", "badcode");
				 String email = check.getString("email", "bademail");
				 if(ServerCalls.unPark(qrcode, email)==1){
					stopService(new Intent(Background.this, Background.class));
					MainActivity.vf.showPrevious();
					SavedInfo.togglePark(Background.this);
				 }else{
					ThrowDialog.show(Background.this, ThrowDialog.UNPARK_ERROR);
				 }
				
				
				editor.putBoolean("parkState", false);
				editor.commit();
			}
		},endTime);
		
		//schedule(TimerTask task, Date time) executes task at the time specified.  
		//so create a date object for 5 minutes before time runs out.
		return START_STICKY;
	}
	public void onDestroy(){
		super.onDestroy();
		x.cancel();
	}

}
